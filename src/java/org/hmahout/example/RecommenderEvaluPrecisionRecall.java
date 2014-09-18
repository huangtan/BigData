package org.hmahout.example;


	import org.apache.mahout.cf.taste.impl.model.file.*;
	import org.apache.mahout.cf.taste.impl.neighborhood.*;
	import org.apache.mahout.cf.taste.impl.recommender.*;
	import org.apache.mahout.cf.taste.impl.similarity.*;
	import org.apache.mahout.cf.taste.model.*;
	import org.apache.mahout.cf.taste.neighborhood.*;
	import org.apache.mahout.cf.taste.recommender.*;
	import org.apache.mahout.cf.taste.similarity.*;
	import java.io.*;
	import org.apache.mahout.cf.taste.common.TasteException;
	import org.apache.mahout.cf.taste.eval.IRStatistics;
	import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
	import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
	import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
	import org.apache.mahout.common.RandomUtils;

	/**
	 *
	 * @author 
	 */
	public class RecommenderEvaluPrecisionRecall {
	    public static void main(String[] args) throws IOException, TasteException {
	        RandomUtils.useTestSeed();
	        DataModel model = new FileDataModel(new File("intro.csv"));
	        //=导入org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;=
	        //构建评估器
	        RecommenderIRStatsEvaluator evaluator =
	                new GenericRecommenderIRStatsEvaluator();
	        
	        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
	            @Override
	            public Recommender buildRecommender(DataModel model)
	                    throws TasteException {
	                UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
	                UserNeighborhood neighborhood =
	                        new NearestNUserNeighborhood(2, similarity, model);
	                return new GenericUserBasedRecommender(model, neighborhood, similarity);
	            }
	        };
	        //使用评估器，并设定评估期的参数
	        //2表示"precision and recall at 2"即相当于推荐top2，然后在top-2的推荐上计算准确率和召回率
	        //既然涉及到准确率和召回率，这里就有一个"hit"的定义，就是怎样的一个推荐算是good
	        //下面的参数设置是这样定义"good"的：利用阈值threshold = µ + σ 
	        //即 user's average preference value µ plus one standard deviation σ
	        //如果一个推荐，它的真实分值是高于threshold的，那么它就是"good"
	        IRStatistics stats = evaluator.evaluate(
	                recommenderBuilder, null, model, null, 2,
	                GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,
	                1.0);
	        //输出为0.75 1.0
	        System.out.println(stats.getPrecision());
	        System.out.println(stats.getRecall());
	    }
	}

