import { useEffect, useState } from "react";
import { DataArea } from "./DataArea";
import "../styles/main.css";
import { datasetFiles } from "../data/mockedJson";

/**
 * This is an asynchronous function that loads a given file (based on filepath) to the server!
 * @param filepath - the file to load
 */
export async function loadData(filepath: string) {
  const fetch1 = await fetch("http://localhost:3232/loadcsv?filepath=data/" + filepath);
  const loadjson = await fetch1.json();

  if (loadjson["result"] === "success") {
    console.log("goober"); // i'm keeping this b/c i'm a D1 goober
  }
}

/**
* This is function allowing many constants to be defined
*/
export function DataSelector() {

  //states that respond to buttons or searches
  const [selectedOption, setSelectedOption] = useState("");
  const [searchTerm, setSearchTerm] = useState("");
  const [broadbandTerm, setBroadbandTerm] = useState("");

  //states for the search results
  const [searchResults, setSearchResults] = useState<(string | null)[]>([]);

  /**
   * This modifies selectedOption based on what is selected in the dropdown
   */
  const handleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedOption(event.target.value);
  };

  /**
   * This modifies searchTerm based on what is searched in the searchbar
   */
  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };

  /**
   * This modifies broadbandTerm based on what is searched in the searchbar
   */
  const handleBroadbandChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setBroadbandTerm(event.target.value);
  };

  /**
   * This modifies searchResults based on what is searched in the csv
   * @param filepath the file to search
   * @param searchOption what you are searching for
   */
  const searchData = async (filepath: string, searchOption: string) => {

    //make a call to the backend
    try {
      const response = await fetch(
          `http://localhost:3232/searchcsv?filepath=data/${filepath}&searchString=${searchOption}&hasHeader=True`
      );
      const loadjson1 = await response.json();


      //validate the json obtained from backend is of the right format and do error handling
      if (Array.isArray(loadjson1["data"])) {
        const filteredResults = loadjson1["data"]
        .map((row: any) => {
          if (Array.isArray(row) && row.length > 1) {
            const value = row[1];
            return typeof value === "string" && value.trim() !== "" ? value : null;
          }
          return null;
        })
        .filter((item) => item !== null); // Remove null values

        setSearchResults(filteredResults);
      } else {
        setSearchResults(["No matches found."]);
      }
    } catch (error) {
      console.error("Error fetching search results:", error);
      setSearchResults(["Error fetching results"]);
    }
  };

  /**
   * This modifies searchResults based on what is searched in Census API
   * @param stateAndCountyOption the state and county to search
   */
  const searchBroadbandData = async (stateAndCountyOption: string) => {

    //split the state and county
    let county: string;
    let state: string;

    const match = stateAndCountyOption.match(/^(.*?),\s*(.+)$/);
    if (match) {
      county = match[1];
      state = match[2]
    }
    else {
      county = "error";
      state = "error";
    }

    //make a call to the backend and get json
    try {
      //http://localhost:3232/broadband?state=California&county=San Mateo County
      const response = await fetch(
          `http://localhost:3232/broadband?state=${state}&county=${county}`
      );
      const loadjson2 = await response.json();

      console.log(loadjson2["percentage"])

      //update searchResults and error handle
      if (loadjson2["result"] == "success") {
        const filteredResults = loadjson2["percentage"]
        setSearchResults([`Broadband Coverage: ${filteredResults}%`]);

      } else {
        setSearchResults(["No matches found."]);
      }
    } catch (error) {
      console.error("Error fetching search results:", error);
      setSearchResults(["Error fetching results"]);
    }
  };

  /**
   * This executes the file search when you click the regular search button
   */
  const handleSearchClick = () => {
    if (selectedOption && searchTerm) {
      console.log(`Searching ${selectedOption} for "${searchTerm}"`);
      searchData(selectedOption, searchTerm);
    }
  };

  /**
   * This executes the api search when you click the regular search button
   */
  const handleBroadbandSearchClick = () => {
    if (broadbandTerm) {
      searchBroadbandData(broadbandTerm);
    }
  };

  /**
   * This constantly updates the data loaded based on a selected option
   */
  useEffect(() => {
    if (selectedOption) {
      loadData(selectedOption);
    }
  }, [selectedOption]);

  return (
      //dropdown container
      <div>
        <div className="dropdown-container">
          <input
              type="text"
              className="search-bar"
              placeholder="Enter What You Want to Search"
              value={searchTerm}
              onChange={(e) => {
                handleSearchChange(e);
                handleBroadbandChange(e);
              }}
          />

          <select aria-label="dropdown" className="dropdown" onChange={(e) => handleChange(e)}>
            <option aria-label="default-select" value="">
              Click Here to Select a File
            </option>

            {datasetFiles.map((file) => (
                <option aria-label={file} key={file} value={file}>
                  {file}
                </option>
            ))}

            <option>unknown.csv</option>
          </select>
        </div>

        <button className="search-files-button" onClick={handleSearchClick}>
          Search Files
        </button>

        <button className="search-API-button" onClick={handleBroadbandSearchClick}>
          Search Census API
        </button>

        {/* Display search results */}
        <div className="search-results">
          <h3>Search Result From Files Broadband %:</h3>
          <ul>
            {searchResults.map((result, index) => (
                <li key={index}>{result}</li>
            ))}
          </ul>
        </div>

        <DataArea selectedFile={selectedOption} />
      </div>
  );
}
