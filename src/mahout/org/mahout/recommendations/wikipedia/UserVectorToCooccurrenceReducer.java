package org.mahout.recommendations.wikipedia;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;

/**
 * Download wikipedia related link data from here:
 * http://users.on.net/%7Ehenry/pagerank/links-simple-sorted.zip
 *
 * @author Krisztian_Horvath
 */
public class UserVectorToCooccurrenceReducer extends Reducer<IntWritable, IntWritable, IntWritable, VectorWritable> {

    @Override
    protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        Vector cooccurrenceRow = new RandomAccessSparseVector(Integer.MAX_VALUE, 100);
        for (IntWritable intWritable : values) {
            int itemIndex2 = intWritable.get();
            cooccurrenceRow.set(itemIndex2, cooccurrenceRow.get(itemIndex2) + 1.0);
        }
        context.write(key, new VectorWritable(cooccurrenceRow));
    }
}
