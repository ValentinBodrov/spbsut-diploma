package bodrov.valentin.spbsut.api;

import bodrov.valentin.spbsut.processing.NativeProcessing;

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

    public static BufferedImage doSepia(BufferedImage openImage,
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

                int newRed = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                int newGreen = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                int newBlue = (int) (0.272 * r + 0.534 * g + 0.131 * b);
                r = Math.min(newRed, 255);
                g = Math.min(newGreen, 255);
                b = Math.min(newBlue, 255);

                p = (a << 24) | (r << 16) | (g << 8) | b;
                openImage.setRGB(x, y, p);
            }
        }
        return openImage;
    }

    public static BufferedImage doNegative(BufferedImage openImage,
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
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;

                p = (a << 24) | (r << 16) | (g << 8) | b;
                openImage.setRGB(x, y, p);
            }
        }
        return openImage;
    }

    public static BufferedImage getMirroredImage(BufferedImage image,
                                                 int directionFlag) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage mirroredImage = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        if (directionFlag == NativeProcessing.HORIZONTAL) {
            for (int y = 0; y < height; y++) {
                for (int x = 0, mirroredX = width - 1; x < width; x++, mirroredX--) {
                    int p = image.getRGB(x, y);
                    mirroredImage.setRGB(mirroredX, y, p);
                }
            }
        } else if (directionFlag == NativeProcessing.VERTICAL) {
            for (int y = 0, mirroredY = height - 1; y < height; y++, mirroredY--) {
                for (int x = 0; x < width; x++) {
                    int p = image.getRGB(x, y);
                    mirroredImage.setRGB(x, mirroredY, p);
                }
            }
        }

        return mirroredImage;
    }

    public static BufferedImage getRotatedImage(BufferedImage image,
                                                int directionFlag) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage rotatedImage = new BufferedImage(
                height, width, BufferedImage.TYPE_INT_ARGB);
        if (directionFlag == NativeProcessing.RIGHT) {
            for (int x = 0; x < width; x++) {
                for (int y = 0, rotatedY = height - 1; y < height; y++, rotatedY--) {
                    rotatedImage.setRGB(rotatedY, x, image.getRGB(x, y));
                }
            }
        } else if (directionFlag == NativeProcessing.LEFT) {
            for (int x = 0, rotatedX = width - 1; x < width; x++, rotatedX--) {
                for (int y = 0; y < height; y++) {
                    rotatedImage.setRGB(y, rotatedX, image.getRGB(x, y));
                }
            }
        }
        return rotatedImage;
    }

}
