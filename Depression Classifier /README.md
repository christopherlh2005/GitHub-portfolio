# Depression Classifier

![istockphoto-2148445361-612x612-2](https://github.com/user-attachments/assets/b6da1f54-2e7e-47a3-aa8a-c127bbd54fba)

## Overview:

This project is a way to fight the mental health crisis by identifying people with depression based on their social media posts. It is trained on 2 million Reddit posts (a mixture of actual posts showing depression and control group posts). The classifier uses LDA (Latent Dirichlet allocation) and roBERTa embeddings to train a RandomForestClassifier to classify specific posts as either depression related or not. The program achieves approximately 85% accuracy (ignore LDA accuracy). I coded it using the pyTorch, sklearn, and Hugging Face transformers libraries. 

## How to View the Program in Action:

Due to the need for GPU resources, it is difficult to run the program natively. This program was originally created in Google Colab. To view the final output accuracies (broken down by specific depression symptoms), go to the bottom of the .ipynb file. You can view the code in the .py file. To see the program broken down in detail, watch the video linked below. 

## Video: 

https://drive.google.com/file/d/10zwUojxGFCxgMex6iIcikIJsa3poh8Ff/view?usp=sharing

## Code Overview: 

First, my load() function will generate different datasets depending on whether it's intended for lda or roBERTa. It will not preprocess (tokenize and stop word remove) for roBERTa.
However, both datasets both share the common factor that they are one dataframe with all the symptom and control related posts. Note that the col "symptom" in this dataframe is a bad description. 
Really the symptom col is telling you the subreddit correlating to the post (if its depression) or just "Control" (for Control posts).

My dataset_generation() is unique in that it returns a dictionary. For all the depression related posts, the key is the depression subreddit name correlating to the post and the value
is the list of all posts in that subreddit. For control posts, the key is "Control" and the value is all the control posts that fit the criteria. This dictionary is cast into a dataframe
in load().  

Next, I map (creating a new dataframe col) from the subreddit to the actual symptoms we care about in both run_roberta() and run_lda(). 

I won't go over my LDA or roBERTa embeddings in detail because I go over LDA in great detail in the video and roBERTa in the overview. However, I did print some function
outputs for "Anger" in LDA that I quickly rushed in my video. (I didn't print for all 10 symptoms because that would've made my outputs so hard to read). 

Lastly, my main() function is where the RandomForestClassifier() actually evaluates the posts. It's also where I call run_roberta() and run_lda() which are the methods that run 
roBERTa and LDA and actually produce the outputs in main() you see.

