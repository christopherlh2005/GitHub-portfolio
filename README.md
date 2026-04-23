# Chris H GitHub Portfolio

![dog welcome 2](https://github.com/user-attachments/assets/e7bf38a4-db9d-49e9-9dea-3d4009eece46)

Here is a collection of my favorite personal projects. No project was directly coded on this repo. They were coded on other repos in my GitHub, but I am unable to share my entire GitHub since it has university assignments (against university policy).

### There are more detailed READMEs in each folder. Additionally, some projects have video overviews (under 3 mins) showing you the projects in action and explaining the code quickly.

## For more details, click each project's respective folder.


## Depression Classifier - Personal Project

This project is a way to fight the mental health crisis by identifying people with depression based on their social media posts. It is trained on 2 million Reddit posts (a mixture of actual posts showing depression and control group posts). The classifier uses LDA (Latent Dirichlet allocation) and roBERTa embeddings to train a RandomForestClassifier to classify specific posts as either depression related or not. The program achieves approximately 85% accuracy (ignore LDA accuracy). I coded it using the pyTorch, sklearn, and Hugging Face transformers libraries.


[Video](https://drive.google.com/file/d/10zwUojxGFCxgMex6iIcikIJsa3poh8Ff/view?usp=sharing)

For More Details: Watch the video (about 2 mins long) and click on the subfolder to access the more detailed README and code.


## Generating Novel Molecules with a Sequential VAE

This group project uses Variational Autoencoders to generate novel molecules made up of carbon, hydrogen, oxygen, nitrogen, and fluorine atoms. Our goal was twofold: generate chemically stable molecules that follow fundamental laws of physics and chemistry, and generate truly novel molecules not found in existing datasets. Our model achieves 98% validity and 65% novelty. 

We use a Sequential VAE with a bidirectional GRU encoder and an autoregressive GRU decoder operating on SMILES strings. The model was trained on the QM9 dataset (135k small stable molecules) using a combined cross-entropy and KL loss with an Adam optimizer.

For More Details: View this [project poster](molecule-generation/Poster.pdf) and click on the subfolder to access the more detailed README and code.
