package mlp.mnist;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // Charger les données d'entraînement et de test
        Donnees donneesEntrainement = Donnees.loadData("./donnees/train-images.idx3-ubyte", "./donnees/train-labels.idx1-ubyte", 1000);
        Donnees donneesTest = Donnees.loadData("./donnees/t10k-images.idx3-ubyte", "./donnees/t10k-labels.idx1-ubyte", 500);

        // Créer une instance de KNN
        int k = 5; // Nombre de voisins pour KNN
        kNN knn = new kNN(donneesEntrainement, k);

        // Créer une instance de MLP via l'adaptateur
        int[] configurationMLP = {784, 64, 10};
        double tauxApprentissage = 0.6;
        double erreurCible = 0.01;
        String fonctionActivation = "sigmoid"; // Peut être "sigmoid" ou "tanh"

        AdaptateurMLP mlp = new AdaptateurMLP(donneesEntrainement, configurationMLP, tauxApprentissage, fonctionActivation);

        // Entraîner le MLP
        System.out.println("\nEntraînement du MLP...");
        mlp.entrainer(erreurCible, 1000);

        // Tester les performances des deux algorithmes
        System.out.println("\nTest des performances finales :");
        double precisionMLP = testerAlgo(mlp, donneesTest);
        double precisionKNN = testerAlgo(knn, donneesTest);

        System.out.printf("Précision finale MLP : %.2f%%%n", precisionMLP);
        System.out.printf("Précision finale KNN : %.2f%%%n", precisionKNN);
    }

    private static double testerAlgo(AlgoClassification algo, Donnees donnees) {
        int correct = 0;
        for (Imagette img : donnees.getImagettes()) {
            int prediction = algo.predire(img);
            if (prediction == img.getLabel()) {
                correct++;
            }
        }
        return (correct / (double) donnees.getImagettes().length) * 100;
    }
}
