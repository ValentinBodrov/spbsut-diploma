package bodrov.valentin.spbsut.utils;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;

import java.awt.*;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class Utils {

    public static final int SIMPLE_SIGN = 0;
    public static final int LOBSTER_SIGN = 1;
    public static final int WATERMARK = 2;

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

    public static Rectangle getSelectionRectangle(int startX, int startY, int width, int height) {
        Rectangle selectionRectangle = new Rectangle(startX, startY, width, height);
        selectionRectangle.setStroke(javafx.scene.paint.Color.WHITE);
        selectionRectangle.setStrokeWidth(1);
        selectionRectangle.getStrokeDashArray().addAll(10d, 10d);
        selectionRectangle.setFill(null);
        return selectionRectangle;
    }

}
