package bodrov.valentin.spbsut.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainController {

    public Button helloWorldSay;
    public ImageView sampleImage;

    public void sayHelloWorld(ActionEvent actionEvent) {
        int width = 960;
        int height = 760;
        BufferedImage image = null;

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("src/main/resources/images/"));
            File inputFile = fileChooser.showOpenDialog(null);
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(inputFile);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

        Image imageToImport = SwingFXUtils.toFXImage(image, null);
        sampleImage.setImage(imageToImport);
        sampleImage.setPreserveRatio(false);
    }

}
