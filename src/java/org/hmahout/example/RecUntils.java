package org.hmahout.example;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import java.io.FileWriter;

public class RecUntils {
    String URL="F:\\ItemRE.txt";
	private static MemoryIDMigrator thing2long;
	StringBuffer buf = new StringBuffer();
//把推荐结果写入文件
	public static void writetxt(Recommender recommender) throws TasteException {
		appendFile("F:\\ItemRE.txt", "用户\t\t推荐列表\r\n");

		DataModel model = recommender.getDataModel();
		for (Iterator iter = model.getUserIDs(); iter.hasNext();) {
			Long str = (Long) iter.next();
			appendFile("F:\\ItemRE.txt", str + "\t\t");// 用户ID从1开始
			System.out.println(thing2long.toStringID(str));
			for (String rec : recommendThings(thing2long.toStringID(str), recommender)) {
				appendFile("F:\\ItemRE.txt", rec + "  ");// 产品ID从1开始
			}
		}
	}

	public static String[] recommendThings(String personName, Recommender recommender)
			throws TasteException {
		List<String> recommendations = new ArrayList<String>();
		try {
			List<RecommendedItem> items = recommender.recommend(
					thing2long.toLongID(personName), 2);
			for (RecommendedItem item : items) {
				recommendations.add(thing2long.toStringID(item.getItemID()));
			}
		} catch (TasteException e) {

			throw e;
		}
		return recommendations.toArray(new String[recommendations.size()]);
	}

	public static void appendFile(String fileName, String content) {
		try {
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
