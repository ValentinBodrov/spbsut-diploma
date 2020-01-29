package bodrov.valentin.spbsut.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainController {

    public Label helloWorld;
    public Button helloWorldSay;
    public ImageView sampleImage;


    public void sayHelloWorld(ActionEvent actionEvent) {
        helloWorld.setText("Hello world!");

        int width = 960;    //width of the image
        int height = 760;   //height of the image

        // For storing image in RAM
        BufferedImage image = null;

        // READ IMAGE
        try {
            File input_file = new File("src/main/java/bodrov/valentin/spbsut/sample.jpg"); //image file path

            image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_ARGB);

            image = ImageIO.read(input_file);

            System.out.println("Reading complete.");
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

        Image imageToImport = SwingFXUtils.toFXImage(image, null);
        sampleImage.setImage(imageToImport);
        sampleImage.setPreserveRatio(false);
        double fitHeight = sampleImage.getFitHeight();
        double fitWidth = sampleImage.getFitWidth();
        System.out.println(fitHeight + " : " + fitWidth);
        System.out.println("Writing complete.");
    }
}
