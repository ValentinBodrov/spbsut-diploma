package bodrov.valentin.spbsut.controller;

import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static bodrov.valentin.spbsut.utils.Utils.showUrlInputTextDialog;

public class MainController {

    public ImageView sampleImage;
    public Label statusBar;
    public AnchorPane centerPane;
    public Slider redSlider;
    public Slider greenSlider;
    public Slider blueSlider;
    public Spinner redSpinner;
    public Spinner greenSpinner;
    public Spinner blueSpinner;
    public Button test;

    private Image currentProcessedImage;
    private Image originalImage;

    @FXML
    private void initialize() {
        redSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, 1));
        greenSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, 1));
        blueSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255, 1));

        redSlider.valueProperty().addListener((
                observableValue, oldValue, newValue) -> {
            redSpinner.getValueFactory().setValue(newValue.intValue());
            changeRedCustom();
        });
        greenSlider.valueProperty().addListener((
                observableValue, oldValue, newValue) -> {
            greenSpinner.getValueFactory().setValue(newValue.intValue());
            changeGreenCustom();
        });
        blueSlider.valueProperty().addListener((
                observableValue, oldValue, newValue) -> {
            blueSpinner.getValueFactory().setValue(newValue.intValue());
            changeBlueCustom();
        });
        redSpinner.getValueFactory().valueProperty().addListener((
                ChangeListener<Number>) (observableValue, oldValue, newValue) -> {
            redSlider.setValue(newValue.intValue());
            changeRedCustom();
        });
        greenSpinner.getValueFactory().valueProperty().addListener((
                ChangeListener<Number>) (observableValue, oldValue, newValue) -> {
            greenSlider.setValue(newValue.intValue());
            changeGreenCustom();
        });
        blueSpinner.getValueFactory().valueProperty().addListener((
                ChangeListener<Number>) (observableValue, oldValue, newValue) -> {
            blueSlider.setValue(newValue.intValue());
            changeBlueCustom();
        });
    }

    private Image getCurrentProcessedImage() {
        return currentProcessedImage;
    }

    private void setCurrentProcessedImage(Image currentProcessedImage) {
        this.currentProcessedImage = currentProcessedImage;
    }

    public Image getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(Image originalImage) {
        this.originalImage = originalImage;
    }

    private void setImageToImageView(BufferedImage image) {
        Image imageToImport = SwingFXUtils.toFXImage(image, null);
        sampleImage.setImage(imageToImport);
        sampleImage.setFitWidth(image.getWidth());
        sampleImage.setSmooth(true);
        sampleImage.setCache(true);
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
            setOriginalImage(SwingFXUtils.toFXImage(image, null));
            setImageToImageView(image);
        } catch (Exception e) {
            setLogs(e.getMessage());
            sampleImage.setImage(null);
        }
    }

    public void openUrl(ActionEvent actionEvent) {
        BufferedImage image;

        try {
            String website = showUrlInputTextDialog();
            if (!website.matches("http(|s)://.*(.(com|ru|en|eu|su|uk)/?).*")) {
                sampleImage.setImage(null);
                setCurrentProcessedImage(null);
                throw new Exception("The URL is invalid");
            }
            URL url = new URL(website);
            image = ImageIO.read(url);
            setOriginalImage(SwingFXUtils.toFXImage(image, null));
            setImageToImageView(image);
            setLogs(String.format("The image from %s was successfully loaded",
                    url));
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

    public void savePictureAs(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(
                    new File("src/main/resources/images/"));
            File outputFile = fileChooser.showSaveDialog(null);

            if (outputFile == null) {
                throw new NullPointerException("Something goes wrong...");
            }
            if (!outputFile.getName().matches(".*png")) {
                throw new Exception("The preferred file cannot be saved as image");
            }
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
            try {
                ImageIO.write(bufferedImage, "png", outputFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            setLogs(String.format(
                    "The image with name %s is saved", outputFile.getName()));
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

    public void doGreyscale(ActionEvent actionEvent) {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
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
            setLogs("The greyscale effect was successfully applied to image");
            setImageToImageView(image);
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

    public void doSepia(ActionEvent actionEvent) {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
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

                    int newRed = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                    int newGreen = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                    int newBlue = (int) (0.272 * r + 0.534 * g + 0.131 * b);

                    r = Math.min(newRed, 255);
                    g = Math.min(newGreen, 255);
                    b = Math.min(newBlue, 255);

                    p = (a << 24) | (r << 16) | (g << 8) | b;

                    image.setRGB(x, y, p);
                }
            }

            setLogs("The sepia effect was successfully applied to image");
            setImageToImageView(image);
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

    public void doNegative(ActionEvent actionEvent) {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
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
                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;

                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    image.setRGB(x, y, p);
                }
            }
            setLogs("The negative effect was successfully applied to image");
            setImageToImageView(image);
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

    public void doHorizontalMirroring(ActionEvent actionEvent) {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
            BufferedImage image = SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage mirroredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0, mirroredX = width - 1; x < width; x++, mirroredX--) {
                    int p = image.getRGB(x, y);
                    mirroredImage.setRGB(mirroredX, y, p);
                }
            }
            setLogs("The horizontal mirroring effect was successfully applied to image");
            setOriginalImage(SwingFXUtils.toFXImage(mirroredImage, null));
            setImageToImageView(mirroredImage);
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

    public void doVerticalMirroring(ActionEvent actionEvent) {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
            BufferedImage image = SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage mirroredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0, mirroredY = height - 1; y < height; y++, mirroredY--) {
                for (int x = 0; x < width; x++) {
                    int p = image.getRGB(x, y);
                    mirroredImage.setRGB(x, mirroredY, p);
                }
            }
            setLogs("The horizontal mirroring effect was successfully applied to image");
            setOriginalImage(SwingFXUtils.toFXImage(mirroredImage, null));
            setImageToImageView(mirroredImage);
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

    public void rotateRight(ActionEvent actionEvent) {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
            BufferedImage image = SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage rotatedImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0, rotatedY = height - 1; y < height; y++, rotatedY--) {
                    rotatedImage.setRGB(rotatedY, x, image.getRGB(x, y));
                }
            }
            setLogs("The left rotation effect was successfully applied to image");
            setOriginalImage(SwingFXUtils.toFXImage(rotatedImage, null));
            setImageToImageView(rotatedImage);
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

    public void rotateLeft(ActionEvent actionEvent) {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
            BufferedImage image = SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage rotatedImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
            for (int x = 0, rotatedX = width - 1; x < width; x++, rotatedX--) {
                for (int y = 0; y < height; y++) {
                    rotatedImage.setRGB(y, rotatedX, image.getRGB(x, y));
                }
            }
            setLogs("The left rotation effect was successfully applied to image");
            setOriginalImage(SwingFXUtils.toFXImage(rotatedImage, null));
            setImageToImageView(rotatedImage);
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

    public void changeRedCustom() {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
            BufferedImage image = SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
            BufferedImage originalImage = SwingFXUtils.fromFXImage(getOriginalImage(), null);
            int width = image.getWidth();
            int height = image.getHeight();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int p = image.getRGB(x, y);
                    int originalP = originalImage.getRGB(x, y);

                    int a = (p >> 24) & 0xff;
                    int r;
                    int originalR = (originalP >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;

                    int newR = originalR + (int) redSlider.getValue();
                    r = Math.min(newR, 255);

                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    image.setRGB(x, y, p);
                }
            }
            setLogs("The red channel was successfully changed");
            setImageToImageView(image);
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

    public void changeGreenCustom() {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
            BufferedImage image = SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
            BufferedImage originalImage = SwingFXUtils.fromFXImage(getOriginalImage(), null);
            int width = image.getWidth();
            int height = image.getHeight();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int p = image.getRGB(x, y);
                    int originalP = originalImage.getRGB(x, y);

                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g;
                    int originalG = (originalP >> 8) & 0xff;
                    int b = p & 0xff;

                    int newG = originalG + (int) greenSlider.getValue();
                    g = Math.min(newG, 255);

                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    image.setRGB(x, y, p);
                }
            }
            setLogs("The green channel was successfully changed");
            setImageToImageView(image);
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

    public void changeBlueCustom() {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
            BufferedImage image = SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
            BufferedImage originalImage = SwingFXUtils.fromFXImage(getOriginalImage(), null);
            int width = image.getWidth();
            int height = image.getHeight();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int p = image.getRGB(x, y);
                    int originalP = originalImage.getRGB(x, y);

                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b;
                    int originalB = originalP & 0xff;

                    int newB = originalB + (int) blueSlider.getValue();
                    b = Math.min(newB, 255);

                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    image.setRGB(x, y, p);
                }
            }
            setLogs("The blue channel was successfully changed");
            setImageToImageView(image);
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

    @Deprecated
    public void doCringe() {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
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
                    r += (int) redSlider.getValue();
                    g += (int) greenSlider.getValue();
                    b += (int) blueSlider.getValue();

                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    image.setRGB(x, y, p);
                }
            }
            setLogs("The negative effect was successfully applied to image");
            setImageToImageView(image);
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

    public void resetChanges(ActionEvent actionEvent) {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
            setImageToImageView(SwingFXUtils.fromFXImage(getOriginalImage(), null));
            setLogs("The original image was restored");
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

}
