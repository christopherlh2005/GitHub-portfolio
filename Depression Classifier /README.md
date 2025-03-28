# Depression Classifier

## Overview:

This project was inspired as a way to fight the mental health crisis by identifying people with depression based on their social media posts. The classifier uses LDA and roBERTa embeddings to train a RandomForestClassifier to classify specific posts as either depression related or not. The program achieves an approximate 85% accuracy. I coded it using the pyTorch, sklearn, and Hugging Face transformers libraries. 

## Video: https://drive.google.com/file/d/1l-15OCQm-zCuZIRw7RoiQ96KWyomB0mf/view?usp=sharing

Code Overview: 

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

