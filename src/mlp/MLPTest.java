package mlp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class MLPTest {

    /**
     * Ce programme teste un perceptron multicouche (MLP) sur les tables logiques AND, OR, XOR.
     *
     * Arguments possibles :
     * -func {sigmoid|tanh}  : Spécifie la fonction de transfert à utiliser.
     * -table {and|or|xor|andor}   : Spécifie la table logique à tester.
     * -rate {valeur}        : Définit le taux d'apprentissage.
     * -nb {valeur}          : Définit le nombre maximal d'itérations.
     * -layers {x,y,z,...}   : Spécifie les couches du réseau (exemple : 2,2,1).
     * -err {valeur}         : Définit l'erreur cible pour chaque exemple.
     */
    public static void main(String[] args) {
        // Paramètres par défaut
        String functionType = "sigmoid";
        String tableType = "and";
        double learningRate = 0.1;
        int maxIterations = 10000;
        int[] layers = {2, 2, 1};
        double errorTarget = 0.01;

        // Traiter les arguments de ligne de commande
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-func":
                    functionType = args[++i].toLowerCase();
                    break;
                case "-table":
                    tableType = args[++i].toLowerCase();
                    break;
                case "-rate":
                    learningRate = Double.parseDouble(args[++i]);
                    break;
                case "-nb":
                    maxIterations = Integer.parseInt(args[++i]);
                    break;
                case "-layers":
                    String[] layerParts = args[++i].split(",");
                    layers = new int[layerParts.length];
                    for (int j = 0; j < layerParts.length; j++) {
                        layers[j] = Integer.parseInt(layerParts[j]);
                    }
                    break;
                case "-err":
                    errorTarget = Double.parseDouble(args[++i]);
                    break;
                default:
                    System.out.println("Argument inconnu : " + args[i]);
                    break;
            }
        }

        // Définir les données d'apprentissage
        double[][] inputData = {
                {0, 0},
                {0, 1},
                {1, 0},
                {1, 1}
        };

        double[][] outputAnd = {{0}, {0}, {0}, {1}};
        double[][] outputOr = {{0}, {1}, {1}, {1}};
        double[][] outputXor = {{0}, {1}, {1}, {0}};
        double[][] outputAndOr = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};

        // Sélectionner la table logique
        double[][] outputData;
        switch (tableType) {
            case "and":
                outputData = outputAnd;
                break;
            case "or":
                outputData = outputOr;
                break;
            case "andor":
                outputData = outputAndOr;
                break;
            case "xor":
            default:
                outputData = outputXor;
                break;
        }

        // Sélectionner la fonction de transfert
        TransferFunction function;
        if (functionType.equals("tanh")) {
            function = new TanhFunction();
        } else {
            function = new SigmoidFunction();
        }

        // Lancer le test
        System.out.println("Test avec la fonction : " + functionType + ", Table : " + tableType + ", Taux : " + learningRate + ", Itérations : " + maxIterations + ", Erreur cible : " + errorTarget);
        testMLP(inputData, outputData, layers, learningRate, function, maxIterations, errorTarget);
    }

    private static void testMLP(double[][] inputData, double[][] outputData, int[] layers, double learningRate,
                                TransferFunction function, int maxIterations, double errorTarget) {
        // Initialiser le perceptron
        MLP mlp = new MLP(layers, learningRate, function);

        // Stocker les erreurs et sorties finales par ligne
        double[] finalErrors = new double[inputData.length];
        double[][] finalOutputs = new double[inputData.length][];

        // Mélanger les données
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < inputData.length; i++) {
            indices.add(i);
        }

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            boolean allBelowTarget = true;

            // Mélanger les indices à chaque itération
            Collections.shuffle(indices);

            for (int index : indices) {
                double[] input = inputData[index];
                double[] output = outputData[index];

                // Apprentissage par rétropropagation et mise à jour des erreurs finales
                double error = mlp.backPropagate(input, output);
                finalErrors[index] = error;
                finalOutputs[index] = mlp.execute(input); // Stocker la sortie finale

                // Vérifier si l'erreur dépasse le seuil cible
                if (error > errorTarget) {
                    allBelowTarget = false;
                }
            }

            // Vérifier si toutes les erreurs sont en dessous du seuil cible
            if (allBelowTarget) {
                System.out.println("Toutes les erreurs sont en dessous de " + errorTarget + " après " + iteration + " itérations.");
                break;
            }
        }

        // Afficher les résultats finaux pour chaque ligne
        System.out.println("\nRésultats après apprentissage :");
        for (int i = 0; i < inputData.length; i++) {
            double[] input = inputData[i];
            System.out.println("Entrée : " + Arrays.toString(input) +
                    " -> Sortie finale : " + Arrays.toString(finalOutputs[i]) +
                    ", Erreur finale : " + finalErrors[i]);
        }
    }
}
