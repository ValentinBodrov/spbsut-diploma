package bodrov.valentin.spbsut.controller;

import bodrov.valentin.spbsut.utils.Utils;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    public Label coordinatesBar;
    public Slider redSlider;
    public Slider greenSlider;
    public Slider blueSlider;
    public Spinner redSpinner;
    public Spinner greenSpinner;
    public Spinner blueSpinner;
    public AnchorPane centerPane;
    public ImageView selectionImageView;
    public ImageView cutImageView;

    private Image currentProcessedImage;
    private Image originalImage;
    private Image selectedImage;

    private int startX;
    private int startY;
    private int releaseX;
    private int releaseY;
    private Rectangle selectionRectangle;

    private boolean gointToBeSelected = false;
    private boolean gointToBeCut = false;

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getReleaseX() {
        return releaseX;
    }

    public void setReleaseX(int releaseX) {
        this.releaseX = releaseX;
    }

    public int getReleaseY() {
        return releaseY;
    }

    public void setReleaseY(int releaseY) {
        this.releaseY = releaseY;
    }

    @FXML
    private void initialize() {
        redSpinner.setValueFactory(new SpinnerValueFactory.
                IntegerSpinnerValueFactory(0, 255, 1));
        greenSpinner.setValueFactory(new SpinnerValueFactory.
                IntegerSpinnerValueFactory(0, 255, 1));
        blueSpinner.setValueFactory(new SpinnerValueFactory.
                IntegerSpinnerValueFactory(0, 255, 1));

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
        redSpinner.getValueFactory().valueProperty().
                addListener((ChangeListener<Number>)
                        (observableValue, oldValue, newValue) -> {
            redSlider.setValue(newValue.intValue());
            changeRedCustom();
        });
        greenSpinner.getValueFactory().valueProperty().
                addListener((ChangeListener<Number>)
                        (observableValue, oldValue, newValue) -> {
            greenSlider.setValue(newValue.intValue());
            changeGreenCustom();
        });
        blueSpinner.getValueFactory().valueProperty().
                addListener((ChangeListener<Number>)
                        (observableValue, oldValue, newValue) -> {
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

    private Image getOriginalImage() {
        return originalImage;
    }

    private void setOriginalImage(Image originalImage) {
        this.originalImage = originalImage;
    }

    public Image getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(Image selectedImage) {
        this.selectedImage = selectedImage;
    }


    private void setImageToImageView(BufferedImage image) {
        Image imageToImport = SwingFXUtils.toFXImage(image, null);
        sampleImage.setImage(imageToImport);
        if (imageToImport.getWidth() > imageToImport.getHeight()) {
            if (imageToImport.getWidth() > centerPane.getPrefWidth()) {
                sampleImage.setFitWidth(centerPane.getPrefWidth());
            }
            sampleImage.setFitHeight(imageToImport.getHeight());
        } else {
            sampleImage.setFitWidth(imageToImport.getWidth());
        }
        sampleImage.setSmooth(true);
        sampleImage.setCache(true);
        setCurrentProcessedImage(imageToImport);
    }

    private void setLogs(String message) {
        statusBar.setText(message);
    }

    private void setCoordinates(double x, double y) {
        coordinatesBar.setText(String.format("X: %f Y: %f", x, y));
    }

    public void openLocal(ActionEvent actionEvent) {
        int width = 960;
        int height = 760;
        BufferedImage image;
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.dir")));
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
            FileChooser.ExtensionFilter imageFilter
                    = new FileChooser.ExtensionFilter("Image PNG Files", "*.png");
            fileChooser.getExtensionFilters().add(imageFilter);
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.dir")));
            File outputFile = fileChooser.showSaveDialog(null);

            if (outputFile == null) {
                throw new NullPointerException("The file isn't chosen");
            }
            if (!outputFile.getName().matches(".*png")) {
                throw new Exception(
                        "The preferred file cannot be saved as image");
            }
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
            BufferedImage bufferedImage =
                    SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
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
            BufferedImage image =
                    SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
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
            BufferedImage image =
                    SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
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
            BufferedImage image =
                    SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
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
            BufferedImage image =
                    SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage mirroredImage = new BufferedImage(
                    width, height, BufferedImage.TYPE_INT_ARGB);
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
            BufferedImage image =
                    SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage mirroredImage = new BufferedImage(
                    width, height, BufferedImage.TYPE_INT_ARGB);
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
            BufferedImage image =
                    SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage rotatedImage = new BufferedImage(
                    height, width, BufferedImage.TYPE_INT_ARGB);
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
            BufferedImage image =
                    SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage rotatedImage = new BufferedImage(
                    height, width, BufferedImage.TYPE_INT_ARGB);
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

    private void changeRedCustom() {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
            BufferedImage image =
                    SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
            BufferedImage originalImage =
                    SwingFXUtils.fromFXImage(getOriginalImage(), null);
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

    private void changeGreenCustom() {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
            BufferedImage image =
                    SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
            BufferedImage originalImage =
                    SwingFXUtils.fromFXImage(getOriginalImage(), null);
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

    private void changeBlueCustom() {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
            BufferedImage image =
                    SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
            BufferedImage originalImage =
                    SwingFXUtils.fromFXImage(getOriginalImage(), null);
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
    private void doCringe() {
        try {
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
            BufferedImage image =
                    SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
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
            setLogs("The cringe effect was successfully applied to image");
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
            centerPane.getChildren().remove(selectionRectangle);
            setImageToImageView(
                    SwingFXUtils.fromFXImage(getOriginalImage(), null));
            setLogs("The original image was restored");
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

    public void getCoordinates(MouseEvent mouseEvent) {
        setCoordinates(mouseEvent.getX(), mouseEvent.getY());
    }

    public void testPressed(MouseEvent mouseEvent) {
        setStartX((int) mouseEvent.getX());
        setStartY((int) mouseEvent.getY());
    }

    public void testReleased(MouseEvent mouseEvent) {
        try {
            centerPane.getChildren().remove(selectionRectangle);
            if (getCurrentProcessedImage() == null) {
                throw new Exception("There's no processed image");
            }
            if (!gointToBeSelected) {
                throw new Exception("Click the 'Select'-button first!");
            }
            setReleaseX((int) mouseEvent.getX());
            setReleaseY((int) mouseEvent.getY());
            if (getStartY() > getReleaseY()) {
                int tempStartY = getStartY();
                int tempReleaseY = getReleaseY();
                setStartY(tempReleaseY);
                setReleaseY(tempStartY);
            }
            if (getStartX() > getReleaseX()) {
                int tempStartX = getStartX();
                int tempReleaseX = getReleaseX();
                setStartX(tempReleaseX);
                setReleaseX(tempStartX);
            }
            int newWidth = Math.abs(getReleaseX() - getStartX());
            int newHeight = Math.abs(getReleaseY() - getStartY());
            setLogs(String.format("Width: %d Height: %d", newWidth, newHeight));
            selectionRectangle = new Rectangle(startX, startY, newWidth, newHeight);
            selectionRectangle.setStroke(Color.WHITE);
            selectionRectangle.setStrokeWidth(1);
            selectionRectangle.getStrokeDashArray().addAll(10d, 10d);
            selectionRectangle.setFill(null);
            centerPane.getChildren().add(selectionRectangle);

            Image oldImage = getCurrentProcessedImage();
            Image imageToBeSelected = new WritableImage(oldImage.getPixelReader(), startX, startY, newWidth, newHeight);
            setSelectedImage(imageToBeSelected);
        } catch (Exception e) {
            setLogs(e.getMessage());
        }
    }

    public void doSelection(ActionEvent actionEvent) {
        if (!gointToBeSelected) {
            gointToBeSelected = true;
            setLogs("goingToBeSelected: " + gointToBeSelected);
            selectionImageView.setImage(new Image("icons/selection_clicked.png"));
            return;
        }
        if (gointToBeSelected) {
            gointToBeSelected = false;
            setLogs("goingToBeSelected: " + gointToBeSelected);
            selectionImageView.setImage(new Image("icons/selection.png"));
            return;
        }
    }

    public void doCut(ActionEvent actionEvent) {
        if (!gointToBeCut) {
            gointToBeCut = true;
            setLogs("goingToBeCut: " + gointToBeCut);
            cutImageView.setImage(new Image("icons/cut_clicked.png"));
            return;
        }
        if (gointToBeCut) {
            gointToBeCut = false;
            setLogs("goingToBeCut: " + gointToBeCut);
            cutImageView.setImage(new Image("icons/cut.png"));
            return;
        }
    }
}
