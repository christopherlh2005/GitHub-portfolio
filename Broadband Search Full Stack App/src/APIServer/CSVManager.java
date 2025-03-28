package APIServer;

import Parser.CustomException;
import Parser.FactoryFailureException;
import Parser.Parser;
import Parser.TrivialCreator;
import Search.Search;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * A class that keeps track of the currently loaded CSV and performs parsing and searching logic on
 * that CSV.
 */
public class CSVManager {

  private List<List<String>> currentCSV;
  private String currFilePath;

  /** Constructor for the class. */
  public CSVManager() {}

  /**
   * @param filepath the file path of the CSV being loaded
   *     <p>Initializes instance variables so they won't be initialized if load is not called.
   *     Parses the CSV and stores it as a list of strings.
   */
  public void load(String filepath) throws IOException, CustomException, FactoryFailureException {
    this.currentCSV = null;
    FileReader data = new FileReader(filepath);
    Parser myParser = new Parser(data, new TrivialCreator(), Boolean.FALSE);
    myParser.parse();
    this.currentCSV = myParser.getParsedContent();
    this.currFilePath = filepath;
  }

  /**
   * @param filePath the file path of the provided CSV
   * @param searchTerm the term being search for
   * @param hasHeader a boolean for whether the file has a header
   * @return A list of matching strings.
   * @throws CustomException if search is malformed or no matches are found.
   * @throws IOException in case of issues reading file
   *     <p>Search method when no column is provided.
   */
  public List<List<String>> search(String filePath, String searchTerm, Boolean hasHeader)
      throws CustomException, IOException {
    if (!filePath.equals(this.currFilePath)) {
      throw new CustomException("filepath does not match loaded csv");
    }
    if (filePath == null) {
      throw new CustomException("filepath is null");
    }
    if (searchTerm == null) {
      throw new CustomException("search term is null");
    }

    if (hasHeader == null) {
      throw new CustomException("hasHeader term is missing");
    }

    Search mySearch = new Search(filePath, searchTerm, hasHeader);

    List<List<String>> matches = mySearch.getMatch();
    if (matches.isEmpty()) {
      throw new CustomException("No matches");
    }
    return matches;
  }

  /**
   * @param filepath the file path of the provided CSV
   * @param column the column information provided (either header or index)
   * @param searchTerm the term being search for
   * @param hasHeader a boolean for whether the file has a header
   * @return A list of matching strings.
   * @throws CustomException if search is malformed or no matches are found.
   * @throws IOException in case of issues reading file
   *     <p>Search method when no column is provided.
   */
  public List<List<String>> search(
      String filepath, String column, String searchTerm, Boolean hasHeader)
      throws CustomException, IOException {
    if (!filepath.equals(this.currFilePath)) {
      throw new CustomException("filepath does not match loaded csv");
    }
    if (filepath == null) {
      throw new CustomException("filepath is null");
    }
    if (searchTerm == null) {
      throw new CustomException("search term is null");
    }
    if (hasHeader == null) {
      throw new CustomException("hasHeader term is missing");
    }

    Search mySearch = new Search(filepath, searchTerm, column, hasHeader);
    List<List<String>> matches = mySearch.getMatch();
    if (matches.isEmpty()) {
      throw new CustomException("No matches");
    }
    return matches;
  }

  /**
   * @return a defensive copy of the whole CSV represented as a list of strings
   * @throws CustomException if no file has been loaded
   *     <p>Search method when no column is provided.
   */
  public List<List<String>> view() throws CustomException {
    if (this.currentCSV == null) {
      throw new CustomException("File not loaded");
    } else {
      return List.copyOf(this.currentCSV);
    }
  }
}
