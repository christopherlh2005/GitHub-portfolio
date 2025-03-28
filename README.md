# Chris H GitHub Portfolio

![Welcome dog](https://github.com/user-attachments/assets/4c8c9d0e-6c30-4299-81b0-bec91c39540b)

Here is a collection of my favorite personal projects. 

### There are more detailed READMEs in each folder. Additionally, both projects have video overviews (both under 3 mins) showing you the projects in action and explaining the code quickly.

## Depression Classifier 

This project is a way to fight the mental health crisis by identifying people with depression based on their social media posts. It is trained on 2 million Reddit posts (a mixture of actual posts showing depression and control group posts). The classifier uses LDA (Latent Dirichlet allocation) and roBERTa embeddings to train a RandomForestClassifier to classify specific posts as either depression related or not. The program achieves approximately 85% accuracy (ignore LDA accuracy). I coded it using the pyTorch, sklearn, and Hugging Face transformers libraries.


Video:
https://drive.google.com/file/d/10zwUojxGFCxgMex6iIcikIJsa3poh8Ff/view?usp=sharing

## Broadband Search Full Stack App

I have long been committed to helping expand broadband (high-speed internet) access since I noticed vast disparities in my public school during the pandemic. It's crucial to have internet access to function in the 21st Century. I've researched to expand broadband access with my state representative, but I also wanted to have a program where you could see broadband accessibility nationwide to highlight the problem.

The application allows the user to search the Census using our API by entering the state and county to get the
corresponding broadband percentage. We additionally designed the application to enable mock csv files to be loaded in the backend server, and then you can view the data in tabular or bar graph form and search the csv file for keywords.

Additionally, this application is password protected and has aria labels that allow blind/visually impaired people to use it via a screenreader. 

Video: https://drive.google.com/file/d/1RZmLy6_XNXK29ZUVbN4IiXJhSk9YnHmc/view?usp=sharing
