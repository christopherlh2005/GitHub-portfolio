import {useEffect, useState} from "react";
import { ViewButton } from "./ToggleViewButton";
import { Bar } from "react-chartjs-2";
import "chart.js/auto";
import {loadData} from "./DataSelector";

interface DataAreaProps {
  selectedFile: string;
}

export function DataArea({ selectedFile }: DataAreaProps) {
  const [tableSize] = useState({
    width: window.innerWidth,
    height: window.innerHeight
  });

  const [tableData, setTableData] = useState<string[][]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        await loadData(selectedFile);
        setError(null);
        const response = await fetch(`http://localhost:3232/viewcsv`);
        if (!response.ok) {
          throw new Error("ERROR: Could not fetch data!");
        }
        const json = await response.json();
        const data = await json.data;
        setTableData(data || []);
      } catch (err) {
        setError("ERROR: Could not load data!");
      }
    };

    if (selectedFile) {
      fetchData();
    }
  }, [selectedFile]);

  const missingHeader = tableData.length > 0 && tableData[0].some(cell => !isNaN(Number(cell)));
  const boolArray = tableData.slice(1).map(row => row.slice(1).every(cell => !isNaN(Number(cell))));
  const yAllNumbers = boolArray.every(value => value === true);
  const [isTable, setIsTable] = useState<boolean>(true);

  return (
      <div
          aria-label="data-container"
          className="data-container"
          style={{
            maxWidth: tableSize.width,
            maxHeight: tableSize.height,
            overflow: "auto"
          }}
      >
        {selectedFile && tableData.length !== 0 ? (
            <>
              <h3 aria-label="showing-data-for-text">Showing data for: {selectedFile}</h3>
              <ViewButton isTableView={isTable} setIsTable={setIsTable} />
              {missingHeader ? (
                  <p aria-label="error-message" className="error-message">Error: Missing header in CSV.</p>
              ) : isTable ? (
                  <table aria-label="results-table">
                    <thead>
                    <tr>
                      {tableData.length > 0 &&
                          tableData[0].map((header, headerIndex) => (
                              <th aria-label={header} className="table-head" key={headerIndex}>
                                {header}
                              </th>
                          ))}
                    </tr>
                    </thead>
                    <tbody>
                    {tableData.slice(1).map((row, rowIndex) => (
                        <tr className="table-row" key={rowIndex}>
                          {row.map((cell, cellIndex) => (
                              <td
                                  aria-label={`row ${row[0]}, column ${tableData[0][cellIndex]}` + (cellIndex !== 0 ? `, value ${cell}` : "")}
                                  className="table-data"
                                  key={cellIndex}
                              >
                                {cell}
                              </td>
                          ))}
                        </tr>
                    ))}
                    </tbody>
                  </table>
              ) : tableData.length > 10 || tableData[0].length > 10 ? (
                  <p className="error-message" aria-label="error-message">
                    Error: Too many rows or columns for graph view (max 10 each).
                  </p>
              ) : yAllNumbers ? (
                  <Bar
                      aria-label="Bar Chart"
                      data={{
                        labels: tableData.slice(1).map(row => row[0]),
                        datasets: tableData[0].slice(1).map((_, index) => ({
                          label: tableData[0][index + 1],
                          data: tableData.slice(1).map(row => Number(row[index + 1]))
                        }))
                      }}
                      options={{
                        plugins: {
                          legend: {
                            display: true
                          }
                        },
                        scales: {
                          x: {
                            title: {
                              display: true,
                              text: tableData[0][0],
                              font: { size: 15 }
                            }
                          }
                        }
                      }}
                  />
              ) : (
                  <p aria-label="error-message" className="error-message">
                    Error: y-axes could not be parsed into numbers.
                  </p>
              )}
            </>
        ) : tableData.length === 0 && selectedFile !== "" ? (
            <p aria-label="error-message" className="error-message">Could not find data.</p>
        ) : (
            <p aria-label="start-message"></p>
        )}
      </div>
  );
}
