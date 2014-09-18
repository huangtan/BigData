package org.mahout.recommendations.wikipedia;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
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
public class UserVectorToCooccurrenceMapper extends Mapper<VarLongWritable, VectorWritable, IntWritable, IntWritable> {

    @Override
    protected void map(VarLongWritable key, VectorWritable value, Context context) throws IOException, InterruptedException {
        Iterator<Vector.Element> it = value.get().nonZeroes().iterator();
        while (it.hasNext()) {
            int index1 = it.next().index();
            Iterator<Vector.Element> it2 = value.get().nonZeroes().iterator();
            while (it2.hasNext()) {
                int index2 = it2.next().index();
                context.write(new IntWritable(index1), new IntWritable(index2));
            }
        }
    }
}