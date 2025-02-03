package mlp.mnist;

import mlp.MLP;
import mlp.SigmoidFunction;
import mlp.TanhFunction;
import mlp.TransferFunction;
import org.jfree.data.xy.XYSeries;

public class AdaptateurMLP extends AlgoClassification {
    private MLP mlp;

    public AdaptateurMLP(Donnees donnees, int[] couchesMLP, double tauxApprentissage, String fonctionActivation) {
        super(donnees);

        // Configurer la fonction d'activation
        TransferFunction activationFunction = switch (fonctionActivation.toLowerCase()) {
            case "tanh" -> new TanhFunction();
            case "sigmoid" -> new SigmoidFunction();
            default -> throw new IllegalArgumentException("Fonction d'activation invalide : " + fonctionActivation);
        };

        this.mlp = new MLP(couchesMLP, tauxApprentissage, activationFunction);
    }

    @Override
    public int predire(Imagette imagette) {
        double[] entree = convertToDoubleArray(imagette.getPixels());
        double[] sortie = mlp.execute(entree);
        return getPredictionFromOutput(sortie);
    }

    public void entrainer(double erreurCible, int maxEpochs) {
        Imagette[] imagesEntrainement = donnees.getImagettes();
        System.out.println("Début de l'entraînement global...");

        double erreurTotale;
        for (int epoch = 0; epoch < maxEpochs; epoch++) {
            erreurTotale = 0.0;
            int correctPredictions = 0; // Ajout du suivi des bonnes prédictions

            for (Imagette img : imagesEntrainement) {
                double[] entree = convertToDoubleArray(img.getPixels());
                double[] sortieDesiree = oneHotEncode(img.getLabel());
                erreurTotale += mlp.backPropagate(entree, sortieDesiree);

                // Calcul de la précision après chaque epoch
                double[] sortie = mlp.execute(entree);
                int prediction = getPredictionFromOutput(sortie);
                if (prediction == img.getLabel()) {
                    correctPredictions++;
                }
            }

            double erreurMoyenne = erreurTotale / imagesEntrainement.length;
            double precision = (correctPredictions / (double) imagesEntrainement.length) * 100;

            if (epoch % 10 == 0) {
                System.out.printf("Époque %d : Erreur moyenne = %.5f | Précision = %.2f%%%n", epoch, erreurMoyenne, precision);
            }

            // Vérification de l’erreur cible
            if (erreurMoyenne <= erreurCible) {
                System.out.printf("Erreur cible atteinte à l'époque %d. Arrêt de l'entraînement.%n", epoch + 1);
                break;
            }
        }
        System.out.println("\nEntraînement terminé !");
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
