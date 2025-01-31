package mlp.mnist;

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
            // Charger les données d'entraînement et de test
            System.out.println("Chargement des données...");
            Donnees donneesEntrainement = Donnees.loadData(IMAGES_TRAIN, ETIQUETTES_TRAIN);
            Donnees donneesTest = Donnees.loadData(IMAGES_TEST, ETIQUETTES_TEST);
            System.out.println("Données chargées avec succès !\n");

            // kNN
            kNN knn = new kNN(donneesEntrainement, 10);

            // Résultats finaux
            System.out.println("Test kNN...");
            long startTime = System.currentTimeMillis();
            Statistiques stats = new Statistiques(knn);
            double precisionFinaleKnn = stats.calculerPourcentageCorrect(donneesTest,"./resultats/knn.csv");
            long endTime = System.currentTimeMillis();
            System.out.printf("Précision finale kNN : %.2f%%%n", precisionFinaleKnn);
            System.out.printf("Temps d'exécution : %d ms%n", (endTime - startTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
