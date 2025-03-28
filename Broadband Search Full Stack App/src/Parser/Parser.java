package Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This is a class that has a csv parser that will parse a csv file and turn each row into a new
 * object as specified by the creatorClass parameter. It also has a variety of accessor methods for
 * defensive purposes.
 */
public class Parser<T> {
  private Reader reader;
  private Boolean hasHeader;
  private List<List<String>> parsedContent;
  private CreatorFromRow<T> myCreatorClass;
  private List<T> objectList;
  private List<String> firstLine;

  /**
   * @param reader - path to a csv file to be parsed
   * @param creatorClass - the class of the object you want to transform a row into
   * @param hasHeader - a boolean describing whether a csv file has a header
   */
  public Parser(Reader reader, CreatorFromRow<T> creatorClass, Boolean hasHeader)
      throws IOException {
    this.reader = reader;
    this.parsedContent = new ArrayList<>();
    this.hasHeader = hasHeader;
    this.myCreatorClass = creatorClass;
    this.objectList = new ArrayList<>();
    this.firstLine = new ArrayList<>();
  }

  /**
   * TODO feel free to modify this method to incorporate your design choices
   *
   * @throws IOException when buffer reader fails to read-in a line/filepath
   * @throws CustomException there are 2 custom exceptions. The first is if the file is null and the
   *     second is if the rows of the csv file have an inconsistent number of cols.
   * @throws FactoryFailureException when there are more/less parameters in a row from the csv file
   *     than the number required to inialize a new object.
   *     <p>This method will parse a CSV file and convert each row into an object of the type
   *     specified by the creatorClass parameter in parser.
   */
  public void parse() throws IOException, CustomException, FactoryFailureException {
    String line;
    Pattern regexSplitCSVRow = Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");
    BufferedReader readInBuffer =
        new BufferedReader(reader); // wraps around readers to improve efficiency when reading

    // check null by seeing if the first row is empty
    String firstLine = readInBuffer.readLine();
    if (firstLine == null) {
      throw new CustomException("The CSV file is empty.");
    }
    String[] firstLineResult = regexSplitCSVRow.split(firstLine);
    this.firstLine = Arrays.stream(firstLineResult).toList();

    // only adds the first row if it's not a header
    if (this.hasHeader == Boolean.FALSE) {
      parsedContent.add(this.firstLine);
      T firstLineObject = myCreatorClass.create(this.firstLine);
      objectList.add(firstLineObject);
    }

    // while the next line is not empty
    while ((line = readInBuffer.readLine()) != null) {
      String[] result = regexSplitCSVRow.split(line);

      // throw an error if the length of rows is inconsistent
      if (firstLineResult.length != result.length) {
        throw new CustomException("Inconsistent length of rows.");
      }

      // otherwise add the parsed row to parsedContent and then call the create method
      List<String> lineToArr = Arrays.stream(result).toList();
      parsedContent.add(lineToArr);
      T nextLineObject = myCreatorClass.create(lineToArr);
      objectList.add(nextLineObject);
    }
    readInBuffer.close();
  }

  /**
   * @return all the parsed content for a file
   *     <p>This accessor method accesses the parsed contents of a file.
   */
  public List<List<String>> getParsedContent() {
    List<List<String>> copyParsedContent = this.parsedContent;
    return copyParsedContent;
  }

  /**
   * @return first line in file
   *     <p>This accessor method accesses the parsed contents of a file.
   */
  public List<String> getFirstLine() {
    List<String> copyFirstLine = this.firstLine;
    return copyFirstLine;
  }
}
