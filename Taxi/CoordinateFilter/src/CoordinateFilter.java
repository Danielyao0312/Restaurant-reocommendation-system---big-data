import java.io.*;
import java.util.Scanner;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class CoordinateFilter {
	public static void main(String[] args) throws ParseException, IOException
	{
		 String fullFileName = "nta.json";
		 JSONParser parser = new JSONParser();
		 JSONObject response = null;
		 JSONArray features = null;
		 JSONObject feature = null;
		 JSONObject properties = null;
		 JSONObject geometry = null;
		 JSONArray coordinate = null;
		 String BoroName = null;
		 
		 FileWriter out = new FileWriter("manhattan.json", true);
		 out.write("{ coordinate : [");
	        File file = new File(fullFileName);
	        Scanner scanner = null;
	        StringBuilder buffer = new StringBuilder();
	        scanner = new Scanner(file, "utf-8");
	        while (scanner.hasNextLine()) 
                buffer.append(scanner.nextLine());
	        scanner.close();
	        String json = buffer.toString();
	        //System.out.println(json);
	        response = (JSONObject) parser.parse(json);
	        features = (JSONArray) response.get("features");
	        //System.out.println(features.toString());
	        
	        for(int index = 0; index < features.size(); index++)
	        {
	        	feature = (JSONObject) features.get(index);
	        	properties = (JSONObject) feature.get("properties");
	        	BoroName = (String) properties.get("BoroName");
	        	
	        	if(BoroName.equals("Manhattan"))
	        	{
	        		//System.out.println(BoroName);
	        		geometry = (JSONObject) feature.get("geometry");
	        		coordinate = (JSONArray) geometry.get("coordinates");
	        		out.write(coordinate.get(0).toString());
	        		out.write(",\n");
	        		//System.out.println(coordinate.toString());
	        	}
	        }
	        System.out.println("Done");
	        out.write("] }");
	        out.close();
	        
	}

}
