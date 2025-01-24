package mlp.mnist;

import mlp.MLP;
import mlp.SigmoidFunction;
import mlp.TanhFunction;
import mlp.TransferFunction;

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

    public double entrainer(double erreurCible, int maxEpochs) {
        Imagette[] imagesEntrainement = donnees.getImagettes();
        double erreurTotale = 0.0;

        System.out.println("Début de l'entraînement global...");
        for (int epoch = 0; epoch < maxEpochs; epoch++) {
            erreurTotale = 0.0;

            for (Imagette img : imagesEntrainement) {
                double[] entree = convertToDoubleArray(img.getPixels());
                double[] sortieDesiree = oneHotEncode(img.getLabel());
                erreurTotale += mlp.backPropagate(entree, sortieDesiree);
            }

            double erreurMoyenne = erreurTotale / imagesEntrainement.length;

            // Afficher la progression globale
            afficherProgressionGlobale(epoch + 1, maxEpochs);

            // Vérifier si l'erreur cible est atteinte
            if (erreurMoyenne <= erreurCible) {
                System.out.printf(" - Erreur cible atteinte à l'époque %d. Arrêt de l'entraînement.%n", epoch + 1);
                return erreurMoyenne;
            }
        }

        System.out.println("\nEntraînement terminé !");
        return erreurTotale / imagesEntrainement.length;
    }


    private void afficherProgressionGlobale(int currentEpoch, int maxEpochs) {
        int largeurBarre = 30; // Largeur de la barre de progression
        int progress = (int) (largeurBarre * ((double) currentEpoch / maxEpochs));
        StringBuilder barre = new StringBuilder("[");
        for (int i = 0; i < largeurBarre; i++) {
            barre.append(i < progress ? "#" : " ");
        }
        barre.append("]");
        System.out.printf("\rProgression globale %s %d/%d", barre, currentEpoch, maxEpochs);
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
