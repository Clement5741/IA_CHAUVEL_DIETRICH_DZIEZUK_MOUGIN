package mlp.mnist;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Labels {
    private int[] labels;

    public Labels(int[] labels) {
        this.labels = labels;
    }

    public int[] getLabels() {
        return labels;
    }

    public static Labels loadLabels(String filePath) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) {
            int magicNumber = dis.readInt(); // Devrait être 2049 pour les fichiers d'étiquettes
            if (magicNumber != 2049) {
                throw new IOException("Le fichier des étiquettes n'est pas valide. Magic number: " + magicNumber);
            }

            int numberOfLabels = dis.readInt(); // Nombre d'étiquettes dans le fichier
            int[] labels = new int[numberOfLabels];

            for (int i = 0; i < numberOfLabels; i++) {
                labels[i] = dis.readUnsignedByte(); // Lire chaque étiquette comme un octet non signé
            }

            return new Labels(labels);
        }
    }
}
