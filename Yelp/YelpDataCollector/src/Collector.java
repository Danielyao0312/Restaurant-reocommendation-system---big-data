import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.*;

/**
 * Code sample for accessing the Yelp API V2.
 * 
 * This program demonstrates the capability of the Yelp API version 2.0 by using the Search API to
 * query for businesses by a search term and location, and the Business API to query additional
 * information about the top result from the search query.
 * 
 * <p>
 * See <a href="http://www.yelp.com/developers/documentation">Yelp Documentation</a> for more info.
 * 
 */

public class Collector {

  private static final String API_HOST = "api.yelp.com";
  private static final String DEFAULT_TERM = "restaurants";
  private static final String DEFAULT_LOCATION = "40.709272, -73.995409 | 40.701984, -74.016738"; 
  //private static final double SW_LATITUDE = 40.872442;
  //private static final double SW_LONGITUDE = -73.910007;
  //private static final double NE_LATITUDE = 40.839593;
  //private static final double NE_LONGITUDE = -73.94949;
  //private static final double LATITUDE_INCREMENT = 0.00065;
  //private static final double LONGITUDE_INCREMENT = 0.0032;
  private static final String SEARCH_PATH = "/v2/search";
  //private static final String BUSINESS_PATH = "/v2/business";
  //private static final int SEARCH_LIMIT = 20;
  /*
   * Update OAuth credentials below from the Yelp Developers API site:
   * http://www.yelp.com/developers/getting_started/api_access
   */
  private static final String CONSUMER_KEY = "YtA6KPtzy8aEFdKh3m5_Iw";
  private static final String CONSUMER_SECRET = "D0Wt5ztGXmH17UyDnMH5ZUpmbmE";
  private static final String TOKEN = "OsczPrybxucvgj0A7BJp43k6lDlhJI-z";
  private static final String TOKEN_SECRET = "ynkXeWJnAqEtJGR1iBv0BXQ_2Dg";

  OAuthService service;
  Token accessToken;

  /**
   * Setup the Yelp API OAuth credentials.
   * 
   * @param consumerKey Consumer key
   * @param consumerSecret Consumer secret
   * @param token Token
   * @param tokenSecret Token secret
   */
  public Collector(String consumerKey, String consumerSecret, String token, String tokenSecret) {
    this.service =
        new ServiceBuilder().provider(OAuth.class).apiKey(consumerKey)
            .apiSecret(consumerSecret).build();
    this.accessToken = new Token(token, tokenSecret);
  }

  /**
   * Creates and sends a request to the Search API by term and location.
   * <p>
   * See <a href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp Search API V2</a>
   * for more info.
   * 
   * @param term <tt>String</tt> of the search term to be queried
   * @param location <tt>String</tt> of the location
   * @return <tt>String</tt> JSON Response
   */
  public String searchForBusinessesByLocation(String term, String location, String offset) {
    OAuthRequest request = createOAuthRequest(SEARCH_PATH);
    request.addQuerystringParameter("term", term);
    request.addQuerystringParameter("bounds", location);
    /**
     *add s parameter "offset" so each time we can get a new chunk of the result; By Marvin
     *
     *
     */
    request.addQuerystringParameter("offset", offset);     
    //request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
    return sendRequestAndGetResponse(request);
  }

  /**
   * Creates and sends a request to the Business API by business ID.
   * <p>
   * See <a href="http://www.yelp.com/developers/documentation/v2/business">Yelp Business API V2</a>
   * for more info.
   * 
   * @param businessID <tt>String</tt> business ID of the requested business
   * @return <tt>String</tt> JSON Response
   */
  //public String searchByBusinessId(String businessID) {
    //OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
    //return sendRequestAndGetResponse(request);
  //}

  /**
   * Creates and returns an {@link OAuthRequest} based on the API endpoint specified.
   * 
   * @param path API endpoint to be queried
   * @return <tt>OAuthRequest</tt>
   */
  private OAuthRequest createOAuthRequest(String path) {
    OAuthRequest request = new OAuthRequest(Verb.GET, "https://" + API_HOST + path);
    return request;
  }

  /**
   * Sends an {@link OAuthRequest} and returns the {@link Response} body.
   * 
   * @param request {@link OAuthRequest} corresponding to the API request
   * @return <tt>String</tt> body of API response
   */
  private String sendRequestAndGetResponse(OAuthRequest request) {
    System.out.println("Querying " + request.getCompleteUrl() + " ...");
    this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    return response.getBody();
  }

  /**
   * Queries the Search API based on the command line arguments and takes the first result to query
   * the Business API.
   * 
   * @param yelpApi <tt>YelpAPI</tt> service instance
   * @param yelpApiCli <tt>YelpAPICLI</tt> command line arguments
   */
  private static void queryAPI(Collector yelpApi, YelpAPICLI yelpApiCli) {
    String searchResponseJSON;
    JSONParser parser = new JSONParser();
    JSONObject response = null;
    JSONArray businesses = null;
    JSONObject business = null;
    JSONObject location = null;
    JSONObject coordinate = null;
    int numberOfResultGet;
    FileWriter out = null;
    
    try {
		out = new FileWriter("yelp.csv", true);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    
    do{
	    searchResponseJSON = yelpApi.searchForBusinessesByLocation(yelpApiCli.term, yelpApiCli.location, yelpApiCli.offset);
	    try {
	      response = (JSONObject) parser.parse(searchResponseJSON);
	    } catch (ParseException pe) {
	      System.out.println("Error: could not parse JSON response:");
	      System.out.println(searchResponseJSON);
	      System.exit(1);
	    }
	    
	    businesses = (JSONArray) response.get("businesses");
	    numberOfResultGet = businesses.size();
	    for(int index = 0; index < businesses.size(); index++){
	    	business = (JSONObject)businesses.get(index);
	    	System.out.print(business.get("id") + "\t");
	    	System.out.print(business.get("name") + "\t");
	    	System.out.print(business.get("review_count") + "\t");
	    	System.out.print(business.get("rating") + "\t");
	    	location = (JSONObject)business.get("location");
	    	 
	    	try {
				out.write(business.get("id") + "\t");
				out.write(business.get("name") + "\t");
				out.write(business.get("review_count") + "\t");
				out.write(business.get("rating") + "\t");
		    	location = (JSONObject)business.get("location");
		    	coordinate = (JSONObject) location.get("coordinate");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	
	    	try {
	    		if(location.containsKey("coordinate")){
	    	    	coordinate = (JSONObject) location.get("coordinate");
	    	    	System.out.print(coordinate.get("latitude") + "\t");
	    	    	System.out.println(coordinate.get("longitude"));
	    	    	out.write(coordinate.get("latitude") + "\t");
			    	out.write(coordinate.get("longitude") + "\n");
	    		}
	    		else{
	    			System.out.print("0" + "\t");
	    	    	System.out.println("0" + "\t");
	    			out.write("0" + "\t");
			    	out.write("0" + "\n");
	    		}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    /**
	     * update offset to get next 20 result
	     * by Marvin
	     */
	    
	    int lastOffset = Integer.parseInt(yelpApiCli.offset);          
	    if(lastOffset + numberOfResultGet < 1000)
	    	yelpApiCli.offset = Integer.toString(lastOffset + numberOfResultGet);
	    else 
	    	break;
    }while(numberOfResultGet == 20);
    
    try {
		out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    //System.out.println(businesses.);
    //JSONObject firstBusiness = (JSONObject) businesses.get(0);
    
    //System.out.println(String.format(
    //    "%s businesses found, querying business info for the top result \"%s\" ...",
    //   businesses.size(), firstBusinessID));
    // Select the first business and display business details
    //String businessResponseJSON = yelpApi.searchByBusinessId(firstBusinessID.toString());
    //System.out.println(String.format("Result for business \"%s\" found:", firstBusinessID));
    //System.out.println(businessResponseJSON);
  }

  /**
   * Command-line interface for the sample Yelp API runner.
   */
  private static class YelpAPICLI {
    //@Parameter(names = {"-q", "--term"}, description = "Search Query Term")
    public String term = DEFAULT_TERM;

    //@Parameter(names = {"-l", "--location"}, description = "Location to be Queried")
    public String location = DEFAULT_LOCATION;//SW_LATITUDE + "," + SW_LONGITUDE + "|" + NE_LATITUDE + "," + NE_LONGITUDE;
    public String offset = "0";
    /**
     * offset start from 0; by Marvin
     */
  }

  /**
   * Main entry for sample Yelp API requests.
   * <p>
   * After entering your OAuth credentials, execute <tt><b>run.sh</b></tt> to run this example.
   */
  public static void main(String[] args) {
    YelpAPICLI yelpApiCli = new YelpAPICLI();
    new JCommander(yelpApiCli, args);

    Collector yelpApi = new Collector(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
    queryAPI(yelpApi, yelpApiCli);
  }
}