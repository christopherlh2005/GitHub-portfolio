## Generating Novel Molecules with a Sequential VAE

This group project uses Variational Autoencoders to generate novel molecules made up of carbon, hydrogen, oxygen, nitrogen, and fluorine atoms. Our goal was twofold: generate chemically stable molecules that follow fundamental laws of physics and chemistry, and generate truly novel molecules not found in existing datasets. Our model achieves 98% validity and 65% novelty. 

We use a Sequential VAE with a bidirectional GRU encoder and an autoregressive GRU decoder operating on SMILES strings. The model was trained on the QM9 dataset (135k small stable molecules) using a combined cross-entropy and KL loss with an Adam optimizer.

For More Details: View this [project poster](molecule-generation/Poster.pdf) and click on the subfolder to access the more detailed README and code.
