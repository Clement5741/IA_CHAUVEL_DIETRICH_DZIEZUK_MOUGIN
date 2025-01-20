package mlp;

import java.util.Arrays;

public class MLPTest {

    public static void main(String[] args) {
        // Paramètres par défaut
        String functionType = "sigmoid";
        String tableType = "xor";
        double learningRate = 0.1;

        // Récupérer les arguments
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

        // Sélectionner la table de vérité
        double[][] outputData;
        switch (tableType) {
            case "and":
                outputData = outputAnd;
                break;
            case "or":
                outputData = outputOr;
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

        // Configuration du réseau
        int[] layers = {2, 2, 1};

        // Lancer le test
        System.out.println("Test avec la fonction : " + functionType + ", Table : " + tableType + ", Taux : " + learningRate);
        testMLP(inputData, outputData, layers, learningRate, function);
    }

    private static void testMLP(double[][] inputData, double[][] outputData, int[] layers, double learningRate, TransferFunction function) {
        // Initialiser le perceptron
        MLP mlp = new MLP(layers, learningRate, function);

        // Configuration de l'apprentissage
        int maxIterations = 10000;
        double errorThreshold = 0.01;

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            double totalError = 0.0;

            // Mélanger les données d'entrée
            for (int i = 0; i < inputData.length; i++) {
                int index = (int) (Math.random() * inputData.length);
                double[] input = inputData[index];
                double[] output = outputData[index];

                // Apprentissage par rétropropagation
                totalError += mlp.backPropagate(input, output);
            }

            // Afficher l'erreur moyenne périodiquement
            if (iteration % 100 == 0) {
                System.out.println("Itération " + iteration + ", Erreur moyenne : " + (totalError / inputData.length));
            }

            // Vérifier si l'apprentissage est terminé
            if (totalError / inputData.length < errorThreshold) {
                System.out.println("Apprentissage terminé après " + iteration + " itérations.");
                break;
            }
        }

        // Tester les performances
        System.out.println("\nRésultats après apprentissage :");
        for (int i = 0; i < inputData.length; i++) {
            double[] input = inputData[i];
            double[] predictedOutput = mlp.execute(input);
            System.out.println("Entrée : " + Arrays.toString(input) +
                    " -> Sortie prédite : " + Arrays.toString(predictedOutput));
        }
    }
}