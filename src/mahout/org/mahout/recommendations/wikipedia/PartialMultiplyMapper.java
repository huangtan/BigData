package org.mahout.recommendations.wikipedia;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.cf.taste.hadoop.item.VectorAndPrefsWritable;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;
import java.util.List;

/**
 * Download wikipedia related link data from here:
 * http://users.on.net/%7Ehenry/pagerank/links-simple-sorted.zip
 *
 * @author Krisztian_Horvath
 */
public class PartialMultiplyMapper extends Mapper<IntWritable, VectorAndPrefsWritable, VarLongWritable, VectorWritable> {

    @Override
    protected void map(IntWritable key, VectorAndPrefsWritable value, Context context) throws IOException, InterruptedException {
        Vector cooccurrenceColumn = value.getVector();
        List<Long> userIDs = value.getUserIDs();
        List<Float> prefValues = value.getValues();
        for (int i = 0; i < userIDs.size(); i++) {
            long userID = userIDs.get(i);
            float prefValue = prefValues.get(i);
            Vector partialProduct = cooccurrenceColumn.times(prefValue);
            context.write(new VarLongWritable(userID), new VectorWritable(partialProduct));
        }
    }
}
