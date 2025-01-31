package mlp.mnist;

import mlp.MLP;
import mlp.TanhFunction;

import java.util.Arrays;
import java.util.stream.IntStream;

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
            // Chargement des données
            System.out.println("Chargement des données...");
            Donnees donneesEntrainement = Donnees.loadData(IMAGES_TRAIN, ETIQUETTES_TRAIN);
            Donnees donneesTest = Donnees.loadData(IMAGES_TEST, ETIQUETTES_TEST);
            System.out.println("Données chargées avec succès !\n");

            // Configurations des tests
            int[][] configurationsCouches = {
                    {784, 10},
                    {784, 64, 10},
                    {784, 128, 64, 10},
                    {784, 128, 128, 64, 10},
            };
            double[] tauxApprentissageInitials = {0.01, 0.1, 0.5};
            double erreurCible = 0.01;
            boolean[] melanger = {true, false};

            for (int[] configuration : configurationsCouches) {
                System.out.println(Arrays.toString(configuration));
                for (double tauxApprentissage : tauxApprentissageInitials) {
                    for (boolean melange : melanger) {
//                        if (melange) {
//                            Arrays.stream(donneesEntrainement.getImagettes()).forEach(imagette -> {
//                                double[] entrees = aplatirEtNormaliser(imagette.getPixels());
//                                double[] sortiesDesirees = new double[10];
//                                sortiesDesirees[imagette.getLabel()] = 1;
//                                perceptron.backPropagate(entrees, sortiesDesirees);
//                            });
//                        }

                        MLP perceptron = new MLP(configuration, tauxApprentissage, new TanhFunction());
                        long startTime = System.currentTimeMillis();
                        double precision = entrainerEtEvaluer(perceptron, donneesEntrainement, donneesTest);
                        long endTime = System.currentTimeMillis();
                        System.out.printf("Précision : %.2f%% - Temps : %d ms\n", precision, (endTime - startTime));
                    }
                }
            }

            // Test des différentes configurations
//            testerNombreDeNeurones(donneesEntrainement, donneesTest);
//            testerNombreDeCouches(donneesEntrainement, donneesTest);
//            testerTauxApprentissage(donneesEntrainement, donneesTest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * Teste l'influence du nombre de neurones cachés.
//     */
//    private static void testerNombreDeNeurones(Donnees donneesTrain, Donnees donneesTest) {
//        System.out.println("Test du nombre de neurones cachés");
//
//        int[] nombreNeuronesOptions = {9, 50, 100, 250, 500, 1000};
//        for (int nombreNeurones : nombreNeuronesOptions) {
//            int[] couches = {784, nombreNeurones, 10};
//            MLP perceptron = new MLP(couches, 0.1, new TanhFunction());
//
//            long startTime = System.currentTimeMillis();
//            double precision = entrainerEtEvaluer(perceptron, donneesTrain, donneesTest);
//            long endTime = System.currentTimeMillis();
//
//            System.out.printf("Neurones cachés : %d - Précision : %.2f%% - Temps : %d ms\n", nombreNeurones, precision, (endTime - startTime));
//        }
//    }
//
//    /**
//     * Teste l'influence du nombre de couches cachées.
//     */
//    private static void testerNombreDeCouches(Donnees donneesTrain, Donnees donneesTest) {
//        System.out.println("Test du nombre de couches cachées");
//
//
//
//        for (int[] couches : configurationsCouches) {
//            MLP perceptron = new MLP(couches, 0.1, new TanhFunction());
//
//            long startTime = System.currentTimeMillis();
//            double precision = entrainerEtEvaluer(perceptron, donneesTrain, donneesTest);
//            long endTime = System.currentTimeMillis();
//
//            System.out.printf("Couches : %s - Précision : %.2f%% - Temps : %d ms\n", Arrays.toString(couches), precision, (endTime - startTime));
//        }
//    }
//
//    /**
//     * Teste l'influence du taux d'apprentissage.
//     */
//    private static void testerTauxApprentissage(Donnees donneesTrain, Donnees donneesTest) {
//        System.out.println("Test du taux d'apprentissage");
//
//
//
//        for (double tauxApprentissage : tauxApprentissageOptions) {
//            int[] couches = {784, 100, 10};
//            MLP perceptron = new MLP(couches, tauxApprentissage, new TanhFunction());
//
//            long startTime = System.currentTimeMillis();
//            double precision = entrainerEtEvaluer(perceptron, donneesTrain, donneesTest);
//            long endTime = System.currentTimeMillis();
//
//            System.out.printf("Taux d'apprentissage : %.2f - Précision : %.2f%% - Temps : %d ms\n", tauxApprentissage, precision, (endTime - startTime));
//        }
//    }

    /**
     * Entraîne et évalue un réseau de neurones.
     */
    private static double entrainerEtEvaluer(MLP perceptron, Donnees donneesTrain, Donnees donneesTest) {
        Arrays.stream(donneesTrain.getImagettes()).forEach(imagette -> {
            double[] entrees = aplatirEtNormaliser(imagette.getPixels());
            double[] sortiesDesirees = new double[10];
            sortiesDesirees[imagette.getLabel()] = 1;
            perceptron.backPropagate(entrees, sortiesDesirees);
        });

        long predictionsCorrectes = Arrays.stream(donneesTest.getImagettes())
                .filter(imagette -> {
                    double[] entrees = aplatirEtNormaliser(imagette.getPixels());
                    double[] sortiesPredites = perceptron.execute(entrees);
                    int etiquettePredite = obtenirEtiquetteDepuisSortie(sortiesPredites);
                    return etiquettePredite == imagette.getLabel();
                })
                .count();

        return (double) predictionsCorrectes / donneesTest.getImagettes().length * 100;
    }



    private static double[] aplatirEtNormaliser(int[][] pixels) {
        return Arrays.stream(pixels).flatMapToInt(Arrays::stream).mapToDouble(val -> val / 255.0).toArray();
    }

    private static int obtenirEtiquetteDepuisSortie(double[] sorties) {
        return IntStream.range(0, sorties.length).reduce((i, j) -> sorties[i] > sorties[j] ? i : j).orElse(0);
    }
}
