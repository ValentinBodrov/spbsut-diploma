package bodrov.valentin.spbsut.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.util.Optional;

public class Utils {

    public static String showUrlInputTextDialog() {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Open URL");
        dialog.setHeaderText("Enter your URL:");
        dialog.setContentText("URL:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {

        });
        return dialog.getEditor().getText();
    }

    public static Graphics2D getGraphics(BufferedImage imageWithFont,
                                         int x, int y, String string,
                                         Font font) {
        Graphics2D graphics2D = imageWithFont.createGraphics();

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        FontRenderContext frc = graphics2D.getFontRenderContext();

        GlyphVector gv = font.createGlyphVector(frc, string);
        graphics2D.drawGlyphVector(gv, x, y);
        AffineTransform transform;
        Shape outline = gv.getOutline();
        java.awt.Rectangle outlineBounds = outline.getBounds();
        transform = graphics2D.getTransform();
        transform.translate(x, y);
        graphics2D.transform(transform);
        graphics2D.setColor(java.awt.Color.BLACK);
        graphics2D.draw(outline);
        graphics2D.setClip(outline);
        return graphics2D;
    }

    public static Rectangle getSelectionRectangle(int startX,
                                                  int startY,
                                                  int width,
                                                  int height) {
        Rectangle selectionRectangle =
                new Rectangle(startX, startY, width, height);
        selectionRectangle.setStroke(javafx.scene.paint.Color.WHITE);
        selectionRectangle.setStrokeWidth(1);
        selectionRectangle.getStrokeDashArray().addAll(10d, 10d);
        selectionRectangle.setFill(null);
        return selectionRectangle;
    }

    public static Mat javaImageToMat(BufferedImage image) {
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        byte[] pixels = null;
        Mat cvImage = null;

        int width = image.getWidth();
        int height = image.getHeight();

        if (dataBuffer instanceof DataBufferByte) {
            pixels = ((DataBufferByte) dataBuffer).getData();
        }

        if (dataBuffer instanceof DataBufferInt) {
            int byteSize = width * height;
            pixels = new byte[byteSize * 3];

            int[] imgIntegerPixels = ((DataBufferInt) dataBuffer).getData();

            for (int p = 0; p < byteSize; p++) {
                pixels[p * 3] = (byte) ((imgIntegerPixels[p] & 0x00FF0000) >> 16);
                pixels[p * 3 + 1] = (byte) ((imgIntegerPixels[p] & 0x0000FF00) >> 8);
                pixels[p * 3 + 2] = (byte) (imgIntegerPixels[p] & 0x000000FF);
            }
        }

        if (pixels != null) {
            cvImage = new Mat(height, width, CvType.CV_8UC3);
            cvImage.put(0, 0, pixels);
        }
        return cvImage;
    }

    public static BufferedImage matToJavaImage(Mat cvImage) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", cvImage, buffer);
        Image javaImage = new Image(new ByteArrayInputStream(buffer.toArray()));
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(javaImage, null);

        return bufferedImage;
    }
}
