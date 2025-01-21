package mlp.mnist;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // Charger les données d'entraînement et de test
        Donnees donneesEntrainement = Donnees.loadData("./donnees/train-images.idx3-ubyte", "./donnees/train-labels.idx1-ubyte", 1000);
        Donnees donneesTest = Donnees.loadData("./donnees/t10k-images.idx3-ubyte", "./donnees/t10k-labels.idx1-ubyte", 500);

        // Créer l'adaptateur
        int[] configurationMLP = {784, 64, 10};
        double tauxApprentissage = 0.6;
        int k = 5; // Valeur de k pour KNN
        AdaptateurMLPvsKNN adaptateur = new AdaptateurMLPvsKNN(donneesEntrainement, donneesTest, configurationMLP, tauxApprentissage, k);

        // Entraîner le MLP
        System.out.println("Début de l'entraînement du MLP...");
        adaptateur.entrainer(10);

        // Évaluer les performances finales
        System.out.println("Test des performances finales :");
        double precisionFinaleMLP = adaptateur.testerMLP();
        double precisionFinaleKNN = adaptateur.testerKNN();

        System.out.printf("Précision finale MLP : %.2f%%%n", precisionFinaleMLP);
        System.out.printf("Précision finale KNN : %.2f%%%n", precisionFinaleKNN);
    }
}
