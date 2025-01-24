import ia.framework.common.ArgParse;
import ia.framework.common.State;
import ia.framework.recherche.TreeSearch;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.SearchNode;

import java.io.FileWriter;
import java.io.IOException;

public class TestLancerRechercheToCSV {

    public static void main(String[] args) {
        // Configurations pour les tests
        int[] sizes = {100, 1_000, 5_000, 10_000}; // Tailles de graphe
        int[] branchingFactors = {3, 5, 7}; // Facteurs de branchement
        long[] seeds = {1234}; // Graines aléatoires
        String problemType = "dum"; // Problème utilisé : Dummy
        String[] algoNames = {"bfs", "dfs", "ucs", "gfs", "astar"}; // Algorithmes à tester

        // Fichier CSV de sortie
        String csvFile = "resultats_algo.csv";

        try (FileWriter writer = new FileWriter(csvFile)) {
            // Écrire l'en-tête
            writer.write("Problème;n;k;Graine;Algorithme;Solution;Temps (ms);Nodes Explored;Max Depth;Solution Cost\n");

            for (int size : sizes) {
                for (int k : branchingFactors) {
                    for (long seed : seeds) {
                        // Préparer les arguments pour ArgParse
                        String[] testArgs = {
                                "-prob", problemType,
                                "-n", String.valueOf(size),
                                "-k", String.valueOf(k),
                                "-r", String.valueOf(seed)
                        };

                        // Générer le problème
                        SearchProblem problem = ArgParse.makeProblem(problemType, testArgs);
                        State initialState = ArgParse.makeInitialState(problemType);

                        for (String algo : algoNames) {
                            // Instancier l'algorithme avec ArgParse
                            TreeSearch algorithm = ArgParse.makeAlgo(algo, problem, initialState);

                            try {
                                // Initialiser les compteurs
                                SearchNode.resetCount();

                                // Résoudre le problème
                                long startTime = System.currentTimeMillis();
                                boolean solved = algorithm.solve();
                                long endTime = System.currentTimeMillis();

                                // Récupérer les données
                                String solution = solved
                                        ? "\"" + String.join(" ", algorithm.getSolution().stream()
                                        .map(Object::toString)
                                        .toArray(String[]::new)) + "\""
                                        : "Pas de solution";

                                long executionTime = endTime - startTime;
                                int nodesExplored = SearchNode.getTotalSearchNodes();
                                int maxDepth = SearchNode.getMaxDepth();
                                double solutionCost = solved ? algorithm.getEndNode().getCost() : 0;

                                // Écrire une ligne dans le fichier CSV
                                writer.write(String.format(
                                        "%s;%d;%d;%d;%s;%s;%f;%d;%d;%.2f\n",
                                        problemType, size, k, seed, algo, solution,
                                        executionTime/1000., nodesExplored, maxDepth, solutionCost
                                ));
                            } catch (Exception e) {
                                writer.write(String.format(
                                        "%s;%d;%d;%d;%s;Erreur : %s;0;0;0;0.0\n",
                                        problemType, size, k, seed, algo, e.getMessage()
                                ));
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            System.out.println("Résultats écrits dans le fichier CSV : " + csvFile);

        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier CSV : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
