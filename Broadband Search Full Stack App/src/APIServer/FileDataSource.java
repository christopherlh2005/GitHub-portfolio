package APIServer;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Interface implemented by CachingAPIDataSource and APIDataSource
 */
public interface FileDataSource {

  /**
   * Search the CSV file for data
   *
   * @param state the file to retrieve data for
   * @return the file data obtained
   * @throws IllegalArgumentException if the geolocation given is invalid
   */
  public String getResponse(String state, String county, String variableSpecifier)
      throws URISyntaxException, IOException, InterruptedException;

  /**
   * Converts state and county names to codes
   *
   * @param stateCode the name of a state
   * @param countyCode the name of a county
   *
   * @return a list consisting of the state and county code
   *
   * @throws URISyntaxException if URI has bad syntax
   * @throws IOException any input mistakes or mistakes with state/county
   * @throws InterruptedException in case of interruption
   */
  public List<String> getStateAndCounty(String stateCode, String countyCode) throws
      URISyntaxException, IOException, InterruptedException;

  /**
   * accesses the time of a API request
   *
   * @return the time of the API request from APIDataSource
   */
  public String getTime();

}
