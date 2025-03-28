import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import APIServer.APIDataSource;
import APIServer.CSVManager;
import APIServer.CachingDataSource;
import APIServer.FileDataSource;
import APIServer.LoadCSVHandler;
import APIServer.SearchCSVHandler;
import APIServer.ViewCSVHandler;
import Broadband.BroadbandHandler;
import Broadband.BroadbandUtilities;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mocks.MockACSDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class ServerTest {

  /** Test suite for API server functionality. */
  @BeforeAll
  public static void setupOnce() {
    Spark.port(0); // Pick an arbitrary free port
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root
  }

  private final Type mapStringObject =
      Types.newParameterizedType(Map.class, String.class, Object.class);
  private JsonAdapter<Map<String, Object>> adapter;
  private FileDataSource mockedSource = new MockACSDataSource();

  /**
   * Sets up the test environment by initializing necessary handlers and dependencies.
   *
   * @param source The file data source to be used.
   */
  public void setup(FileDataSource source) {
    CachingDataSource cacheSource = new CachingDataSource(source, 4, 10);
    BroadbandUtilities utilities = new BroadbandUtilities();
    CSVManager manager = new CSVManager();
    Spark.get("broadband", new BroadbandHandler(utilities, cacheSource));
    Spark.get("viewcsv", new ViewCSVHandler(manager));
    Spark.get("searchcsv", new SearchCSVHandler(manager));
    Spark.get("loadcsv", new LoadCSVHandler(manager));
    Spark.awaitInitialization(); // Ensure server starts before tests

    Moshi moshi = new Moshi.Builder().build();
    adapter = moshi.adapter(mapStringObject);
  }

  /** Cleans up server routes after each test to prevent conflicts. */
  @AfterEach
  public void tearDown() {
    Spark.unmap("loadcsv");
    Spark.unmap("viewcsv");
    Spark.unmap("searchscv");
    Spark.unmap("broadband");

    Spark.awaitStop(); // Stop server after each test
  }

  /**
   * Utility method to send an API request to the test server.
   *
   * @param apiCall The API endpoint with query parameters.
   * @return The HttpURLConnection object representing the response.
   */
  private HttpURLConnection tryRequest(String apiCall) throws IOException {
    String fullURL = "http://localhost:" + Spark.port() + "/" + apiCall;
    System.out.println("Sending request to: " + fullURL); // Debugging line

    URL requestURL = new URL(fullURL);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestProperty("Content-Type", "application/json");
    clientConnection.setRequestProperty("Accept", "application/json");
    clientConnection.connect();
    return clientConnection;
  }

  /**
   * Tests a successful broadband data request with a valid state and county.
   *
   * @throws IOException if file cannot be opened
   */
  @Test
  public void testBroadbandRequestSuccess() throws IOException {
    this.setup(mockedSource);
    String encodedState = URLEncoder.encode("Michigan", StandardCharsets.UTF_8);
    String encodedCounty = URLEncoder.encode("Wayne County", StandardCharsets.UTF_8);
    HttpURLConnection connection =
        tryRequest("broadband?state=" + encodedState + "&county=" + encodedCounty);
    assertEquals(200, connection.getResponseCode());

    Map<String, Object> responseBody = this.getResponseBody(connection.getInputStream());
    showDetailsIfError(responseBody);
    assertEquals("success", responseBody.get("result"));
    System.out.println(responseBody);
    assertNotNull(responseBody.get("percentage"));

    connection.disconnect();
  }

  /**
   * Tests loading a CSV file and searching for a value within it.
   *
   * @throws IOException if file cannot be opened
   */
  @Test
  public void testCSVLoadAndSearch() throws IOException {
    this.setup(mockedSource);
    HttpURLConnection loadCsv = tryRequest("loadcsv?filepath=data/provided.csv");
    assertEquals(200, loadCsv.getResponseCode());

    String searchString = this.encode("New Shoreham");
    HttpURLConnection searchCsv =
        tryRequest(
            "searchcsv?filepath=data/provided.csv&searchString="
                + searchString
                + "&colID=0&hasHeader=True");
    assertEquals(200, searchCsv.getResponseCode());

    Map<String, Object> responseBody = this.getResponseBody(searchCsv.getInputStream());
    assertEquals("success", responseBody.get("result"));
    assertNotNull(responseBody.get("data"));

    loadCsv.disconnect();
    searchCsv.disconnect();
  }

  /**
   * Tests an error condition where a CSV search is performed on a file that does not match the
   * previously loaded CSV file. Ensures the correct error response is returned.
   *
   * @throws IOException if file cannot be opened
   */
  @Test
  public void testCSVErrorCondition() throws IOException {
    this.setup(mockedSource);
    HttpURLConnection loadCsv = tryRequest("loadcsv?filepath=data/provided.csv");
    assertEquals(200, loadCsv.getResponseCode());

    String searchString = this.encode("New Shoreham");
    HttpURLConnection searchCsv =
        tryRequest(
            "searchcsv?filepath=data/provided&searchString="
                + searchString
                + "&colID=0&hasHeader=True");
    assertEquals(200, searchCsv.getResponseCode());

    Map<String, Object> responseBody = this.getResponseBody(searchCsv.getInputStream());
    assertEquals("Error: filepath does not match loaded csv", responseBody.get("result"));
    assertNotNull(responseBody.get("data"));

    loadCsv.disconnect();
    searchCsv.disconnect();
  }

  /**
   * Tests a scenario where both CSV and broadband queries are performed in sequence. Ensures both
   * endpoints return successful responses and expected data.
   *
   * @throws IOException if file cannot be opened
   */
  @Test
  public void testCombinedCSVAndBroadbandQueries() throws IOException {
    this.setup(mockedSource);
    HttpURLConnection loadCsv = tryRequest("loadcsv?filepath=data/provided.csv");
    assertEquals(200, loadCsv.getResponseCode());
    String newShoreham = this.encode("New Shoreham");
    HttpURLConnection queryCsv =
        tryRequest(
            "searchcsv?filepath=data/provided.csv&searchString="
                + newShoreham
                + "&colID=0&hasHeader=True");
    assertEquals(200, queryCsv.getResponseCode());
    String encodedCounty = this.encode("Shiawassee County");
    String encodedState = this.encode("Michigan");
    HttpURLConnection queryBroadband =
        tryRequest("broadband?state=" + encodedState + "&county=" + encodedCounty);
    assertEquals(200, queryBroadband.getResponseCode());

    Map<String, Object> csvResponse = this.getResponseBody(queryCsv.getInputStream());
    assertEquals("success", csvResponse.get("result"));

    Map<String, Object> broadbandResponse = this.getResponseBody(queryBroadband.getInputStream());
    assertEquals("success", broadbandResponse.get("result"));
    assertNotNull(broadbandResponse.get("percentage"));
    System.out.println(broadbandResponse.get("percentage"));
    assertEquals("81.5", broadbandResponse.get("percentage"));

    loadCsv.disconnect();
    queryCsv.disconnect();
    queryBroadband.disconnect();
  }

  /**
   * Tests a successful broadband request using real API data. Ensures the response contains a valid
   * broadband percentage.
   *
   * @throws IOException if file cannot be opened
   */
  @Test
  public void testBroadbandRequestSuccessReal() throws IOException {
    APIDataSource dataSource = new APIDataSource();
    this.setup(dataSource);
    String encodedState = this.encode("New Jersey");
    String encodedCounty = this.encode("Union County");
    HttpURLConnection connection =
        tryRequest("broadband?state=" + encodedState + "&county=" + encodedCounty);
    assertEquals(200, connection.getResponseCode());

    Map<String, Object> responseBody = this.getResponseBody(connection.getInputStream());
    showDetailsIfError(responseBody);
    assertEquals("success", responseBody.get("result"));
    assertNotNull(responseBody.get("percentage"));

    connection.disconnect();
  }

  /**
   * Tests a broadband request with an invalid state and county. Ensures the correct error response
   * is returned.
   *
   * @throws IOException if file cannot be opened
   */
  @Test
  public void testBroadbandRequestFail_invalidLocation() throws IOException {
    APIDataSource real = new APIDataSource();
    this.setup(real);
    HttpURLConnection connection = tryRequest("broadband?state=FakeState&county=Nowhere");
    assertEquals(200, connection.getResponseCode());

    Map<String, Object> responseBody = this.getResponseBody(connection.getInputStream());
    showDetailsIfError(responseBody);
    assertEquals("error_bad_request State does not exist.", responseBody.get("result"));

    connection.disconnect();
  }

  /** Utility method to print details in case of an error */
  private void showDetailsIfError(Map<String, Object> body) {
    if (body.containsKey("result") && body.get("result").toString().startsWith("error")) {
      System.out.println(body);
    }
  }

  /**
   * Utility method to convert InputStream response to a JSON map.
   *
   * @param inputStream The input stream containing JSON response.
   * @return Parsed response as a map.
   */
  private Map<String, Object> getResponseBody(InputStream inputStream) throws IOException {
    try (InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(reader)) {
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        response.append(line);
      }
      return adapter.fromJson(response.toString()); // Ensure the adapter is correctly set up
    }
  }

  /** Utility method to encode strings for use in URL */
  private String encode(String input) {
    return URLEncoder.encode(input, StandardCharsets.UTF_8);
  }
}
