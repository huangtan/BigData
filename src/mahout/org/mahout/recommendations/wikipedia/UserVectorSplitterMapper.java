package org.mahout.recommendations.wikipedia;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.cf.taste.hadoop.item.VectorOrPrefWritable;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;
import java.util.Iterator;

/**
 * Download wikipedia related link data from here:
 * http://users.on.net/%7Ehenry/pagerank/links-simple-sorted.zip
 *
 * @author Krisztian_Horvath
 */
public class UserVectorSplitterMapper extends Mapper<VarLongWritable, VectorWritable, IntWritable, VectorOrPrefWritable> {

    @Override
    protected void map(VarLongWritable key, VectorWritable value, Context context) throws IOException, InterruptedException {
        long userID = key.get();
        Vector userVector = value.get();
        Iterator<Vector.Element> it = userVector.nonZeroes().iterator();
        IntWritable itemIndexWritable = new IntWritable();
        while (it.hasNext()) {
            Vector.Element e = it.next();
            int itemIndex = e.index();
            float preferenceValue = (float) e.get();
            itemIndexWritable.set(itemIndex);
            context.write(itemIndexWritable, new VectorOrPrefWritable(userID, preferenceValue));
        }
    }
}
