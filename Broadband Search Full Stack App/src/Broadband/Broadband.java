package Broadband;

import Parser.CustomException;

/**
 * Wrapper class for a Broadband data object stored as a row in a JSON file. Used to convert the
 * JSON into an object using Moshi.
 */
public class Broadband {

  private String countyAndState;
  private String percentage;
  private String state;
  private String county;
  private String time;

  /** Constructor for the class. */
  public Broadband() {
  }

  /**
   * @return the percentage field of the broadband
   *     <p>Getter for the percentage field.
   */
  public String getPercentage() throws CustomException {
    String copyPercentage = this.percentage;
    return copyPercentage;
  }

}
