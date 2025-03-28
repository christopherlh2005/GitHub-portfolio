package APIServer;

import Parser.CustomException;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

/** Handles a get request for our server search for a specific thing in a csv */
public class SearchCSVHandler implements Route {

  private Map<String, Object> responseMap;
  private CSVManager myManager;

  /** Accepts a CSV handler via dependency injection. */
  public SearchCSVHandler(CSVManager myManager) {
    this.myManager = myManager;
    this.responseMap = new HashMap<>();
  }

  /**
   * @param request this allows the program to access the queryParams
   * @param response allows the server to process responses
   * @return a response map indicating if a call is successful
   *     <p>This method allows one to make a searchcsv request and fill out the according
   *     responseMap to indicate whether the call is successful and which matches there are.
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {

    // get queryParams for filepath, the string to search, the specific col (optional),
    // and the header status of a file
    Set<String> params = request.queryParams();
    String filePath = request.queryParams("filepath");
    String searchString = request.queryParams("searchString");
    String colID = request.queryParams("colID");
    String stringHasHeader = request.queryParams("hasHeader");

    // convert the hasHeader string into a usable boolean
    Boolean hasHeader = null;
    if (stringHasHeader.equalsIgnoreCase("True")) {
      hasHeader = Boolean.TRUE;
    } else if (stringHasHeader.equalsIgnoreCase("False")) {
      hasHeader = Boolean.FALSE;
    }

    // if no col parameter
    if (colID == null) {
      try {
        // search is successful
        List<List<String>> searchMatches = myManager.search(filePath, searchString, hasHeader);
        this.responseMap.put("result", "success");
        this.responseMap.put("data", searchMatches);
        this.responseMap.put("filePath", filePath);
        this.responseMap.put("searchString", searchString);
        this.responseMap.put("colID", null);
        return this.toJson(this.responseMap);
      } catch (CustomException e) {
        // if an exception is thrown due to a specific Search/Parser problem
        this.responseMap.put("result", "Error: " + e.getMessage());
        this.responseMap.put("data", "No matches due to error");
        this.responseMap.put("filePath", filePath);
        this.responseMap.put("searchString", searchString);
        this.responseMap.put("colID", null);
        return this.toJson(this.responseMap);
      } catch (IOException f) {
        // if an exception is thrown due to an Input/Output Exception
        this.responseMap.put("result", "Error: IOException");
        this.responseMap.put("data", "No matches due to error");
        this.responseMap.put("filePath", filePath);
        this.responseMap.put("searchString", searchString);
        this.responseMap.put("colID", null);
        return this.toJson(this.responseMap);
      }
    }
    // if the col parameter exists
    try {
      // search is successful
      List<List<String>> searchMatches = myManager.search(filePath, colID, searchString, hasHeader);
      this.responseMap.put("result", "success");
      this.responseMap.put("data", searchMatches);
      this.responseMap.put("filePath", filePath);
      this.responseMap.put("searchString", searchString);
      this.responseMap.put("colID", colID);
      return this.toJson(this.responseMap);
    } catch (CustomException e) {
      // if an exception is thrown due to a specific Search/Parser problem
      this.responseMap.put("result", "Error: " + e.getMessage());
      this.responseMap.put("data", "No matches due to error");
      this.responseMap.put("filePath", filePath);
      this.responseMap.put("searchString", searchString);
      this.responseMap.put("colID", colID);
      return this.toJson(this.responseMap);
    } catch (IOException f) {
      // if an exception is thrown due to an Input/Output Exception
      this.responseMap.put("result", "Error: IOException");
      this.responseMap.put("data", "No matches due to error");
      this.responseMap.put("filePath", filePath);
      this.responseMap.put("searchString", searchString);
      this.responseMap.put("colID", colID);
      return this.toJson(this.responseMap);
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
}
