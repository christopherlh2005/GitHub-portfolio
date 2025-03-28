package APIServer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;

/**
 * This is a Proxy Data Wrapper that wraps APIDataSource, caching data in the process.
 */
public class CachingDataSource implements FileDataSource {
  private final FileDataSource original;
  private final Cache<String, String> cache;

  //constructor
  public CachingDataSource(FileDataSource original, int maxSize, int expireAfterMinutes) {
    this.original = original;
    this.cache = CacheBuilder.newBuilder()
        .maximumSize(maxSize)
        .expireAfterWrite(expireAfterMinutes, TimeUnit.MINUTES)
        .build();
  }

  /**
   * returns from the cache if key is present
   *
   * @return the String of the value
   */
  public String get(String key) {
    String keyToReturn = cache.getIfPresent(key);
    return keyToReturn;
  }

  /**
   * places a new key in the cache
   */
  public void put(String key, String data) {
    cache.put(key, data);
  }

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
      throws IllegalArgumentException, URISyntaxException, IOException, InterruptedException {
    String key = this.generateKey(stateCode, countyCode);
    //adds data to cache if it didn't previously exist
    if (this.get(key) == null) {
      String data = this.original.getResponse(stateCode, countyCode, variableSpecifier);
      this.put(key, data);
      System.out.println("Key put in cache");
      return data;
      //gets data from cache if it is stored there
    } else  {
      System.out.println("Key accessed from cache");
      return this.get(key);
    }
  }

  /**
   * Converts state and county names to codes
   *
   * @param stateCode the code of a state
   * @param countyCode the code of a county
   *
   * @return a list consisting of the state and county code
   *
   * @throws URISyntaxException if URI has bad syntax
   * @throws IOException any input mistakes or mistakes with state/county
   * @throws InterruptedException in case of interruption
   */
  @Override
  public List<String> getStateAndCounty(String stateCode, String countyCode) throws URISyntaxException, IOException,
      InterruptedException {

    List<String> stateAndCounty = this.original.getStateAndCounty(stateCode, countyCode);
    return stateAndCounty;
  }

  //throw new UnsupportedOperationException();

  /**
   * generates a key by concatinating the state and county
   */
  private String generateKey(String state, String county) {
    return state + "-" + county;
  }

  /**
   * accesses the time of a API request
   *
   * @return the time of the API request from APIDataSource
   */
  @Override
  public String getTime() {
    String time = this.original.getTime();
    return time;
  }

}
