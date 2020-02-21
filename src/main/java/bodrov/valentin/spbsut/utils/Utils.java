package bodrov.valentin.spbsut.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextInputDialog;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
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

    public static Graphics2D getGraphics(BufferedImage imageWithFont, int x, int y, String upperString) {
        Graphics2D graphics2D = imageWithFont.createGraphics();

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        FontRenderContext frc = graphics2D.getFontRenderContext();
        Font font = new Font("Impact", Font.PLAIN, 40);
        GlyphVector gv = font.createGlyphVector(frc, upperString);
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

}
