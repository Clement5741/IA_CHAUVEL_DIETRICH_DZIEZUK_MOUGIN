package mlp.mnist;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagetteUtils {
    public static void saveImage(Imagette imagette, String filePath) throws IOException {
        int[][] pixels = imagette.getPixels();
        int rows = pixels.length;
        int cols = pixels[0].length;
        BufferedImage img = new BufferedImage(cols, rows, BufferedImage.TYPE_BYTE_GRAY);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int gray = pixels[row][col];
                int rgb = gray << 16 | gray << 8 | gray; // Grayscale conversion
                img.setRGB(col, row, rgb);
            }
        }

        ImageIO.write(img, "png", new File(filePath));
    }
}