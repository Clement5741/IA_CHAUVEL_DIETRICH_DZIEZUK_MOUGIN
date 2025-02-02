package mlp.mnist;

import java.io.PrintWriter;

public class Statistiques {

    private AlgoClassification algo;

    public Statistiques(AlgoClassification algo) {
        this.algo = algo;
    }

    public double calculerPourcentageCorrect(Donnees donneesTest, PrintWriter writer, long startTime) {
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
                long elapsedTime = System.currentTimeMillis() - startTime;
                double precision = (double) correct / (i + 1) * 100;
                int echec = (i + 1) - correct;

                // Affichage console
                System.out.printf("Images : %d / %d | Précision : %.2f%% | Réussites : %d | Échecs : %d | Temps : %d ms%n",
                        (i + 1), total, precision, correct, echec, elapsedTime);

                // Écriture dans le fichier via le `PrintWriter` existant
                writer.printf("%d;%.2f;%d;%d;%d%n", (i + 1), precision, correct, echec, elapsedTime);
                writer.flush(); // Forcer l'écriture immédiate
            }
        }

        return (double) correct / total * 100;
    }
}