import "../styles/App.css";
import { DataSelector } from "./DataSelector";
import {
  SignedIn,
  SignedOut,
  SignInButton,
  SignOutButton,
  UserButton,
} from "@clerk/clerk-react";

/**
 * Main application component that renders the Home Appraisal interface.
 *
 * @returns - The root JSX for the app, containg the app header and 
 * data selector component.
 */
function App() {
    return (
        <div className="App">
          <div className="App-header">
            
            <h1 aria-label="Broadband Application Header">Broadband Application</h1>

          </div>

          <SignedIn>

            <div className="User-Container">
              <SignOutButton>
                SIGN OUT
              </SignOutButton>

              <UserButton>

              </UserButton>
            </div>

            <div className="App-Instructions">

              <h3 aria-label="Welcome Message">
                Welcome to the Broadband Application!
              </h3>
              <p aria-label="Instructions Paragraph">
                Instructions: <br/>

                To view uploaded data: select the desired filename using the dropdown menu.
                If the data was successfully uploaded, the data will appear in tabular form,
                and there will be a button to press to switch to a bar graph. <br/>

                To search for a term within an uploaded data file: enter the search term
                in the search bar. After pressing the "Search Files" button, you should obtain
                the desired matching cell.

                To search for a term from the broadband API: enter the search term in the form
                county, state in the search bar. After pressing the "Search API" button, the
                broadband percentage for that county should display below.
              </p>


            </div>

            <DataSelector/>

          </SignedIn>

          <SignedOut>
            <SignInButton>
              SIGN IN
            </SignInButton>
          </SignedOut>
        </div>
    )

}
export default App;

