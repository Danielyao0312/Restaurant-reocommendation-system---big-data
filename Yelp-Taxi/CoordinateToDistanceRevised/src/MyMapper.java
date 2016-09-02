
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.net.URI;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;


public class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private static final double EARTH_RADIUS = 6378137;
     
    private ArrayList<ArrayList<String>> yelp = new ArrayList<ArrayList<String>>();

    @Override
    protected void setup(Context context) throws IOException,InterruptedException {
        String input = null;
        BufferedReader in = null;
        try {
           
          File file = new File("distinct_yelp.csv");
          FileInputStream fis = new FileInputStream(file);
          in = new BufferedReader(new InputStreamReader(fis));

          while (null != (input = in.readLine())) {
              String[] restauranta = input.split("\t");

              ArrayList<String> line = new ArrayList<String>();
              line.add(restauranta[0]);
              line.add(restauranta[5]);
              line.add(restauranta[4]);
              yelp.add(line);
          }   
             
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           try {
               if (in != null) {
                   in.close();
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }
     
     
     
     public void map(LongWritable key, Text value, Context context)
             throws IOException, InterruptedException {
       
      String taxi = value.toString();
      String[] taxiCoordinate = taxi.split("\t");
      if(taxiCoordinate.length == 2){
        double taxi_log = Double.parseDouble(taxiCoordinate[0]);
        double taxi_lat = Double.parseDouble(taxiCoordinate[1]);
        
           for(ArrayList<String> row : yelp){
            double res_log = Double.parseDouble(row.get(1));
            double res_lat = Double.parseDouble(row.get(2));
  
            if (LantitudeLongitudeDist(taxi_log, taxi_lat, res_log, res_lat) <= 500) 
              context.write(new Text(row.get(0)), new IntWritable(1));
           }
      }
     }
     
      private static double rad(double d)  {  
         return d * Math.PI / 180.0;  
      }  

      public static double LantitudeLongitudeDist(double lon1, double lat1,double lon2, double lat2) {  
        double radLat1 = rad(lat1);  
        double radLat2 = rad(lat2);  
         
        double radLon1 = rad(lon1);  
        double radLon2 = rad(lon2);  
         
        if (radLat1 < 0)  
        radLat1 = Math.PI / 2 + Math.abs(radLat1);// south  
        if (radLat1 > 0)  
        radLat1 = Math.PI / 2 - Math.abs(radLat1);// north  
        if (radLon1 < 0)  
        radLon1 = Math.PI * 2 - Math.abs(radLon1);// west  
        if (radLat2 < 0)  
        radLat2 = Math.PI / 2 + Math.abs(radLat2);// south  
        if (radLat2 > 0)  
        radLat2 = Math.PI / 2 - Math.abs(radLat2);// north  
        if (radLon2 < 0)  
        radLon2 = Math.PI * 2 - Math.abs(radLon2);// west  

        double x1 = EARTH_RADIUS  * Math.cos(radLon1) * Math.sin(radLat1);  
        double y1 = EARTH_RADIUS * Math.sin(radLon1) * Math.sin(radLat1);  
        double z1 = EARTH_RADIUS * Math.cos(radLat1);  
        
        double x2 = EARTH_RADIUS * Math.cos(radLon2) * Math.sin(radLat2);  
        double y2 = EARTH_RADIUS * Math.sin(radLon2) * Math.sin(radLat2);  
        double z2 = EARTH_RADIUS * Math.cos(radLat2);  
     
        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)+ (z1 - z2) * (z1 - z2));  
       
        double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS));  
        double dist = theta * EARTH_RADIUS;  
        return dist;  
    }
      
      
}
