package org.mahout.recommendations.wikipedia;

import org.apache.hadoop.mapreduce.Reducer;
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
public class AggregateCombiner extends Reducer<VarLongWritable, VectorWritable, VarLongWritable, VectorWritable> {

    @Override
    protected void reduce(VarLongWritable key, Iterable<VectorWritable> values, Context context) throws IOException, InterruptedException {
        Vector partial = null;
        for (VectorWritable vectorWritable : values) {
            partial = partial == null ? vectorWritable.get() : partial.plus(vectorWritable.get());
        }
        context.write(key, new VectorWritable(partial));
    }
}
