package Parser;

import java.util.List;

/**
 * This is a class that allows you to test if the Creator classes are implementing the interface
 * work for testing purposes.
 */
public class TrivialCreator implements CreatorFromRow<List<String>> {

  public TrivialCreator() {}

  /**
   * @param row the specific row whose details you put in the argument
   * @return the new list of strings
   *     <p>This method allows you to create a string list to test the concept.
   */
  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }
}
