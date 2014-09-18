package org.hmahout.example;

/*M///////////////////////////////////////////////////////////////////////////////////////
 //
 //  IMPORTANT: READ BEFORE DOWNLOADING, COPYING, INSTALLING OR USING.
 //
 //  By downloading, copying, installing or using the software you agree to this license.
 //  If you do not agree to this license, do not download, install,
 //  copy or use the software.
 //
 //
 //                           License Agreement
 //                For de.apaxo.bedcon.FacebookRecommender Bean
 //
 // Copyright (C) 2012, Apaxo GmbH, all rights reserved.
 // Third party copyrights are property of their respective owners.
 //
 // Redistribution and use in source and binary forms, with or without modification,
 // are permitted provided that the following conditions are met:
 //
 //   * Redistribution's of source code must retain the above copyright notice,
 //     this list of conditions and the following disclaimer.
 //
 //   * Redistribution's in binary form must reproduce the above copyright notice,
 //     this list of conditions and the following disclaimer in the documentation
 //     and/or other materials provided with the distribution.
 //
 //   * The name of the copyright holders may not be used to endorse or promote products
 //     derived from this software without specific prior written permission.
 //
 // This software is provided by the copyright holders and contributors "as is" and
 // any express or implied warranties, including, but not limited to, the implied
 // warranties of merchantability and fitness for a particular purpose are disclaimed.
 // In no event shall the Apaxo GmbH or contributors be liable for any direct,
 // indirect, incidental, special, exemplary, or consequential damages
 // (including, but not limited to, procurement of substitute goods or services;
 // loss of use, data, or profits; or business interruption) however caused
 // and on any theory of liability, whether in contract, strict liability,
 // or tort (including negligence or otherwise) arising in any way out of
 // the use of this software, even if advised of the possibility of such damage.
 //
 //M*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVParser;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
 * 对于字段类型是String的推荐
 * 
 * @javax.ejb.ConcurrencyManagement(javax.ejb.ConcurrencyManagementType)
 * 
 * @author
 * 
 */

public class StringRecTest {

	/**
	 * Log class which is used for sophisticated error logging.
	 */
	private Logger log = Logger.getLogger(StringRecTest.class.getName());

	/**
	 * Recommender which will be hold by this session bean.
	 */
	private RecommenderBuilder builder = null;
	private static Recommender recommender = null;

	/**
	 * An MemoryIDMigrator which is able to create for every string a long
	 * representation. Further it can store the string which were put in and it
	 * is possible to do the mapping back.
	 */
	private static MemoryIDMigrator thing2long = new MemoryIDMigrator();

	/**
	 * The name of the file used for loading.
	 */
	// private static String DATA_FILE_NAME = "DemoFriendsLikes.csv";
	private static String DATA_FILE_NAME = "dest.csv";

	/**
	 * A data model which is needed for the recommender implementation. It
	 * provides a standardized interface for using the recommender. The data
	 * model can be become quite memory consuming. In our case it will be around
	 * 2 mb.
	 */
	private static DataModel dataModel;

	/**
	 * This function will init the recommender it will load the CSV file from
	 * the resource folder, parse it and create the necessary data structures to
	 * create a recommender. The
	 */
	@PostConstruct
	public void initRecommender() {

		try {
			// get the file which is part of the WAR as
			URL url = getClass().getClassLoader().getResource(DATA_FILE_NAME);

			// create a file out of the resource
			File data = new File(url.toURI());

			// create a map for saving the preferences (likes) for
			// a certain person
			Map<Long, List<Preference>> preferecesOfUsers = new HashMap<Long, List<Preference>>();

			// use a CSV parser for reading the file
			// use UTF-8 as character set
			CSVParser parser = new CSVParser(new InputStreamReader(
					new FileInputStream(data), "UTF-8"));// UTF-8

			// parse out the header
			// we are not using the header
			String[] header = parser.getLine();

			// should output person name
			// log.fine(header[0]+" "+header[1]);

			String[] line;

			// go through every line
			while ((line = parser.getLine()) != null) {

				String person = line[0];
				String likeName = line[1];
				String pre = line[2];

				// other lines contained but not used
				// String category = line[2];
				// String id = line[3];
				// String created_time = line[4];

				// create a long from the person name
				float preFloat = thing2long.toLongID(pre);

				long userLong = thing2long.toLongID(person);

				// store the mapping for the user
				thing2long.storeMapping(userLong, person);

				// create a long from the like name
				long itemLong = thing2long.toLongID(likeName);

				// store the mapping for the item
				thing2long.storeMapping(itemLong, likeName);

				List<Preference> userPrefList;

				// if we already have a userPrefList use it
				// otherwise create a new one.
				if ((userPrefList = preferecesOfUsers.get(userLong)) == null) {
					userPrefList = new ArrayList<Preference>();
					preferecesOfUsers.put(userLong, userPrefList);
				}
				// add the like that we just found to this user
				userPrefList.add(new GenericPreference(userLong, itemLong,
						preFloat));
				log.fine("Adding " + person + "(" + userLong + ") to "
						+ likeName + "(" + itemLong + ")");
			}

			// create the corresponding mahout data structure from the map
			FastByIDMap<PreferenceArray> preferecesOfUsersFastMap = new FastByIDMap<PreferenceArray>();
			for (Entry<Long, List<Preference>> entry : preferecesOfUsers
					.entrySet()) {
				preferecesOfUsersFastMap.put(entry.getKey(),
						new GenericUserPreferenceArray(entry.getValue()));
			}

			// create a data model
			dataModel = new GenericDataModel(preferecesOfUsersFastMap);

			// Instantiate the recommender
			recommender = new GenericItemBasedRecommender(dataModel,
					new LogLikelihoodSimilarity(dataModel));

			// recommender = new
			// GenericBooleanPrefItemBasedRecommender(dataModel, new
			// LogLikelihoodSimilarity(dataModel));
			// builder=new GenericItemBasedRecommender(dataModel, new
			// LogLikelihoodSimilarity(dataModel));
		} catch (URISyntaxException e) {
			log.log(Level.SEVERE, "Problem with the file URL", e);
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, DATA_FILE_NAME + " was not found", e);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Error during reading line of file", e);
		}
	}

	/**
	 * Returns up to 10 recommendations for a certain person as a string array.
	 * If less then 10 things are found the array will contain less elements. If
	 * no recommendations are found the array will contain 0 elements.
	 * 
	 * @param personName
	 *            The Facebook name of the person
	 * @return a string array with recommendations
	 * @throws TasteException
	 *             If anything goes wrong a TasteException is thrown
	 */
	public String[] recommendThings(String personName) throws TasteException {
		List<String> recommendations = new ArrayList<String>();
		try {
			List<RecommendedItem> items = recommender.recommend(
					thing2long.toLongID(personName), 10);
			for (RecommendedItem item : items) {
				recommendations.add(thing2long.toStringID(item.getItemID()));
			}
		} catch (TasteException e) {
			log.log(Level.SEVERE, "Error during retrieving recommendations", e);
			throw e;
		}
		return recommendations.toArray(new String[recommendations.size()]);
	}

	public static void main(String[] args) {

		StringRecTest facebookRecommender = new StringRecTest();
		StringBuffer buf = new StringBuffer();
		facebookRecommender.initRecommender();// Manuel Blechschmidt
		try {

			for (String rec : facebookRecommender.recommendThings("59b07m0")) {
				buf.append(rec + "\n");
			}
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(buf.toString());
		DataModel model = facebookRecommender.dataModel;
		// 评价推荐性能
		/*RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
		
		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
			@Override
			public Recommender buildRecommender(DataModel model)
					throws TasteException {
				// UserSimilarity similarity = new
				// PearsonCorrelationSimilarity(model);
				// UserNeighborhood neighborhood =
				// new NearestNUserNeighborhood(2, similarity, model);
				return new GenericItemBasedRecommender(model,
						new LogLikelihoodSimilarity(dataModel));
			}
		};
		// 使用方法是重载buildRecommender函数，函数里是构造推荐器的方法
		// 使用评估器，并设定评估期的参数
		// 2表示"precision and recall at 2"即相当于推荐top2，然后在top-2的推荐上计算准确率和召回率
		// 既然涉及到准确率和召回率，这里就有一个"hit"的定义，就是怎样的一个推荐算是good
		// 下面的参数设置是这样定义"good"的：利用阈值threshold = µ + σ
		// 即 user's average preference value µ plus one standard deviation σ
		// 如果一个推荐，它的真实分值是高于threshold的，那么它就是"good"
		IRStatistics stats = null;
		try {
			stats = evaluator
					.evaluate(
							recommenderBuilder,
							null,
							model,
							null,
							3,
							GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,
							1.0);
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 输出为0.75 1.0
		//打印出来准确率和召回率
		System.out.println(stats.getPrecision());
		System.out.println(stats.getRecall());
		*/
		
		//把推荐结果固话到文件系统上面  后面可以连数据库或者是存hdfs
		try {
			RecUntils.appendFile("F:\\ItemRE.txt", "用户\t\t推荐列表\r\n");

			// DataModel model = recommender.getDataModel();
			for (Iterator iter = model.getUserIDs(); iter.hasNext();) {
				Long str = (Long) iter.next();
				RecUntils.appendFile("F:\\ItemRE.txt",thing2long.toStringID(str)+ "\t\t");// 用户ID从1开始
				// System.out.println(thing2long.toStringID(str));
				for (String rec : facebookRecommender
						.recommendThings(thing2long.toStringID(str))) {
					RecUntils.appendFile("F:\\ItemRE.txt", rec + "  ");// 产品ID从1开始
				}
				RecUntils.appendFile("F:\\ItemRE.txt", "\r\n");
			}
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
