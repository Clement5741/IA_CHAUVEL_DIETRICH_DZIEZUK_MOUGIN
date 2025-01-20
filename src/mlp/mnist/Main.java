package mlp.mnist;

import mlp.MLP;
import mlp.SigmoidFunction;
import mlp.TransferFunction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        // Charger les données
        Donnees donneesEntrainement = Donnees.loadData("./donnees/train-images.idx3-ubyte", "./donnees/train-labels.idx1-ubyte", 1000);
        Donnees donneesTest = Donnees.loadData("./donnees/t10k-images.idx3-ubyte", "./donnees/t10k-labels.idx1-ubyte", 500);

        // Configurations des tests
        int[][] configurationsCouches = {
                {784, 10},
                {784, 64, 10},
                {784, 128, 64, 10}
        };

        double[] tauxApprentissageInitials = {0.1, 0.6, 1.0};
        int maxEpochs = 10;
        boolean melangerDonnees = true;

        for (int[] configuration : configurationsCouches) {
            for (double tauxApprentissageInitial : tauxApprentissageInitials) {
                testerConfiguration(donneesEntrainement, donneesTest, configuration, tauxApprentissageInitial, maxEpochs, melangerDonnees);
            }
        }
    }

    private static void testerConfiguration(Donnees donneesEntrainement, Donnees donneesTest, int[] nombreNeuronesParCouches, double tauxApprentissage, int maxEpochs, boolean melangerDonnees) {
        TransferFunction fonctionTransfert = new SigmoidFunction();
        MLP reseau = new MLP(nombreNeuronesParCouches, tauxApprentissage, fonctionTransfert);

        List<Imagette> imagettesEntrainement = new ArrayList<>(List.of(donneesEntrainement.getImagettes()));
        if (melangerDonnees) {
            Collections.shuffle(imagettesEntrainement);
        }

        XYSeries serieErreur = new XYSeries("Erreur moyenne");
        XYSeries seriePrecision = new XYSeries("Précision sur le test");

        for (int epoch = 0; epoch < maxEpochs; epoch++) {
            double erreurTotale = 0.0;
            for (Imagette img : imagettesEntrainement) {
                double[] entree = convertToDoubleArray(img.getPixels());
                double[] sortieDesiree = oneHotEncode(img.getLabel());
                erreurTotale += reseau.backPropagate(entree, sortieDesiree);
            }

            double erreurMoyenne = erreurTotale / imagettesEntrainement.size();
            serieErreur.add(epoch + 1, erreurMoyenne);

            int correct = 0;
            for (Imagette img : donneesTest.getImagettes()) {
                double[] entree = convertToDoubleArray(img.getPixels());
                double[] sortie = reseau.execute(entree);
                int prediction = getPredictionFromOutput(sortie);
                if (prediction == img.getLabel()) {
                    correct++;
                }
            }

            double precision = (correct / (double) donneesTest.getImagettes().length) * 100;
            seriePrecision.add(epoch + 1, precision);

            System.out.printf("Epoch %d - Erreur moyenne : %.5f - Précision : %.2f%%%n", epoch + 1, erreurMoyenne, precision);
            tauxApprentissage *= 0.95;
            reseau.setLearningRate(tauxApprentissage);
        }

        afficherCourbes(serieErreur, seriePrecision, nombreNeuronesParCouches, tauxApprentissage);
    }

    private static void afficherCourbes(XYSeries serieErreur, XYSeries seriePrecision, int[] configuration, double tauxApprentissage) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(serieErreur);
        dataset.addSeries(seriePrecision);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Performance du MLP",
                "Epoch",
                "Valeur",
                dataset
        );

        JFrame frame = new JFrame("Courbes MLP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }

    private static double[] convertToDoubleArray(int[][] pixels) {
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

    private static double[] oneHotEncode(int label) {
        double[] result = new double[10];
        result[label] = 1.0;
        return result;
    }

    private static int getPredictionFromOutput(double[] output) {
        int index = 0;
        for (int i = 1; i < output.length; i++) {
            if (output[i] > output[index]) {
                index = i;
            }
        }
        return index;
    }
}
