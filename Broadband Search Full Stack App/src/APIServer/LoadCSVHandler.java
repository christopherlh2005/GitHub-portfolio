package APIServer;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

/** Handles a get request for our server to load a CSV. */
public class LoadCSVHandler implements Route {

  // private FileDataSource filepath;
  private Map<String, Object> responseMap;

  private CSVManager myManager;

  /** Accepts a CSV handler via dependency injection. */
  public LoadCSVHandler(CSVManager myManager) {
    // this.filepath = filepath;
    this.myManager = myManager;
  }

  /**
   * @param request this allows the program to access the queryParams
   * @param response allows the server to process responses
   * @return a response map indicating if a call is successful
   *     <p>This method allows one to make a loadCSV call and fill out the according responseMap to
   *     indicate whether the call is successful.
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {

    // gets the queryParams
    Set<String> params = request.queryParams();
    String filepath = request.queryParams("filepath");

    // if no filepath is given, send a response indicating the filepath is null
    this.responseMap = new HashMap<>();
    if (filepath == null) {
      this.responseMap.put("result", "error_bad_request: null filepath");
      this.responseMap.put("filepath", "null");
      return this.toJson(this.responseMap);
    }

    // if the filepath works, indicate the success on the responseMap
    try {
      this.myManager.load(filepath);
      this.responseMap.put("result", "success");
      this.responseMap.put("filepath", filepath);
      return this.toJson(this.responseMap);
    }
    // if a file is not found, indicate the filepath isn't found on the responseMap
    catch (FileNotFoundException e) {
      this.responseMap.put("result", "error_datasource: filepath not found");
      this.responseMap.put("filepath", filepath);
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
