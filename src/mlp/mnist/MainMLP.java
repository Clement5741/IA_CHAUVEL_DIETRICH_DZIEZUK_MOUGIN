package mlp.mnist;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainMLP {

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
            String nomFichier = "./resultats/mlp.csv";
            FileWriter fw = new FileWriter(nomFichier,true);
            PrintWriter writer = new PrintWriter(fw);
            String fichier = "mnist";

            // Chargement des données
            System.out.println("Chargement des données...");
            Donnees donneesEntrainement = Donnees.loadData(IMAGES_TRAIN, ETIQUETTES_TRAIN,1000);
            Donnees donneesTest = Donnees.loadData(IMAGES_TEST, ETIQUETTES_TEST,1000);
            System.out.println("Données chargées avec succès !\n");

            // Configurations des tests
            int[][] configurationsCouches = {
                    {784, 10},
                    {784, 64, 10},
                    {784, 256, 10},
                    {784, 128, 64, 10},
                    {784, 256, 128, 10},
                    {784, 128, 128, 64, 10},
                    {784, 256, 256, 128, 10},
            };
            double[] tauxApprentissageInitials = {0.01, 0.1, 0.5};
            boolean[] melanger = {true, false};
            String fonctionActivation = "tanh";

            writer.println("Fichiers " + fichier +" + "+fonctionActivation);
            writer.flush();

            for (int[] configuration : configurationsCouches) {
                for (double tauxApprentissage : tauxApprentissageInitials) {
                    for (boolean melange : melanger) {
                        writer.println("Images traitees;Precision;" + "Configuration : " + Arrays.toString(configuration) +
                                " | Taux Apprentissage : " + tauxApprentissage +
                                " | Melange : " + melange);
                        writer.flush();
                        System.out.println("\n=====================");
                        System.out.println("Configuration : " + Arrays.toString(configuration) +
                                " | Taux Apprentissage : " + tauxApprentissage +
                                " | Mélangé : " + melange);
                        System.out.println("=====================");
                        Donnees donneesTrain = melange ? melangerDonnees(donneesEntrainement) : donneesEntrainement;

                        // Initialisation du MLP
                        AdaptateurMLP mlp = new AdaptateurMLP(donneesTrain, configuration, tauxApprentissage, fonctionActivation);

                        mlp.entrainer(0.01, 10);

                        Statistiques stats = new Statistiques(mlp);
                        stats.calculerPourcentageCorrect(donneesTest, writer);
                    }
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Donnees melangerDonnees(Donnees donnees) {
        List<Imagette> listeImagettes = Arrays.asList(donnees.getImagettes());
        Collections.shuffle(listeImagettes);
        return new Donnees(listeImagettes.toArray(new Imagette[0]));
    }
}
