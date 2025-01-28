import ia.framework.common.ArgParse;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameEngine;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

import java.io.FileWriter;
import java.io.IOException;

public class TestLancerJeuxToCSV {

    public static void main(String[] args) {
        // Configurations pour les tests
        String[] games = {"tictactoe", "mnk"}; // Jeux a tester
        String[] playerTypes = {"minmax", "alphabeta", "random"}; // Algorithmes de joueurs
        int[] maxDepths = {3, 4, 5}; // Profondeurs maximales
        int[] sizes = {5, 6}; // Tailles du jeu (uniquement pour Mnk)
        String csvFile = "results_games.csv";

        try (FileWriter writer = new FileWriter(csvFile)) {
            // Ecrire l'entete
            writer.write("Jeu;Profondeur;Taille;Joueur 1;Joueur 2;Vainqueur;Temps (ms);Coups;Etats J1;Etats J2\n");

            for (String gameName : games) {
                for (int depth : maxDepths) {
                    if (gameName.equals("mnk")) {
                        for (int size : sizes) {
                            for (String p1Type : playerTypes) {
                                for (String p2Type : playerTypes) {
                                    // Preparer les arguments pour ArgParse
                                    String[] testArgs = {
                                            "-game", gameName,
                                            "-p1", p1Type,
                                            "-p2", p2Type,
                                            "-d", String.valueOf(depth),
                                            "-s", String.valueOf(size)
                                    };

                                    executeGame(writer, gameName, depth, size, p1Type, p2Type, testArgs);
                                }
                            }
                        }
                    } else {
                        for (String p1Type : playerTypes) {
                            for (String p2Type : playerTypes) {
                                // Preparer les arguments pour ArgParse
                                String[] testArgs = {
                                        "-game", gameName,
                                        "-p1", p1Type,
                                        "-p2", p2Type,
                                        "-d", String.valueOf(depth)
                                };

                                executeGame(writer, gameName, depth, -1, p1Type, p2Type, testArgs);
                            }
                        }
                    }
                }
            }

            System.out.println("Resultats ecrits dans le fichier CSV : " + csvFile);

        } catch (IOException e) {
            System.err.println("Erreur lors de l'ecriture du fichier CSV : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void executeGame(FileWriter writer, String gameName, int depth, int size, String p1Type, String p2Type, String[] testArgs) throws IOException {
        // Creer le jeu et les joueurs
        Game game = ArgParse.makeGame(gameName, testArgs);
        Player player1 = ArgParse.makePlayer(p1Type, game, true, testArgs);
        Player player2 = ArgParse.makePlayer(p2Type, game, false, testArgs);
        GameEngine gameEngine = new GameEngine(game, player1, player2);

        try {
            // Executer la partie
            long startTime = System.currentTimeMillis();
            GameState endGame = gameEngine.gameLoop();
            long endTime = System.currentTimeMillis();

            // Collecter les resultats
            Player winner = gameEngine.getWinner(endGame);
            String winnerName;
            if (winner == player1) {
                winnerName = "Joueur1";
            } else if (winner == player2) {
                winnerName = "Joueur2";
            } else {
                winnerName = "Draw";
            }
            long duration = endTime - startTime;
            int totalMoves = gameEngine.getTotalMoves();
            int statesExploredP1 = player1.getStateCounter();
            int statesExploredP2 = player2.getStateCounter();

            // Ecrire les resultats dans le fichier CSV
            writer.write(String.format("%s;%d;%d;%s;%s;%s;%f;%d;%d;%d\n",
                    gameName, depth, size, p1Type, p2Type, winnerName,
                    duration/1000., totalMoves, statesExploredP1, statesExploredP2));

        } catch (Exception e) {
            writer.write(String.format("%s;%d;%d;%s;%s;Erreur;%d;%d;%d;%d\n",
                    gameName, depth, size, p1Type, p2Type,
                    0, 0, 0, 0));
            e.printStackTrace();
        }
    }
}
