package bodrov.valentin.spbsut.api;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImageProcessingApi {

    public static BufferedImage openLocal(String filename) {
        File inputFile = new File(filename);

        BufferedImage image = null;
        try {
            image = ImageIO.read(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static BufferedImage openUrl(String website) {
        if (!website.matches("http(|s)://.*(.([a-zA-z]{2,3})/?).*")) {
            return null;
        }
        BufferedImage image = null;
        URL url = null;
        try {
            url = new URL(website);
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static boolean savePictureAs(BufferedImage image, String createdFilename) {
        File outputFile = new File(createdFilename);
        if (!outputFile.getName().matches(".*png")) {
            return false;
        }
        try {
            ImageIO.write(image, "png", outputFile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static BufferedImage doGreyScale(BufferedImage openImage,
                                            int startX,
                                            int startY,
                                            int width, int height) {
        for (int y = startY; y < height; y++) {
            for (int x = startX; x < width; x++) {
                int p = openImage.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                int avg = (r + g + b) / 3;

                p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                openImage.setRGB(x, y, p);
            }
        }
        return openImage;
    }
}
