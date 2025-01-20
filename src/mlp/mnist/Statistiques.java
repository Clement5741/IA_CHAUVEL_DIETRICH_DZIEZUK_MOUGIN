package mlp.mnist;

public class Statistiques {

    private AlgoClassification algo;

    public Statistiques(AlgoClassification algo) {
        this.algo = algo;
    }

    public double calculerPourcentageCorrect(Donnees donneesTest) {
        Imagette[] imagesTest = donneesTest.getImagettes();
        int correct = 0;

        for (Imagette img : imagesTest) {
            int prediction = algo.predire(img);
            if (prediction == img.getLabel()) {
                correct++;
            }
        }

        return (double) correct / imagesTest.length * 100;
    }
}
