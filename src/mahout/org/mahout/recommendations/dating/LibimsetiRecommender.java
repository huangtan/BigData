package org.mahout.recommendations.dating;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.iterator.FileLineIterable;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.apache.mahout.cf.taste.web.RecommenderWrapper.readResourceToTempFile;

/**
 * Download dating site ratings from here:
 * http://www.occamslab.com/petricek/data/libimseticomplete.zip
 *
 * @author Krisztian Horvath
 */
public class LibimsetiRecommender implements Recommender {

    private final Recommender delegate;
    private final DataModel model;
    private final FastIDSet men;
    private final FastIDSet women;
    private final FastIDSet usersRateMoreMen;
    private final FastIDSet usersRateLessMen;

    public LibimsetiRecommender() throws TasteException, IOException {
        this(new FileDataModel(readResourceToTempFile("src/main/resources/ratings.dat")));
    }

    public LibimsetiRecommender(DataModel model) throws TasteException, IOException {
        this.model = model;
        UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
        delegate = new GenericUserBasedRecommender(model, neighborhood, similarity);
        FastIDSet[] menWomen = parseMenWomen(readResourceToTempFile("src/main/resources/gender.dat"));
        men = menWomen[0];
        women = menWomen[1];
        usersRateMoreMen = new FastIDSet(50000);
        usersRateLessMen = new FastIDSet(50000);
    }

    private FastIDSet[] parseMenWomen(File genderFile) throws IOException {
        FastIDSet men = new FastIDSet(50000);
        FastIDSet women = new FastIDSet(50000);
        for (String line : new FileLineIterable(genderFile)) {
            int comma = line.indexOf(',');
            char gender = line.charAt(comma + 1);
            if (gender == 'U') {
                continue;
            }
            long profileID = Long.parseLong(line.substring(0, comma));
            if (gender == 'M') {
                men.add(profileID);
            } else {
                women.add(profileID);
            }
        }
        men.rehash();
        women.rehash();
        return new FastIDSet[]{men, women};
    }

    @Override
    public List<RecommendedItem> recommend(long userID, int howMany) throws TasteException {
        IDRescorer rescorer = new GenderRescorer(men, women, usersRateMoreMen, usersRateLessMen, userID, model);
        return delegate.recommend(userID, howMany, rescorer);
    }

    @Override
    public List<RecommendedItem> recommend(long userID, int howMany, IDRescorer rescorer) throws TasteException {
        return delegate.recommend(userID, howMany, rescorer);
    }

    @Override
    public float estimatePreference(long userID, long itemID) throws TasteException {
        IDRescorer rescorer = new GenderRescorer(men, women, usersRateMoreMen, usersRateLessMen, userID, model);
        return (float) rescorer.rescore(itemID, delegate.estimatePreference(userID, itemID));
    }

    @Override
    public void setPreference(long userID, long itemID, float value) throws TasteException {
        delegate.setPreference(userID, itemID, value);
    }

    @Override
    public void removePreference(long userID, long itemID) throws TasteException {
        delegate.removePreference(userID, itemID);
    }

    @Override
    public DataModel getDataModel() {
        return delegate.getDataModel();
    }

    @Override
    public void refresh(Collection<Refreshable> alreadyRefreshed) {
        delegate.refresh(alreadyRefreshed);
    }
}
