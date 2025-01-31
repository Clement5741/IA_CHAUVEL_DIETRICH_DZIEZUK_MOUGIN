package mlp.mnist;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Imagette {
    private int[][] pixels;
    private int label;

    public Imagette(int[][] pixels, int label) {
        this.pixels = pixels;
        this.label = label;
    }

    public void setPixel(int row, int col, int value) {
        pixels[row][col] = value;
    }

    public int[][] getPixels() {
        return pixels;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public static Imagette[] loadImages(String filePath) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) {
            int magicNumber = dis.readInt();
            int numberOfImages = dis.readInt();
            int rows = dis.readInt();
            int cols = dis.readInt();

            Imagette[] images = new Imagette[numberOfImages];

            for (int i = 0; i < numberOfImages; i++) {
                images[i] = new Imagette(new int[rows][cols], -1);
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        images[i].setPixel(row, col, dis.readUnsignedByte());
                    }
                }
            }
            return images;
        }
    }


}
