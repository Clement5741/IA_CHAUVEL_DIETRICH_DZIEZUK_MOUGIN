package mlp.mnist;

public class PlusProche extends AlgoClassification {

    public PlusProche(Donnees donnees) {
        super(donnees);
    }

    private double calculerDistance(Imagette img1, Imagette img2) {
        int[][] pixels1 = img1.getPixels();
        int[][] pixels2 = img2.getPixels();
        double distance = 0.0;

        for (int i = 0; i < pixels1.length; i++) {
            for (int j = 0; j < pixels1[i].length; j++) {
                distance += Math.pow(pixels1[i][j] - pixels2[i][j], 2);
            }
        }
        return Math.sqrt(distance);
    }

    @Override
    public int predire(Imagette imagette) {
        Imagette[] imagesEntrainement = donnees.getImagettes();
        Imagette plusProche = imagesEntrainement[0];
        double minDistance = calculerDistance(imagette, plusProche);

        for (Imagette img : imagesEntrainement) {
            double distance = calculerDistance(imagette, img);
            if (distance < minDistance) {
                minDistance = distance;
                plusProche = img;
            }
        }
        return plusProche.getLabel();
    }
}
