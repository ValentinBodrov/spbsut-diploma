package bodrov.valentin.spbsut.processing;

import java.awt.image.BufferedImage;

/**
 * This class provides all methods for pics processings,
 * those didn't use third party libraries - there's only java.awt
 */
public class NativeProcessing extends AbstractProcessing {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int RIGHT = 2;
    public static final int LEFT = 3;
    public static final int RED = 4;
    public static final int GREEN = 5;
    public static final int BLUE = 6;

    public static BufferedImage getGreyscaledImage(BufferedImage image,
                                                   int startX,
                                                   int startY,
                                                   int width,
                                                   int height) {
        for (int y = startY; y < height; y++) {
            for (int x = startX; x < width; x++) {
                int p = image.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                int avg = (r + g + b) / 3;

                p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                image.setRGB(x, y, p);
            }
        }
        return image;
    }

    public static BufferedImage getSepiaImage(BufferedImage image,
                                              int startX,
                                              int startY,
                                              int width,
                                              int height) {
        for (int y = startY; y < height; y++) {
            for (int x = startX; x < width; x++) {
                int p = image.getRGB(x, y);
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
                image.setRGB(x, y, p);
            }
        }
        return image;
    }

    public static BufferedImage getNegativeImage(BufferedImage image,
                                                 int startX,
                                                 int startY,
                                                 int width,
                                                 int height) {
        for (int y = startY; y < height; y++) {
            for (int x = startX; x < width; x++) {
                int p = image.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;

                p = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(x, y, p);
            }
        }
        return image;
    }

    public static BufferedImage getRandomPixelatedImage(BufferedImage image,
                                                        int startX,
                                                        int startY,
                                                        int width,
                                                        int height) {
        for (int y = startY; y < height; y++) {
            for (int x = startX; x < width; x++) {
                int newX = (int) (Math.random() * width);
                int newY = (int) (Math.random() * height);
                int swappedP = image.getRGB(newX, newY);
                image.setRGB(x, y, swappedP);
            }
        }
        return image;
    }

    /**
     * @param directionFlag here is for mirroring direction
     */
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

    /**
     * @param directionFlag here is for rotation direction
     */
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

    /**
     * @param colorFlag here is for color which
     *                  channel in ARGB will be changed
     */
    public static BufferedImage getChangedImage(BufferedImage image,
                                                BufferedImage originalImage,
                                                int startX,
                                                int startY,
                                                int width,
                                                int height,
                                                double sliderValue,
                                                int colorFlag) {
        for (int y = startY; y < height; y++) {
            for (int x = startX; x < width; x++) {
                int p = image.getRGB(x, y);
                int originalP = originalImage.getRGB(x, y);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                int originalR = (originalP >> 16) & 0xff;
                int originalG = (originalP >> 8) & 0xff;
                int originalB = originalP & 0xff;

                if (colorFlag == NativeProcessing.RED) {
                    int newR = originalR + (int) sliderValue;
                    r = Math.min(newR, 255);
                } else if (colorFlag == NativeProcessing.GREEN) {
                    int newG = originalG + (int) sliderValue;
                    g = Math.min(newG, 255);
                } else if (colorFlag == NativeProcessing.BLUE) {
                    int newB = originalB + (int) sliderValue;
                    b = Math.min(newB, 255);
                }

                p = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(x, y, p);
            }
        }
        return image;
    }

}
