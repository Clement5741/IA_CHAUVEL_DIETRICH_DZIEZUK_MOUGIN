package mlp.mnist;

import java.io.IOException;

public class Donnees {
    private Imagette[] imagettes;

    public Donnees(Imagette[] imagettes) {
        this.imagettes = imagettes;
    }

    public Imagette[] getImagettes() {
        return imagettes;
    }

    public static Donnees loadData(String imagePath, String labelPath) throws IOException {
        Imagette[] images = Imagette.loadImages(imagePath);
        int[] labels = Labels.loadLabels(labelPath).getLabels();

        // Associer les étiquettes aux images
        for (int i = 0; i < images.length; i++) {
            images[i].setLabel(labels[i]);
        }

        return new Donnees(images);
    }
}
