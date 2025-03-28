package Search;

import Parser.CustomException;
import Parser.FactoryFailureException;
import Parser.Parser;
import Parser.TrivialCreator;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a class that allows a parser to search a file for a specific input and returns the
 * contents of the desired row.
 */
public class Search {

  private String filepath;
  private String searchItem;
  private String columnIdentifier;
  private Parser<List<String>> myParser;
  private List<List<String>> matchedRows;
  private Boolean hasHeader;

  /**
   * @param filename - name of the file being searched
   * @param searchItem - the actual String you are searching for
   * @param columnIdentifier - a specific col you are searching
   */
  public Search(String filename, String searchItem, String columnIdentifier, Boolean hasHeader) {
    this.filepath = filename;
    this.searchItem = searchItem;
    this.columnIdentifier = columnIdentifier;
    this.matchedRows = new ArrayList<>();
    this.hasHeader = hasHeader;
  }

  /**
   * @param filepath - name of the file being searched
   * @param searchItem - the actual String you are searching for
   */
  public Search(String filepath, String searchItem, Boolean hasHeader) {
    this.filepath = filepath;
    this.searchItem = searchItem;
    this.columnIdentifier = null;
    this.matchedRows = new ArrayList<>();
    this.hasHeader = hasHeader;
  }

  /**
   * @throws IOException if an input/output is invalid
   * @throws CustomException for errors including any error from the parser, an error to search in a
   *     col that doesn't exist, search for a col based on a header name that doesn't exist, or
   *     trying to search for a header on a file
   *     <p>returns the row(s) that match with a given search criteria
   */
  public List<List<String>> getMatch() throws IOException, CustomException {

    // initializes new parser and file reader
    FileReader data = new FileReader(filepath);
    this.myParser = new Parser<List<String>>(data, new TrivialCreator(), this.hasHeader);

    // actually parses the file
    try {
      this.myParser.parse();
    } catch (FactoryFailureException e) {
      throw new CustomException(e.getMessage());
    } catch (CustomException f) {
      throw new CustomException(f.getMessage());
    }

    // searches for parsed content matches and adds these rows to a list to return
    for (int i = 0; i < this.myParser.getParsedContent().size(); i++) {

      Boolean matchRow = Boolean.FALSE;
      List<String> row = this.myParser.getParsedContent().get(i);

      // searches through everything if no specific col
      if (this.columnIdentifier == null) {
        for (int j = 0; j < row.size(); j++) {
          String word = row.get(j);

          if (word.equalsIgnoreCase(this.searchItem)) {
            matchRow = Boolean.TRUE;
          }
        }
      }
      // searches only through a specific col int (this is also used after converting a string
      // header
      // to a col int
      else {
        try {
          int number = Integer.parseInt(this.columnIdentifier);

          String word;
          try {
            word = row.get(number);
          } catch (IndexOutOfBoundsException e) {
            throw new CustomException(
                "This col does not exist. Your integer is too high or negative!");
          }

          if (word.equalsIgnoreCase(this.searchItem)) {
            matchRow = Boolean.TRUE;
          }
        }
        // using a String, finds the matching header and converts into col int
        catch (NumberFormatException e) {
          Boolean headerNotMatched = Boolean.TRUE;

          if (this.hasHeader) {
            if (i == 0) {
              for (int k = 0; k < myParser.getFirstLine().size(); k++) {
                String word = myParser.getFirstLine().get(k);

                if (word.equalsIgnoreCase(this.columnIdentifier)) {
                  this.columnIdentifier = String.valueOf(k);
                  headerNotMatched = Boolean.FALSE;
                  i = -1;
                }
              }
            }
            // exception if header does not exist
            if (headerNotMatched) {
              throw new CustomException("No matches in header");
            }
          }
          // exception for trying to search for a header if a csv has no header
          else {
            throw new CustomException("Can't search up header name when you have no header");
          }
        }
      }

      // if a row matches, add it to the row to be returned
      if (matchRow) {
        this.matchedRows.add(row);
      }
    }

    return this.matchedRows;
  }
}
