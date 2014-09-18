package org.mahout.recommendations.wikipedia;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;

/**
 * Download wikipedia related link data from here:
 * http://users.on.net/%7Ehenry/pagerank/links-simple-sorted.zip
 *
 * @author Krisztian_Horvath
 */
public class WikipediaToUserVectorReducer extends Reducer<VarLongWritable, VarLongWritable, VarLongWritable, VectorWritable> {

    @Override
    protected void reduce(VarLongWritable key, Iterable<VarLongWritable> values, Context context) throws IOException, InterruptedException {
        Vector userVector = new RandomAccessSparseVector(Integer.MAX_VALUE, 100);
        for (VarLongWritable itemPref : values) {
            userVector.set((int) itemPref.get(), 1.0f);
        }
        context.write(key, new VectorWritable(userVector));
    }
}
