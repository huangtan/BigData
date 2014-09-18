package org.mahout.recommendations.wikipedia;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.cf.taste.hadoop.item.VectorOrPrefWritable;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;

/**
 * Download wikipedia related link data from here:
 * http://users.on.net/%7Ehenry/pagerank/links-simple-sorted.zip
 *
 * @author Krisztian_Horvath
 */
public class CooccurrenceColumnWrapperMapper extends Mapper<IntWritable, VectorWritable, IntWritable, VectorOrPrefWritable> {

    @Override
    protected void map(IntWritable key, VectorWritable value, Context context) throws IOException, InterruptedException {
        context.write(key, new VectorOrPrefWritable(value.get()));
    }
}
