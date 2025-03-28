package Broadband;

import Parser.CustomException;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities class that handles converting a JSON into a Broadband and searching adding it to a list
 * of Broadbands. Also contains searching functionality.
 */
public class BroadbandUtilities {
  // a list of all provided broadband objects
  private List<Broadband> broadbands;

  /** Constructor for utilities class, initializes list. */
  public BroadbandUtilities() {
    this.broadbands = new ArrayList<>();
  }

  /**
   * @param json the file path of the mocked JSON file
   * @return A list of Broadbands.
   *     <p>Method to convert the JSON to a Broadband using Moshi. When complete, the Broadband is
   *     added to a list of all loaded Broadband objects.
   */
  public List<Broadband> createBroadband(String json) {
    try {
      // Initializes Moshi
      Moshi moshi = new Moshi.Builder().build();

      // Initializes an adapter to an Activity class then uses it to parse the JSON.
      JsonAdapter<List<Broadband>> jsonAdapter =
          moshi.adapter(Types.newParameterizedType(List.class, Broadband.class));

      this.broadbands = jsonAdapter.fromJson(json);
      return broadbands;
    }
    // Returns an empty list and prints stack trace.
    catch (IOException e) {
      e.printStackTrace();

      return new ArrayList<>();
    }
  }

  /**
   * allows the api server to return the percentage of people with access to broadbnad
   *
   * @return A string containing the percentage stored in the percentage field of a matching
   *     Broadband.
   *
   * @throws CustomException if an error occurs while parsing for percentage in the json
   */
  public String getPercentage() throws CustomException {
    return this.broadbands.get(0).getPercentage();
  }

}