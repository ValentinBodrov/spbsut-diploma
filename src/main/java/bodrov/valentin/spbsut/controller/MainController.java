package bodrov.valentin.spbsut.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Optional;

public class MainController {

    public ImageView sampleImage;
    public Label statusBar;
    public MenuItem openLocal;
    public MenuItem openUrl;

    private String showInputTextDialog() {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Open URL");
        dialog.setHeaderText("Enter your URL:");
        dialog.setContentText("URL:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {

        });
        return dialog.getEditor().getText();
    }

    public void openLocal(ActionEvent actionEvent) {
        int width = 960;
        int height = 760;
        BufferedImage image = null;

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(
                    new File("src/main/resources/images/"));
            File inputFile = fileChooser.showOpenDialog(null);

            if (!inputFile.getName().matches(".*(png|jpg|jpeg)")) {
                statusBar.setText(String.format(
                        "The file %s isn't image", inputFile.getName()));
                throw new Exception("The choosed file isn't image");
            }

            statusBar.setText(String.format("The image with name %s is loaded",
                    inputFile.getName()));
            image = new BufferedImage(
                    width, height, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(inputFile);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        if (image == null) {
            sampleImage.setImage(null);
        } else {
            Image imageToImport = SwingFXUtils.toFXImage(image, null);
            sampleImage.setImage(imageToImport);
            sampleImage.setPreserveRatio(false);
        }
    }

    public void openURL(ActionEvent actionEvent) {
        BufferedImage image = null;

        try {
            String website = showInputTextDialog();
            //String website = "https://sun9-7.userapi.com/c855236/v855236303/1da762/B2d3T1UDNDE.jpg";

            System.out.println("Downloading File From: " + website);

            URL url = new URL(website);
            image = ImageIO.read(url);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        if (image == null) {
            sampleImage.setImage(null);
        } else {
            Image imageToImport = SwingFXUtils.toFXImage(image, null);
            sampleImage.setImage(imageToImport);
            sampleImage.setPreserveRatio(false);
        }
    }
}
