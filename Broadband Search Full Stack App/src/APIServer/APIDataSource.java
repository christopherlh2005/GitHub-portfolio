package APIServer;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a class that communicates directly with the US Census API, making critical requests
 * to get data and converting state and county names to codes.
 */
public class APIDataSource implements FileDataSource {

  //time of API call
  private String time;

  /**
   * Sends a request to the US Census API
   *
   * @param stateCode the code of a state
   * @param countyCode the code of a county
   * @param variableSpecifier the portion of the URL for the specific data you want from the census
   *
   * @return a string version of the json for a county
   *
   * @throws URISyntaxException if URI has bad syntax
   * @throws IOException any input mistakes or mistakes with state/county
   * @throws InterruptedException in case of interruption
   */
  @Override
  public String getResponse(String stateCode, String countyCode, String variableSpecifier)
      throws URISyntaxException, IOException, InterruptedException {

    // Correct API endpoint
    String url = "https://api.census.gov/data/2021/acs/acs1/subject"
        + variableSpecifier
        + "&for=county:" + countyCode
        + "&in=state:" + stateCode;

    //builds http request
    HttpRequest buildRequest = HttpRequest.newBuilder()
        .uri(URI.create(url)) // Properly form the URI
        .GET()
        .build();

    // Send the API request
    HttpResponse<String> sentResponse = HttpClient.newBuilder()
        .build()
        .send(buildRequest, HttpResponse.BodyHandlers.ofString());

    System.out.println("raw census API data: " + sentResponse.body());

    //convert this result to a json usable for our program
    String convertedAPIResult = this.convertToJson(sentResponse.body());

    System.out.println("converted census API data: " + convertedAPIResult);


    //get time
    LocalDateTime myTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    this.time = myTime.format(formatter);

    return convertedAPIResult;
  }

  /**
   * Converts state and county names to codes
   *
   * @param stateName the name of a state
   * @param countyName the name of a county
   *
   * @return a list consisting of the state and county code
   *
   * @throws URISyntaxException if URI has bad syntax
   * @throws IOException any input mistakes or mistakes with state/county
   * @throws InterruptedException in case of interruption
   */
  @Override
  public List<String> getStateAndCounty(String stateName, String countyName) throws
      URISyntaxException, IOException, InterruptedException {

    List<String> listToReturn = new ArrayList<>();

    // Correct API endpoint
    String url = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*";

    HttpRequest buildRequest = HttpRequest.newBuilder()
        .uri(URI.create(url)) // Properly form the URI
        .GET()
        .build();

    // Send the API request
    HttpResponse<String> sentResponse = HttpClient.newBuilder()
        .build()
        .send(buildRequest, HttpResponse.BodyHandlers.ofString());

    // Parse the JSON string into a nested List<List<String>>
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<List<List<String>>> jsonAdapter =
        moshi.adapter(Types.newParameterizedType(List.class, List.class, String.class));

    //adapter to convert json
    List<List<String>> allStateData = jsonAdapter.fromJson(sentResponse.body());

    //adds state code in the case of a match
    for (List<String> stateTuple : allStateData) {
      if (stateTuple.get(0).equals(stateName)) {
        listToReturn.add(stateTuple.get(1));
      }
    }

    //throws an error if the state doesn't exist
    if (listToReturn.isEmpty()) {
      throw new IOException("State does not exist.");
    }

    //helper that gets the county and adds to the return list
    String countyCode = this.getCountyCodeHelper(stateName, countyName, listToReturn.get(0));
    listToReturn.add(countyCode);

   // return ;
    return listToReturn;
  }

  /**
   * Helper method that converts the county name into a code
   *
   * @param stateName the name of a state
   * @param countyName the name of a county
   * @param stateCode the code of a state
   *
   * @return the county code
   *
   * @throws IOException any input mistakes or mistakes with state/county
   * @throws InterruptedException in case of interruption
   */
  public String getCountyCodeHelper(String stateName, String countyName,
      String stateCode) throws IOException, InterruptedException {

    //URL for all counties in a state
    String newURL = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:"
        + stateCode;

    // Properly form the URI
    HttpRequest newBuildRequest = HttpRequest.newBuilder()
        .uri(URI.create(newURL))
        .GET()
        .build();

    // Send the API request
    HttpResponse<String> newSentResponse = HttpClient.newBuilder()
        .build()
        .send(newBuildRequest, HttpResponse.BodyHandlers.ofString());

    // Parse the JSON string into a nested List
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<List<List<String>>> jsonAdapter =
        moshi.adapter(Types.newParameterizedType(List.class, List.class, String.class));

    //convert the list into a working json
    List<List<String>> allCountyData = jsonAdapter.fromJson(newSentResponse.body());

    //search for a county and returns the value
    String searchingTerm = countyName + ", " + stateName;

    for (List<String> countyEntry : allCountyData) {
      if (countyEntry.get(0).equals(searchingTerm)) {
        return countyEntry.get(2);
      }
    }

    //throw error if no county found
   throw new IOException("County does not exist");

  }

  /**
   * method that converts the census API output to usable JSONs
   *
   * @param rawData string version of the census API output
   *
   * @return the string output json
   */
  public static String convertToJson(String rawData) {
    // Initialize Moshi
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<List<List<String>>> jsonAdapter =
        moshi.adapter(Types.newParameterizedType(List.class, List.class, String.class));

    try {
      // Parse the JSON string into a nested List<List<String>>
      List<List<String>> parsedData = jsonAdapter.fromJson(rawData);

      // Ensure there are at least two rows (headers + data)
      if (parsedData == null || parsedData.size() < 2) {
        return "[]"; // Return empty JSON array if input is invalid
      }

      // get headers and values
      List<String> headers = parsedData.get(0);
      List<String> values = parsedData.get(1);

      // make sure headers and values have the same length
      if (headers.size() != values.size()) {
        throw new IllegalArgumentException("Headers and values size mismatch!");
      }

      // Create a map for the broadband data
      Map<String, String> broadbandData = new HashMap<>();
      broadbandData.put("countyAndState", values.get(headers.indexOf("NAME")));
      broadbandData.put("percentage", values.get(headers.indexOf("S2802_C03_022E")));
      broadbandData.put("state", values.get(headers.indexOf("state")));
      broadbandData.put("county", values.get(headers.indexOf("county")));

      // Convert the map into a JSON string
      JsonAdapter<List<Map<String, String>>> outputAdapter =
          moshi.adapter(Types.newParameterizedType(List.class, Map.class));

      return outputAdapter.toJson(Collections.singletonList(broadbandData));
    } catch (IOException e) {
      e.printStackTrace();
      return "[]"; // Return empty JSON array if parsing fails
    }
  }

  /**
   * accesses the time of a API request
   *
   * @return the time of the API request from APIDataSource
   */
  @Override
  public String getTime() {
    String time = this.time;
    return time;
  }
}




