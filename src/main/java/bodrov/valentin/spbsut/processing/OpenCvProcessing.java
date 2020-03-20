package bodrov.valentin.spbsut.processing;

import bodrov.valentin.spbsut.utils.Utils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;

public class OpenCvProcessing {

    public static BufferedImage enhanceContrast(Image sourceImage) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Imgproc.cvtColor(source, source, Imgproc.COLOR_BGR2GRAY);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        Imgproc.equalizeHist(source, destination);
        return Utils.matToJavaImage(destination);
    }

    public static BufferedImage enhanceBrightness(Image sourceImage, double alpha, double beta) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        source.convertTo(destination, -1, alpha, beta);
        return Utils.matToJavaImage(destination);
    }

    public static BufferedImage doGaussianBlur(Image sourceImage) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat destination = new Mat();
        Imgproc.GaussianBlur(source, destination, new Size(45, 45), 0);
        return Utils.matToJavaImage(destination);
    }

    public static BufferedImage doMedianBlur(Image sourceImage) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat destination = new Mat();
        Imgproc.medianBlur(source, destination, 15);
        return Utils.matToJavaImage(destination);
    }


    public static BufferedImage doBilateralFilter(Image sourceImage) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat destination = new Mat();
        Imgproc.bilateralFilter(source, destination, 15, 80, 70, Core.BORDER_DEFAULT);
        return Utils.matToJavaImage(destination);
    }

    public static BufferedImage doBoxFilter(Image sourceImage) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat destination = new Mat();
        Size size = new Size(45, 45);
        Point point = new Point(-1, -1);
        Imgproc.boxFilter(source, destination, -1, size, point, true, Core.BORDER_DEFAULT);
        return Utils.matToJavaImage(destination);
    }

    public static BufferedImage doSQRBoxFilter(Image sourceImage) {
        Mat source = Utils.javaImageToMat(
                SwingFXUtils.fromFXImage(sourceImage, null));
        Mat destination = new Mat();
        Imgproc.sqrBoxFilter(source, destination, -1, new Size(1, 1));
        return Utils.matToJavaImage(destination);
    }

    public static BufferedImage do2DFilter(Image sourceImage) {
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

}
