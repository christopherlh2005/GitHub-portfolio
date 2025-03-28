package Parser;

import java.util.List;

/** This is a class that creates new Star objects. */
public class StarCreator implements CreatorFromRow<Star> {

  public StarCreator() {}

  /**
   * @param row the specific row whose details you put in the argument
   * @return the new Star
   *     <p>This method creates a new Star object.
   */
  @Override
  public Star create(List<String> row) throws FactoryFailureException {
    if (row.size() != 5) {
      throw new FactoryFailureException("You don't have 5 parameters", row);
    }
    return new Star(row.get(0), row.get(1), row.get(2), row.get(3), row.get(4));
  }
}
