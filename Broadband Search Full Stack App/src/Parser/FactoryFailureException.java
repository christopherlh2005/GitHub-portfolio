package Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class that raises an error if there are more/less parameters than what is required to
 * create a new object.
 */
public class FactoryFailureException extends Exception {
  final List<String> row;

  /**
   * @param message is the specific error message you want to communicate
   * @param row is the specific row you want to return with the error
   */
  public FactoryFailureException(String message, List<String> row) {
    super(message);
    this.row = new ArrayList<>(row);
  }
}
