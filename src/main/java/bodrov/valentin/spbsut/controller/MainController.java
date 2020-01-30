package bodrov.valentin.spbsut.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class MainController {

    public Button helloWorldSay;
    public ImageView sampleImage;
    public Label statusBar;

    public void sayHelloWorld(ActionEvent actionEvent) {
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

}
