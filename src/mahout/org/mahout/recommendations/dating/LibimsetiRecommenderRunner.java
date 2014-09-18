package org.mahout.recommendations.dating;

import org.apache.mahout.cf.taste.recommender.Recommender;

/**
 * @author Krisztian Horvath
 */
public class LibimsetiRecommenderRunner {

    public static void main(String[] args) throws Exception {
        Recommender recommender = new LibimsetiRecommender();
        recommender.recommend(8, 5);
    }
}
