package Broadband;

import APIServer.FileDataSource;
import Parser.CustomException;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** Class that handles the Broadband endpoint, organizing and sending responses based on inputs. */
public class BroadbandHandler implements Route {
  // Map to store responses
  private Map<String, Object> responseMap;
  // Broadband utilities instance to handle conversion to a Broadband
  private BroadbandUtilities myManager;
  //An interface that APIDataSource and CachingAPIDataSource both implement
  private FileDataSource dataSource;

  /**
   * Takes in a BroadbandUtilities class in order to perform necessary conversions from JSON to a
   * usable object in Java.
   */
  public BroadbandHandler(BroadbandUtilities myManager, FileDataSource dataSource) {
    this.myManager = myManager;
    this.responseMap = new HashMap<>();
    this.dataSource = dataSource;

  }

  /**
   * @param request this allows the program to access the queryParams
   * @param response allows the server to process responses
   *
   * @throws
   *
   * @return a response map converted to a JSON indicating if a call is successful
   *     <p>This method allows one to make a broadband request, providing a state and county, and
   *     receive a response with the state, county, broadband percentage, and time that ACS was
   *     accessed (which we hardcode here as we are not actually connected to the API).
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    // sets up the parameters
    String state = request.queryParams("state");
    String county = request.queryParams("county");

    // Eventually sendRequest will call to the API
    System.out.println("this broadband creation gets here");
    try {
      myManager.createBroadband(this.sendRequest(state, county));
    }
    catch (IOException e) {
      this.responseMap.put("result", "error_bad_request " + e.getMessage());
      this.responseMap.put("state", state);
      this.responseMap.put("county", county);
      this.responseMap.put("output", "unable to get percentage for non-existent place");
      this.responseMap.put("dateTime", dataSource.getTime());
      return toJson(this.responseMap);
    }
    //List<Broadband> broadband = myManager.createBroadband(sendMockRequest(state, county));

    // tries to search for the percentage by calling to the manager
    // if successful, populates a response map with successful result and all return values
    try {
      String foundPercentage = myManager.getPercentage();
      this.responseMap.put("result", "success");
      this.responseMap.put("state", state);
      this.responseMap.put("county", county);
      this.responseMap.put("percentage", myManager.getPercentage());
      this.responseMap.put("dateTime", dataSource.getTime());
      return toJson(this.responseMap);
    }
    // If not successful, returns an error but still populates map with necessary fields
    catch (CustomException e) {
      this.responseMap.put("result", "Error " + e.getMessage());
      this.responseMap.put("state", state);
      this.responseMap.put("county", county);
      this.responseMap.put("percentage", "Error: can't get percentage due to error");
      this.responseMap.put("dateTime", dataSource.getTime());
      return toJson(this.responseMap);
    }
  }

  /**
   * @param map the response map from string (parameter) to object (value)
   * @return a properly formatted JSON file of that map
   *     <p>Converts responseMap to a properly formatted JSON return.
   */
  private String toJson(Map<String, Object> map) {
    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(type);
    return adapter.toJson(map);
  }

  /**
   * @return a String of the detected JSON
   * @throws FileNotFoundException
   *     <p>This will eventually send a request to an API. For now, it reads our mocked JSON file
   *     using a fileReader and returns it as a string.
   */
  private String sendRequest(String stateCode, String countyCode) throws FileNotFoundException,
      IOException, URISyntaxException, InterruptedException {

    String response;
    List<String> stateAndCounty = dataSource.getStateAndCounty(stateCode, countyCode);
    response = dataSource.getResponse(stateAndCounty.get(0), stateAndCounty.get(1),
        "/variables?get=NAME,S2802_C03_022E");
    return response;
  }
}
