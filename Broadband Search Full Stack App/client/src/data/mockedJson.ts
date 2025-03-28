/**
 * Class containing mock csv data in array format
 * 
 * @constant {Record<string, string[][]>} mockData - The mocked data for various datasets.
 */
export const mockData: Record<string, string[][]> = {
    "data_2020.csv": [
        ["Name", "Age", "City"],
        ["Alice", "25", "New York"],
        ["Bob", "30", "Los Angeles"],
        ],

        "data_2021.csv": [
        ["Product", "Price ($)", "Stock"],
        ["Laptop", "1200", "15"],
        ["Phone", "800", "30"],
        ],

        "data_2022.csv": [
        ["Country", "GDP ($100B)", "Population (M)"],
        ["USA", "210", "331"],
        ["India", "30", "1400"],
        ],

        "experiment_results.csv": [
        ["Experiment", "Result"],
        ["Test A", "Pass"],
        ["Test B", "Fail"],
        ],

        "huge_column_results.csv": [
            ["QWERTYUIOPASDFGHJKLZXCVBNM", "BQWERTYUIOPASDFGHJKLZXCVBNMM", "CDFGHJKL", 
                "DSDFGHJK", "EASDFGHJKLP", "FASDFGHJKL", "GQWERTYUIOP", "HASDFGHJKL", "IASDFGHJKL", 
                "QQWERTYUIOP", "WSDFGHJKL", "EQWERTYUIO", "RASDFGHJKL", "TZXCVBNM", 
                "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", 
                "L", "Z", "X", "C", "V", "B", "N", "M"],
            ["A", "B", "C", "D", "E", "F", "G", "H", "I", "Q", "W", "E", "R", "T", 
                "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", 
                "L", "Z", "X", "C", "V", "B", "N", "M"]
        ],

        "huge_row_results.csv": 
        [
            ["Name", "CSLogin"],
            ["Taylor", "taylor13"],
            ["Selena Gomez", "selgomez"],
            ["Ariana Grande", "arianag"],
            ["Beyoncé", "beyhive"],
            ["Drake", "drizzy"],
            ["Billie Eilish", "billiee"],
            ["Ed Sheeran", "edsheeran"],
            ["Dua Lipa", "dualipa"],
            ["Harry Styles", "hstyles"],
            ["Olivia Rodrigo", "oliviar"],
            ["Shawn Mendes", "shawnm"],
            ["Kanye West", "yeezus"],
            ["Lana Del Rey", "lanadel"],
            ["Rihanna", "rihrih"],
            ["Justin Bieber", "jbiebz"],
            ["Miley Cyrus", "mileyc"],
            ["The Weeknd", "weeknd"],
            ["Post Malone", "posty"],
            ["Katy Perry", "katyperry"],
            ["Doja Cat", "dojac"],
            ["Bruno Mars", "brunom"],
            ["Charlie Puth", "charliep"],
            ["Adele", "adele"],
            ["Sam Smith", "samsmith"],
            ["Nicki Minaj", "nickim"],
            ["Lizzo", "lizzo"],
            ["SZA", "szamusic"],
            ["Halsey", "halsey"],
            ["Camila Cabello", "camilac"],
            ["Zayn Malik", "zaynmalik"],
            ["Lil Nas X", "lilnasx"],
            ["Frank Ocean", "franko"],
            ["Lady Gaga", "ladyg"],
            ["Travis Scott", "traviss"],
            ["J. Cole", "jcole"],
            ["Cardi B", "cardib"],
            ["Kendrick Lamar", "kendrickl"],
            ["Meghan Trainor", "meghantr"],
            ["Charlie XCX", "charliexcx"],
            ["Tyler, the Creator", "tylertc"],
            ["Shakira", "shakira"],
            ["BTS", "btsarmy"],
            ["BLACKPINK", "blackpink"],
            ["Gorillaz", "gorillaz"],
            ["Coldplay", "coldplay"],
            ["Imagine Dragons", "imagined"],
            ["One Direction", "onedirection"],
            ["Paramore", "paramore"],
            ["Twenty One Pilots", "topilots"],
            ["Fall Out Boy", "foboy"],
            ["Arctic Monkeys", "arcticm"],
            ["Florence + The Machine", "florencem"],
            ["The 1975", "the1975"],
            ["Green Day", "greenday"],
            ["My Chemical Romance", "mcr"],
            ["Panic! At The Disco", "panicd"],
            ["Red Hot Chili Peppers", "rhcp"],
            ["Linkin Park", "linkinp"],
            ["Pearl Jam", "pearljam"],
            ["Nirvana", "nirvana"],
            ["The Beatles", "beatles"],
            ["The Rolling Stones", "rollingst"],
            ["Fleetwood Mac", "fleetmac"],
            ["Elton John", "eltonj"],
            ["David Bowie", "davidb"],
            ["Prince", "prince"],
            ["Bob Dylan", "bobdylan"],
            ["Queen", "queenband"],
            ["Madonna", "madonna"],
            ["Stevie Wonder", "steview"],
            ["Aretha Franklin", "arethaf"],
            ["Whitney Houston", "whitneyh"],
            ["Marvin Gaye", "marving"],
            ["John Legend", "johnl"],
            ["Paul McCartney", "paulmcc"],
            ["Johnny Cash", "johnnyc"],
            ["Elvis Presley", "elvisp"]
        ],

        "huge_numerical.csv": [
            ["ID", "Age", "Height_cm", "Weight_kg", "Hourly_Wage_USD", "Score", "Experience_yrs"],
            ["0", "25", "170", "65", "26.44", "85", "2"],
            ["1", "30", "175", "78", "34.62", "90", "5"],
            ["2", "22", "160", "55", "20.67", "75", "1"],
            ["3", "35", "180", "85", "45.67", "95", "10"],
            ["4", "28", "165", "60", "28.85", "80", "3"],
            ["5", "40", "172", "82", "50.48", "88", "15"],
            ["6", "33", "178", "90", "42.31", "92", "7"],
            ["7", "27", "169", "70", "32.21", "78", "4"],
            ["8", "31", "182", "95", "44.23", "89", "6"],
            ["9", "29", "175", "77", "35.58", "86", "4"],
            ["10", "26", "168", "62", "27.88", "79", "3"],
            ["11", "38", "181", "88", "47.12", "91", "12"],
            ["12", "24", "163", "58", "24.04", "76", "2"],
            ["13", "36", "176", "84", "43.27", "94", "9"],
            ["14", "32", "171", "73", "39.42", "87", "5"],
            ["15", "45", "185", "100", "57.69", "96", "20"],
            ["16", "23", "162", "57", "22.60", "74", "1"],
            ["17", "41", "179", "92", "49.04", "89", "16"],
            ["18", "34", "174", "80", "41.83", "90", "8"],
            ["19", "37", "170", "78", "43.75", "93", "10"]
        ]
};

export const datasetFiles = ["washington_counties.csv", "texas_counties.csv", "headerless.csv"]