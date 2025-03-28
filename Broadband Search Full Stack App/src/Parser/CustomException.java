package Parser;

/** This is a class allows me to put a detailed message as to what the problem is. */
public class CustomException extends Exception {

  /**
   * @param message is the specific error message you want to communicate
   */
  public CustomException(String message) {
    super(message); // Call the parent class constructor
  }
}
