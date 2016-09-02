import java.io.IOException;

import org.apache.hadoop.io.IntWritable; 
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	private static final String word1 = "Marcha Cocina";
	private static final String word2 = "shake shack";
	private static final String word3 = "Ippudo NY";
	private static final String word4 = "Java";
	
	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		String tmp = new String(line);
		int index;
		
		String[] words = new String[16];
		String[] regex = new String[30];
		regex[0] = "(.*[i|I]ppudo(\\s*NY)*.*)";
		regex[1] = "(.*[h|H]alal(\\s*[g|G]uys)*.*)";
		regex[2] = "(.*[l|L]ombardis(\\s*pizza)*.*)";
		regex[3] = "(.*halal(\\sguys)*.*)";
		regex[4] = "(.*[t|T]otto(\\s*[r|R]amen)*.*)";
		regex[5] = "(.*[shake|SHAKE]{5}\\s*[shack|SHACK]{5}.*)";
		regex[6] = "(.*[s|S]erendipity.*)";
		regex[7] = "(.*(Cafe)*\\s*[h|H]abana.*)";
		regex[8] = "(.*Cafeteria.*)";
		regex[9] = "(.*[m|M]orimoto.*)";
		regex[10] = "(.*[c|C]aracas\\s*[a|A]repa.*)";
		regex[11] = "(.*[j|J]unior's\\s*[r|R]estaurant.*)";
		regex[12] = "(.*[d|D]inosaur\\s*Bar-B-Que.*)";
		regex[13] = "(.*halal(\\sguys)*.*.*)";
		regex[14] = "(.*[b|B]urger\\s*joint.*)";
		regex[15] = "(.*[c|C]armine's.*)";
		regex[16] = "(^(?=.*\bmamouns\b)(?=.*\bfalafel\b).*$)";
		
		for (int i = 0; i < regex.length; i++){
			Pattern p = Pattern.compile(regex[i]);
	        Matcher m = p.matcher(line);
	        
	        if (m.find()) {
	        	context.write(new Text(words[i]), new IntWritable(1));          
	        }
	        else{
	        	context.write(new Text(words[i]), new IntWritable(0));          
	        }
		}
		
		/*index = tmp.indexOf(word1);
		if (index == -1)
			context.write(new Text(word1), new IntWritable(0));
		while(index != -1){
			context.write(new Text(word1), new IntWritable(1));
			tmp = tmp.substring(index+word1.length());
			index = tmp.indexOf(word1);
		}
		
		tmp = new String(line);
		tmp = tmp.toLowerCase();
		index = tmp.indexOf(word2);
		if (index == -1)
			context.write(new Text(word2), new IntWritable(0));
		while(index != -1){
			context.write(new Text(word2), new IntWritable(1));
			tmp = tmp.substring(index+word2.length());
			index = tmp.indexOf(word2);
		}
		
		tmp = new String(line);
		index = tmp.indexOf(word3);
		if (index == -1)
			context.write(new Text(word3), new IntWritable(0));
		while(index != -1){
			context.write(new Text(word3), new IntWritable(1));
			tmp = tmp.substring(index+word3.length());
			index = tmp.indexOf(word3);
		}
		
		tmp = new String(line);
		index = tmp.indexOf(word4);
		if (index == -1)
			context.write(new Text(word4), new IntWritable(0));
		while(index != -1){
			context.write(new Text(word4), new IntWritable(1));
			tmp = tmp.substring(index+word4.length());
			index = tmp.indexOf(word4);
		}*/
	}
}