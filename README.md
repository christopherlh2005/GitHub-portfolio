# Chris H GitHub Portfolio

![dog welcome 2](https://github.com/user-attachments/assets/e7bf38a4-db9d-49e9-9dea-3d4009eece46)

Here is a collection of my favorite personal projects. Neither project was directly coded on this repo. They were coded on other repos in my GitHub, but I am unable to share my entire GitHub since it has university assignments (against university policy).

### There are more detailed READMEs in each folder. Additionally, both projects have video overviews (both under 3 mins) showing you the projects in action and explaining the code quickly.

## For more details, click each project's respective folder.


## Depression Classifier - Personal Project

This project is a way to fight the mental health crisis by identifying people with depression based on their social media posts. It is trained on 2 million Reddit posts (a mixture of actual posts showing depression and control group posts). The classifier uses LDA (Latent Dirichlet allocation) and roBERTa embeddings to train a RandomForestClassifier to classify specific posts as either depression related or not. The program achieves approximately 85% accuracy (ignore LDA accuracy). I coded it using the pyTorch, sklearn, and Hugging Face transformers libraries.


Video:
https://drive.google.com/file/d/10zwUojxGFCxgMex6iIcikIJsa3poh8Ff/view?usp=sharing


## Broadband Search Full Stack App - Team Project

I have long been committed to helping expand broadband (high-speed internet) access since I noticed vast disparities in my public school during the pandemic. It's crucial to have internet access to function in the 21st Century. I've researched to expand broadband access with my state representative, but I also wanted to have a program where you could see broadband accessibility nationwide to highlight the problem.

The application allows the user to search the Census using our API by entering the state and county to get the
corresponding broadband percentage. We additionally designed the application to enable mock csv files to be loaded in the backend server, and then you can view the data in tabular or bar graph form and search the csv file for keywords.

Additionally, this application is password protected and has aria labels that allow blind/visually impaired people to use it via a screenreader. 

The application backend uses Java (with JUnit tests), and the frontend uses Typescript, CSS, and HTML (with Playwright tests). It was developed through agile development via sprints.

Video: https://drive.google.com/file/d/1RZmLy6_XNXK29ZUVbN4IiXJhSk9YnHmc/view?usp=sharing
