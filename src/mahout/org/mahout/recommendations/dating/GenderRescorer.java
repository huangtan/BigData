package org.mahout.recommendations.dating;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.IDRescorer;

/**
 * Download dating site ratings from here:
 * http://www.occamslab.com/petricek/data/libimseticomplete.zip
 *
 * @author Krisztian Horvath
 */
public class GenderRescorer implements IDRescorer {

    private final FastIDSet men;
    private final FastIDSet women;
    private final FastIDSet usersRateMoreMen;
    private final FastIDSet usersRateLessMen;
    private final boolean filterMen;

    public GenderRescorer(FastIDSet men,
                          FastIDSet women,
                          FastIDSet usersRateMoreMen,
                          FastIDSet usersRateLessMen,
                          long userID,
                          DataModel model) throws TasteException {
        this.men = men;
        this.women = women;
        this.usersRateMoreMen = usersRateMoreMen;
        this.usersRateLessMen = usersRateLessMen;
        this.filterMen = ratesMoreMen(userID, model);
    }

    @Override
    public double rescore(long id, double originalScore) {
        return isFiltered(id) ? Double.NaN : originalScore;
    }

    @Override
    public boolean isFiltered(long id) {
        return filterMen ? men.contains(id) : women.contains(id);
    }

    private boolean ratesMoreMen(long userID, DataModel model) throws TasteException {
        if (usersRateMoreMen.contains(userID)) {
            return true;
        }
        if (usersRateLessMen.contains(userID)) {
            return false;
        }
        PreferenceArray prefs = model.getPreferencesFromUser(userID);
        int menCount = 0;
        int womenCount = 0;
        for (int i = 0; i < prefs.length(); i++) {
            long profileID = prefs.get(i).getItemID();
            if (men.contains(profileID)) {
                menCount++;
            } else if (women.contains(profileID)) {
                womenCount++;
            }
        }
        boolean ratesMoreMen = menCount > womenCount;
        if (ratesMoreMen) {
            usersRateMoreMen.add(userID);
        } else {
            usersRateLessMen.add(userID);
        }
        return ratesMoreMen;
    }

}
