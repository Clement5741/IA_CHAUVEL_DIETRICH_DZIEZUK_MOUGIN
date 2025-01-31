package mlp.mnist;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class MainCourbes {

    public static void main(String[] args) throws IOException {
        // Charger les données d'entraînement et de test
        Donnees donneesEntrainement = Donnees.loadData("./donnees/train-images.idx3-ubyte", "./donnees/train-labels.idx1-ubyte");
        Donnees donneesTest = Donnees.loadData("./donnees/t10k-images.idx3-ubyte", "./donnees/t10k-labels.idx1-ubyte");

        // Configurations des tests
        int[][] configurationsCouches = {
                {784, 64, 10},
                {784, 128, 64, 10},
                {784, 128, 128, 64, 10},
                {784, 256, 128, 10}
        };
        double[] tauxApprentissageInitials = {0.1, 0.6, 1.0};
        int maxEpochs = 1000;
        double erreurCible = 0.01;
        boolean[] melanger = {true, false};

        String fonctionActivation = "tanh";

        for (int[] configurationMLP : configurationsCouches) {
            System.out.printf("\nConfiguration des couches : %s%n", Arrays.toString(configurationMLP));

            for (double tauxApprentissage : tauxApprentissageInitials) {
                System.out.printf("Taux d’apprentissage : %.2f%n", tauxApprentissage);

                for (boolean melange : melanger) {
                    System.out.printf("Mélanger les données : %b%n", melange);
                    if (melange) {
                        List<Imagette> imagettes = new ArrayList<>(List.of(donneesEntrainement.getImagettes()));
                        Collections.shuffle(imagettes);
                        donneesEntrainement = new Donnees(imagettes.toArray(new Imagette[0]));
                    }

                    // MLP
                    AdaptateurMLP mlp = new AdaptateurMLP(donneesEntrainement, configurationMLP, tauxApprentissage, fonctionActivation);
                    System.out.println("Entraînement...");
                    XYSeries[] courbes = mlp.entrainer(erreurCible, maxEpochs);

                    //Knn
                    kNN knn = new kNN(donneesEntrainement, 3);

                    // Résultats finaux
                    double precisionFinaleMLP = testerAlgo(mlp, donneesTest);
                    XYSeries courbePrecisionKnn = new XYSeries("Précision kNN");
                    double precisionKnn = testerAlgo(knn, donneesTest);
                    for (int epoch = 0; epoch < courbes[0].getItemCount(); epoch++) {
                        courbePrecisionKnn.add(epoch, precisionKnn);
                    }
                    System.out.printf("Précision finale MLP : %.2f%%%n", precisionFinaleMLP);
                    System.out.printf("Précision finale kNN : %.2f%%%n", precisionKnn);

                    // Tracer les graphiques
                    XYSeriesCollection datasetPrecision = new XYSeriesCollection();
                    datasetPrecision.addSeries(courbes[1]);
                    datasetPrecision.addSeries(courbePrecisionKnn);
                    tracerCourbes(datasetPrecision, "Précision MLP vs kNN", "Époques", "Précision (%)", "Précision MLP vs kNN");

                    XYSeriesCollection datasetErreur = new XYSeriesCollection();
                    datasetErreur.addSeries(courbes[0]);
                    tracerCourbes(datasetErreur, "Erreur Moyenne MLP", "Époques", "Erreur Moyenne", "Erreur Moyenne MLP");
                }
            }
        }
    }

    private static double testerAlgo(AlgoClassification algo, Donnees donnees) {
        int correct = 0;
        for (Imagette img : donnees.getImagettes()) {
            int prediction = algo.predire(img);
            if (prediction == img.getLabel()) {
                correct++;
            }
        }
        return (correct / (double) donnees.getImagettes().length) * 100;
    }

    private static void tracerCourbes(XYSeriesCollection dataset, String titre, String axeX, String axeY, String label) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                titre,
                axeX,
                axeY,
                dataset
        );

        JFrame frame = new JFrame(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }

}
