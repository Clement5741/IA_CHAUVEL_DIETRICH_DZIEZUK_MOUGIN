package mlp.mnist;

import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class kNN extends AlgoClassification {
    private int k;

    public kNN(Donnees donnees, int k) {
        super(donnees);
        this.k = k;
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

    private Imagette[] trouverKPlusProches(Imagette imagette) {
        PriorityQueue<Imagette> kPlusProches = new PriorityQueue<>(k, Comparator.comparingDouble(img -> -calculerDistance(img, imagette)));
        for (Imagette img : donnees.getImagettes()) {
            if (kPlusProches.size() < k) {
                kPlusProches.add(img);
            } else {
                double distanceActuelle = calculerDistance(img, imagette);
                double maxDistanceDansQueue = calculerDistance(kPlusProches.peek(), imagette);
                if (distanceActuelle < maxDistanceDansQueue) {
                    kPlusProches.poll();
                    kPlusProches.add(img);
                }
            }
        }
        return kPlusProches.toArray(new Imagette[0]);
    }

    @Override
    public int predire(Imagette imagette) {
        Imagette[] kProches = trouverKPlusProches(imagette);
        Map<Integer, Integer> frequenceEtiquettes = new HashMap<>();

        for (Imagette img : kProches) {
            int label = img.getLabel();
            frequenceEtiquettes.put(label, frequenceEtiquettes.getOrDefault(label, 0) + 1);
        }

        return frequenceEtiquettes.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }
}
