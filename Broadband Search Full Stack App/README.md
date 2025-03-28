# Broadband Search Full Stack App

## Project Details

- Project Name: Broadband Search Application
- Team Members: clho, gwang71

This application has 2 main functions. 

1. The application allows the user to search the Census using our API by entering the state and county to get the
corresponding broadband percentage. 

2. We additionally designed the application to allow mock csv files to be loaded in the backend server, and then view the data in tabular or bar graph form and search the csv file for keywords.

Additionally, this application is password protected and has aria labels that allow blind/visually impared people to use it via screenreader. 


## How to Access the File

Unfortunately, the application's use of password protection makes it quite impractical to have a live demo (given that you would need to have an authentication key on your computer). Given this, I filmed a video you can view demonstrating application functions. 


## Video 
https://drive.google.com/file/d/1RZmLy6_XNXK29ZUVbN4IiXJhSk9YnHmc/view?usp=sharing


## File Guide

clerk-react = authentication handling
client = the front-end with HTML, CSS, and Typescript as well as Playwright tests (under the tests subfolder)
data = mocked data to test backend
src = backend of the program with JUnit tests under the test subfolder

