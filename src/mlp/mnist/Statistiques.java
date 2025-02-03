package mlp.mnist;

import java.io.PrintWriter;

public class Statistiques {

    private AlgoClassification algo;

    public Statistiques(AlgoClassification algo) {
        this.algo = algo;
    }

    public double calculerPourcentageCorrect(Donnees donneesTest, PrintWriter writer) {
        Imagette[] imagesTest = donneesTest.getImagettes();
        int correct = 0;
        int total = imagesTest.length;

        for (int i = 0; i < total; i++) {
            Imagette img = imagesTest[i];
            int prediction = algo.predire(img);

            if (prediction == img.getLabel()) {
                correct++;
            }

            // Toutes les 1000 images, écrire les stats dans le fichier et afficher en console
            if ((i + 1) % 1000 == 0 || i == total - 1) {
                double precision = (double) correct / (i + 1) * 100;

                // Affichage console
                System.out.printf("Images : %d / %d | Précision : %.1f%%\n",
                        (i + 1), total, precision);

                // Écriture dans le fichier via le `PrintWriter` existant
                writer.println((i + 1)+";"+precision);
                writer.flush(); // Forcer l'écriture immédiate
            }
        }

        return (double) correct / total * 100;
    }
}