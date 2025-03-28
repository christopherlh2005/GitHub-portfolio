# Project Details
- Project Name: Home Appraisal Application
- Project Description: N/A (not final project)
- Team Members: clho, gwang71
- Time Spent: ~16 Hours
- GitHub Repo: https://github.com/cs0320-s25/repl-clho-gwang71

We designed an application that allows the user to load csv files onto
the backend server, and then view the data in tabular or bar graph form
and search a selected csv file for keywords. The application also allows
the user to search the Census API using state and county to get the
corresponding broadband percentage.


https://clerk.com/docs/hooks/use-user

# Design Choices
4.1: 
We used our backend server endpoints loadcsv, viewcsv, and searchcsv
to connect the frontend to the backend. We use the UseEffect hook to 
detect changes in the dropdown menu, which contains different csv
file names. When a change is detected, we upload that csv to the server.
To view the data in tabular or graph form, we load the csv and use the
returned json from viewcsv as the data to display.
We use asynchronous functions to make server calls.

We also designed our application to meet the 8 web accessibility criteria.
We did this through adequate labelling, having clear instructions, 
and following the other criteria that were outlined.

4.2: 
We are able to use our backend server endpoint broadband to access data from the census
API. Additionally, we are able to do user authentication to validate that only Brown University
emails are able to log in. We implemented authentication through a log-in screen in Clerk and 
implemented access to API data via searching for state and county data to directly query the 
broadband API to access broadband data.

# Errors/Bugs
n/a

# Tests
4.1:
For testing user story 0, we have one comprehensive test in Playwright which 
walks through the entire process of a user viewing and searching uploaded
CSV files. For testing user story 1, we walked through all web accessibility 
criteria and confirmed that all of them are met!

4.2:
For user story 0, we tested to ensure that the log-in screen exists and is functional. For user
story 1, we tested to ensure that API response data that displays is what we expect. 

# How to
- **Run Tests**: You test the frontend by running the testing suite via Playwright. 
You can test the backend server by running the testing suite via Postman.
- **Build and Run This Program**: You can build and run this program by starting the server. 
Once you've reached the website, follow the instructions to interact with the application!

# Collaboration
CS Logins: ncphan, akuma143

Online Sources: livecode example, EdSTEM, StackOverflow

Generative AI: “Can you generate mocked JSON string data for a backend server application that includes state-county and broadband percentage?” prompt. ChatGPT, Nov. version, OpenAI, 13 Mar. 2025, chat.openai.com/chat.

We used this prompt as part of the AI workflow exercise.
