package org.hmahout.kpi.app;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.hmahout.kpi.entity.Kpi;
import org.hmahout.kpi.util.KpiUtil;

public class KpiIpApp {

	 public static class KPIIPMapper extends MapReduceBase implements Mapper<Object, Text, Text, Text> {
	        private Text word = new Text();
	        private Text ips = new Text();

	        @Override
	        public void map(Object key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
	            Kpi kpi = KpiUtil.transformLineKpi(value.toString());
	            if (KpiUtil.isValid(kpi)) {
	                word.set(kpi.getRequest());
	                ips.set(kpi.getRemote_addr());
	                output.collect(word, ips);
	            }
	        }
	    }

	    public static class KPIIPReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
	        private Text result = new Text();
	        private Set<String> count = new HashSet<String>();

	        @Override
	        public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
	            while (values.hasNext()) {
	                count.add(values.next().toString());
	            }
	            result.set(String.valueOf(count.size()));
	            output.collect(key, result);
	        }
	    }

	    public static void main(String[] args) throws Exception {
	        String input = "hdfs://127.0.0.1:9000/user/tianbx/log_kpi/input";
	        String output ="hdfs://127.0.0.1:9000/user/tianbx/log_kpi/ip";
	        JobConf conf = new JobConf(KpiIpApp.class);
	        conf.setJobName("KPIIP");
	        String url = "classpath:";
//	        url = "/home/tianbx/workspace/hmahout/src/resources";
	        conf.addResource(url+"/hadoop/core-site.xml");
	        conf.addResource(url+"/hadoop/hdfs-site.xml");
	        conf.addResource(url+"/hadoop/mapred-site.xml");
	        
	        conf.setMapOutputKeyClass(Text.class);
	        conf.setMapOutputValueClass(Text.class);
	        
	        conf.setOutputKeyClass(Text.class);
	        conf.setOutputValueClass(Text.class);
	        
	        conf.setMapperClass(KPIIPMapper.class);
	        conf.setCombinerClass(KPIIPReducer.class);
	        conf.setReducerClass(KPIIPReducer.class);

	        conf.setInputFormat(TextInputFormat.class);
	        conf.setOutputFormat(TextOutputFormat.class);

	        FileInputFormat.setInputPaths(conf, new Path(input));
	        FileOutputFormat.setOutputPath(conf, new Path(output));

	        JobClient.runJob(conf);
	        System.exit(0);
	    }
	    

}
