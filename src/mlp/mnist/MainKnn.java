package mlp.mnist;

import java.io.FileWriter;
import java.io.PrintWriter;

public class MainKnn {
    // Fichiers MNIST
    public static final String IMAGES_TRAIN = "./donnees/mnist/train-images.idx3-ubyte";
    public static final String ETIQUETTES_TRAIN = "./donnees/mnist/train-labels.idx1-ubyte";
    public static final String IMAGES_TEST = "./donnees/mnist/t10k-images.idx3-ubyte";
    public static final String ETIQUETTES_TEST = "./donnees/mnist/t10k-labels.idx1-ubyte";

    // Fichiers Fashion MNIST
//    public static final String IMAGES_TRAIN = "./donnees/fashion/train-images-idx3-ubyte";
//    public static final String ETIQUETTES_TRAIN = "./donnees/fashion/train-labels-idx1-ubyte";
//    public static final String IMAGES_TEST = "./donnees/fashion/t10k-images-idx3-ubyte";
//    public static final String ETIQUETTES_TEST = "./donnees/fashion/t10k-labels-idx1-ubyte";

    public static void main(String[] args) {
        try {
            String nomFichier = "./resultats/knn.csv";
            FileWriter fw = new FileWriter(nomFichier, true);
            PrintWriter writer = new PrintWriter(fw);
            String fichier = "mnist";
            writer.println("Fichiers " + fichier);

            // Charger les données d'entraînement et de test
            System.out.println("Chargement des données...");
            Donnees donneesEntrainement = Donnees.loadData(IMAGES_TRAIN, ETIQUETTES_TRAIN);
            Donnees donneesTest = Donnees.loadData(IMAGES_TEST, ETIQUETTES_TEST);
            System.out.println("Données chargées avec succès !\n");

            // kNN
            int[] k = {3, 5, 10};
            for (int kk : k) {
                writer.println("Images traitees;Precision;" + "k : " + kk);
                writer.flush();
                System.out.println("\n=====================");
                System.out.println("k : " + kk);
                System.out.println("=====================");
                kNN knn = new kNN(donneesEntrainement, kk);
                Statistiques stats = new Statistiques(knn);
                double precisionFinaleKnn = stats.calculerPourcentageCorrect(donneesTest, writer);
                System.out.printf("Précision finale kNN : %.1f%%%n", precisionFinaleKnn);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
