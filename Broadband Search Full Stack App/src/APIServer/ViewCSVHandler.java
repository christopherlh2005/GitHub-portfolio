package APIServer;

import Parser.CustomException;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;


/**
 * Handles a view request for our server to view a csv in json format
 */
public class ViewCSVHandler implements Route {

  private CSVManager manager;

  /**
   * Accepts a CSV handler via dependency injection.
   */
  public ViewCSVHandler(CSVManager manager) {
    this.manager = manager;
  }

  /**
   * @param request this allows the program to access the queryParams
   * @param response allows the server to process responses
   * @return either a successful or unsuccessful response
   *
   *
   * This method allows one to make a searchcsv request and fill out the according
   * responseMap to indicate whether the call is successful and which matches there are.
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String, Object> responseMap = new HashMap<>();
    try {
      List<List<String>> data = this.manager.view();
      responseMap.put("data", data);
      return new SuccessDataResponse(data).serialize();
    } catch (CustomException e) {
      return new FailureDataResponse(responseMap).serialize();
    }
  }

  /**
   * @param result a string for the result
   * @param data the data in the map, in this case the list of strings tht
   *             represents the CSV
   *
   * Record representing a Successful response and showing the full csv
   */

  public record SuccessDataResponse(String result, List<List<String>> data) {
    public SuccessDataResponse(List<List<String>> data) {
      this("success", data);
    }
    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      try {
        // Initialize Moshi which takes in this class and returns it as JSON!
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SuccessDataResponse> adapter = moshi.adapter(SuccessDataResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        // For debugging purposes, show in the console _why_ this fails
        // Otherwise we'll just get an error 500 from the API in integration
        // testing.
        e.printStackTrace();
        throw e;
      }
    }
  }

  /**
   * @param result a string for the result
   * @param data the data in the map, in this case the list of strings tht
   *             represents the CSV
   *
   * Record representing a Successful response and showing the full csv
   */
  public record FailureDataResponse(String result, Map<String, Object> data) {

    public FailureDataResponse(Map<String, Object> responseMap) {
      this("error_datasource", responseMap);
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      try {
        // Initialize Moshi which takes in this class and returns it as JSON!
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<FailureDataResponse> adapter = moshi.adapter(FailureDataResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        // For debugging purposes, show in the console _why_ this fails
        // Otherwise we'll just get an error 500 from the API in integration
        // testing.
        e.printStackTrace();
        throw e;
      }
    }
  }
}
