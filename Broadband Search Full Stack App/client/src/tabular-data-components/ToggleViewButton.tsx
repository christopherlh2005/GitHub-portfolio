import { Dispatch, SetStateAction } from "react";
/**
 * An interface for view setting (table or graph).
 */
interface viewProps {
  isTableView: boolean;
  setIsTable: Dispatch<SetStateAction<boolean>>;
}

/**
 * Builds a component that manages the view button and end-user's viewing state.
 *
 * @param props to access view state 
 * @returns JSX to let user know they can convert to graph view if they are on table view,
 * and vice versa.
 */
export function ViewButton(props: viewProps) {
  /**
   * Function to manage changing the view.
   *
   * @returns whether the updated view is the table view or not.
   */
  const changeView = () => {
    const newValue = !props.isTableView;
    props.setIsTable(newValue);
    return newValue;
  };

  if (props.isTableView) {
    return (
      <button aria-label="Switch to Graph" onClick={changeView}>
        Switch to Graph Mode
      </button>
    );
  } else {
    return (
      <button aria-label="Switch to Table" onClick={changeView}>
        Switch to Table Mode
      </button>
    );
  }
}
