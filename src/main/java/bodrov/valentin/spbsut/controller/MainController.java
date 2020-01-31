package bodrov.valentin.spbsut.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import static bodrov.valentin.spbsut.utils.Utils.showInputTextDialog;

public class MainController {

    public ImageView sampleImage;
    public Label statusBar;
    public MenuItem openLocal;
    public MenuItem openUrl;

    private Image currentProcessedImage;

    private Image getCurrentProcessedImage() {
        return currentProcessedImage;
    }

    private void setCurrentProcessedImage(Image currentProcessedImage) {
        this.currentProcessedImage = currentProcessedImage;
    }

    private void setImageToImageView(BufferedImage image) {
        Image imageToImport = SwingFXUtils.toFXImage(image, null);
        sampleImage.setImage(imageToImport);
        sampleImage.setPreserveRatio(false);
        setCurrentProcessedImage(imageToImport);
    }

    private void setLogs(String message) {
        statusBar.setText(message);
    }

    public void openLocal(ActionEvent actionEvent) {
        int width = 960;
        int height = 760;
        BufferedImage image;
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(
                    new File("src/main/resources/images/"));
            File inputFile = fileChooser.showOpenDialog(null);

            if (inputFile == null) {
                throw new NullPointerException("The file isn't chosen");
            }
            if (!inputFile.getName().matches(".*(png|jpg|jpeg)")) {
                throw new Exception("The choosed file isn't image");
            }
            setLogs(String.format(
                    "The image with name %s is loaded", inputFile.getName()));

            image = new BufferedImage(
                    width, height, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(inputFile);
            setImageToImageView(image);
        } catch (Exception e) {
            setLogs(e.getMessage());
            sampleImage.setImage(null);
        }
    }

    public void openURL(ActionEvent actionEvent) {
        BufferedImage image;

        try {
            String website = showInputTextDialog();
            URL url = new URL(website);
            image = ImageIO.read(url);
            setImageToImageView(image);
            setLogs(String.format("The image from %s was successfully loaded",
                    url));
        } catch (Exception e) {
            setLogs(e.getMessage());
            sampleImage.setImage(null);
        }
    }

    public void doGreyscale(ActionEvent actionEvent) {
        BufferedImage image = SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
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
        setLogs("The image was successfully greyscaled");
        setImageToImageView(image);
    }

}
