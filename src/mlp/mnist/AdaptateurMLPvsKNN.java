package mlp.mnist;

import mlp.MLP;
import mlp.SigmoidFunction;

public class AdaptateurMLPvsKNN extends AlgoClassification {
    private MLP mlp;
    private kNN knn;
    private Donnees donneesTest;

    public AdaptateurMLPvsKNN(Donnees donneesEntrainement, Donnees donneesTest, int[] couchesMLP, double tauxApprentissage, int k) {
        super(donneesEntrainement);
        this.mlp = new MLP(couchesMLP, tauxApprentissage, new SigmoidFunction());
        this.knn = new kNN(donneesEntrainement, k);
        this.donneesTest = donneesTest;
    }

    @Override
    public int predire(Imagette imagette) {
        double[] entree = convertToDoubleArray(imagette.getPixels());
        double[] sortie = mlp.execute(entree);
        return getPredictionFromOutput(sortie);
    }

    public void entrainer(int maxEpochs) {
        Imagette[] imagesEntrainement = donnees.getImagettes();

        for (int epoch = 0; epoch < maxEpochs; epoch++) {
            // Phase d'entraînement
            double erreurTotale = 0.0;
            for (Imagette img : imagesEntrainement) {
                double[] entree = convertToDoubleArray(img.getPixels());
                double[] sortieDesiree = oneHotEncode(img.getLabel());
                erreurTotale += mlp.backPropagate(entree, sortieDesiree);
            }

            // Évaluer sur les données d'apprentissage
            double precisionApprentissage = testerSurDonnees(donnees);

            // Évaluer sur les données de test
            double precisionTest = testerSurDonnees(donneesTest);

            // Afficher les résultats intermédiaires
            System.out.printf("Époque %d - Erreur moyenne : %.5f - Précision (apprentissage) : %.2f%% - Précision (test) : %.2f%%%n",
                    epoch + 1, erreurTotale / imagesEntrainement.length, precisionApprentissage, precisionTest);
        }
    }

    public double testerMLP() {
        return testerSurDonnees(donneesTest);
    }

    public double testerKNN() {
        int correct = 0;
        for (Imagette img : donneesTest.getImagettes()) {
            int prediction = knn.predire(img);
            if (prediction == img.getLabel()) {
                correct++;
            }
        }
        return (correct / (double) donneesTest.getImagettes().length) * 100;
    }

    private double testerSurDonnees(Donnees donnees) {
        int correct = 0;
        for (Imagette img : donnees.getImagettes()) {
            double[] entree = convertToDoubleArray(img.getPixels());
            double[] sortie = mlp.execute(entree);
            int prediction = getPredictionFromOutput(sortie);
            if (prediction == img.getLabel()) {
                correct++;
            }
        }
        return (correct / (double) donnees.getImagettes().length) * 100;
    }

    private double[] convertToDoubleArray(int[][] pixels) {
        int rows = pixels.length;
        int cols = pixels[0].length;
        double[] result = new double[rows * cols];
        int index = 0;
        for (int[] row : pixels) {
            for (int pixel : row) {
                result[index++] = pixel / 255.0; // Normaliser entre 0 et 1
            }
        }
        return result;
    }

    private double[] oneHotEncode(int label) {
        double[] result = new double[10];
        result[label] = 1.0;
        return result;
    }

    private int getPredictionFromOutput(double[] output) {
        int index = 0;
        for (int i = 1; i < output.length; i++) {
            if (output[i] > output[index]) {
                index = i;
            }
        }
        return index;
    }
}
