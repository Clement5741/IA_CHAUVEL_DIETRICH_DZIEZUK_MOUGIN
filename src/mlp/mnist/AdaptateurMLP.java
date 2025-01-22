package mlp.mnist;

import mlp.MLP;
import mlp.SigmoidFunction;

public class AdaptateurMLP extends AlgoClassification {
    private MLP mlp;

    public AdaptateurMLP(Donnees donnees, int[] couchesMLP, double tauxApprentissage) {
        super(donnees);
        this.mlp = new MLP(couchesMLP, tauxApprentissage, new SigmoidFunction());
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
            for (Imagette img : imagesEntrainement) {
                double[] entree = convertToDoubleArray(img.getPixels());
                double[] sortieDesiree = oneHotEncode(img.getLabel());
                mlp.backPropagate(entree, sortieDesiree);
            }
        }
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
