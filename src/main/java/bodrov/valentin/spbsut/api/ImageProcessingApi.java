package bodrov.valentin.spbsut.api;

import bodrov.valentin.spbsut.processing.NativeProcessing;
import bodrov.valentin.spbsut.utils.Utils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * This class overall provides all static methods
 * for tests. It also includes all methods for independent
 * actions with code. The methods here copy some
 * functional from -Processing classes, so the all
 * explanations are there.
 */
public class ImageProcessingApi {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

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
        URL url;
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

    public static BufferedImage doRandomPixelation(BufferedImage openImage,
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

    public static BufferedImage makeImageContrastEnhanced(Image sourceImage) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Imgproc.cvtColor(source, source, Imgproc.COLOR_BGR2GRAY);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        Imgproc.equalizeHist(source, destination);
        return Utils.matToJavaImage(destination);
    }

    public static BufferedImage makeImageBrightnessEnhanced(Image sourceImage, double alpha, double beta) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        source.convertTo(destination, -1, alpha, beta);
        return Utils.matToJavaImage(destination);
    }

    public static BufferedImage makeImageSharpnessEnhanced(Image sourceImage) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        Imgproc.GaussianBlur(source, destination, new Size(0, 0), 10);
        Core.addWeighted(source, 1.5, destination, -0.5, 0, destination);
        return Utils.matToJavaImage(destination);
    }

    public static BufferedImage makeImageGaussianBlurred(Image sourceImage) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat destination = new Mat();
        Imgproc.GaussianBlur(source, destination, new Size(45, 45), 0);
        return Utils.matToJavaImage(destination);
    }

    public static BufferedImage makeImageMedianBlurred(Image sourceImage) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat destination = new Mat();
        Imgproc.medianBlur(source, destination, 15);
        return Utils.matToJavaImage(destination);
    }


    public static BufferedImage makeImageBilateralFiltered(Image sourceImage) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat destination = new Mat();
        Imgproc.bilateralFilter(source, destination, 15, 80, 70, Core.BORDER_DEFAULT);
        return Utils.matToJavaImage(destination);
    }

    public static BufferedImage makeImageBoxFiltered(Image sourceImage) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat destination = new Mat();
        Size size = new Size(45, 45);
        Point point = new Point(-1, -1);
        Imgproc.boxFilter(source, destination, -1, size, point, true, Core.BORDER_DEFAULT);
        return Utils.matToJavaImage(destination);
    }

    public static BufferedImage makeImageSQRBoxFiltered(Image sourceImage) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat destination = new Mat();
        Imgproc.sqrBoxFilter(source, destination, -1, new Size(1, 1));
        return Utils.matToJavaImage(destination);
    }

    public static BufferedImage makeImage2DFiltered(Image sourceImage) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat destination = new Mat();
        Mat kernel = Mat.ones(2, 2, CvType.CV_32F);
        for (int i = 0; i < kernel.rows(); i++) {
            for (int j = 0; j < kernel.cols(); j++) {
                double[] m = kernel.get(i, j);

                for (int k = 1; k < m.length; k++) {
                    m[k] = m[k] / (2 * 2);
                }
                kernel.put(i, j, m);
            }
        }
        Imgproc.filter2D(source, destination, -1, kernel);
        return Utils.matToJavaImage(destination);
    }

    public static BufferedImage makeImagePixelated(Image sourceImage, int pixelationCoefficient) {
        if (pixelationCoefficient == 0) {
            return SwingFXUtils.fromFXImage(sourceImage, null);
        }
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat destination = new Mat();
        Mat temp = new Mat();
        float tempWidth = (float) source.width() / pixelationCoefficient;
        float tempHeight = (float) source.height() / pixelationCoefficient;
        Imgproc.resize(source, temp, new Size(tempWidth, tempHeight), 0, 0, Imgproc.INTER_LINEAR);
        Imgproc.resize(temp, destination, new Size(source.width(), source.height()), 0, 0, Imgproc.INTER_NEAREST);
        return Utils.matToJavaImage(destination);
    }

    public static BufferedImage makeImageInForegroundDetected(Image sourceImage) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat result = new Mat();
        Mat bgdModel = new Mat();
        Mat fgdModel = new Mat();
        Mat scalarMat = new Mat(1, 1, CvType.CV_8U, new Scalar(3));
        Imgproc.grabCut(source, result,
                new Rect(1, 1, source.width(), source.height()),
                bgdModel, fgdModel, 15, Imgproc.GC_INIT_WITH_RECT);
        Core.compare(result, scalarMat, result, Core.CMP_EQ);
        Mat foreground = new Mat(source.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        source.copyTo(foreground, result);

        Mat foregroundDestination = new Mat(foreground.rows(), foreground.cols(), foreground.type());
        Imgproc.GaussianBlur(foreground, foregroundDestination, new Size(0, 0), 10);
        Core.addWeighted(foreground, 1.5, foregroundDestination, -0.5, 0, foregroundDestination);

        Mat destination = new Mat();
        double alpha = 0.45;
        double beta = 0.55;
        Core.addWeighted(source, alpha, foregroundDestination, beta, 0.0, destination);
        return Utils.matToJavaImage(destination);
    }

}
