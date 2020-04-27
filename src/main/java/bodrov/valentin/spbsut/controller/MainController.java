package bodrov.valentin.spbsut.controller;

import bodrov.valentin.spbsut.processing.NativeProcessing;
import bodrov.valentin.spbsut.processing.OpenCvProcessing;
import bodrov.valentin.spbsut.utils.NoProcessedImageException;
import bodrov.valentin.spbsut.utils.Utils;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public class MainController {

    public ImageView sampleImage;
    public Label statusBar;
    public Label coordinatesBar;
    public Slider redSlider;
    public Slider greenSlider;
    public Slider blueSlider;
    public Spinner<Integer> redSpinner;
    public Spinner<Integer> greenSpinner;
    public Spinner<Integer> blueSpinner;
    public ImageView selectionImageView;
    public ImageView cutImageView;
    public Button cut;
    public ScrollPane centerPane;
    public AnchorPane centerAnchorPane;
    public Slider pixelationSlider;
    public Label pixelationLabel;
    public Slider alphaSlider;
    public Label alphaLabel;
    public Slider betaSlider;
    public Label betaLabel;
    private Image currentProcessedImage;
    private Image originalImage;
    private Image selectedImage;
    private int startX;
    private int startY;
    private int releaseX;
    private int releaseY;
    private Rectangle selectionRectangle;
    private boolean goingToBeSelected = false;

    private int getStartX() {
        return startX;
    }

    private void setStartX(int startX) {
        this.startX = startX;
    }

    private int getStartY() {
        return startY;
    }

    private void setStartY(int startY) {
        this.startY = startY;
    }

    private int getReleaseX() {
        return releaseX;
    }

    private void setReleaseX(int releaseX) {
        this.releaseX = releaseX;
    }

    private int getReleaseY() {
        return releaseY;
    }

    private void setReleaseY(int releaseY) {
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
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
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

    private Image getSelectedImage() {
        return selectedImage;
    }

    private void setSelectedImage(Image selectedImage) {
        this.selectedImage = selectedImage;
    }

    private void setImageToImageView(BufferedImage image) {
        Image imageToImport = SwingFXUtils.toFXImage(image, null);

        sampleImage.setFitWidth(imageToImport.getWidth());
        sampleImage.setFitHeight(imageToImport.getHeight());
        sampleImage.setImage(imageToImport);
        sampleImage.setSmooth(true);
        sampleImage.setCache(true);
        setCurrentProcessedImage(imageToImport);
        centerAnchorPane.getChildren().remove(selectionRectangle);
    }

    private void setLogs(String message) {
        statusBar.setText(message);
    }

    private void setCoordinates(double x, double y) {
        coordinatesBar.setText(String.format("X: %f Y: %f", x, y));
    }

    public void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }

    public void handleDrop(DragEvent dragEvent) throws FileNotFoundException {
        List<File> files = dragEvent.getDragboard().getFiles();
        Image img = new Image(new FileInputStream(files.get(0)));
        setImageToImageView(SwingFXUtils.fromFXImage(img, null));
        setOriginalImage(img);
    }

    public void handleHotKeys(KeyEvent keyEvent) throws NoProcessedImageException {
        KeyCombination saveImageKeyCombination =
                new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        KeyCombination loadImageKeyCombination =
                new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
        KeyCombination copyImageKeyCombination =
                new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
        KeyCombination cancelKeyCombination =
                new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        if (saveImageKeyCombination.match(keyEvent)) {
            savePictureAs();
        }
        if (loadImageKeyCombination.match(keyEvent)) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            if (clipboard.hasImage()) {
                Image image = clipboard.getImage();
                setImageToImageView(SwingFXUtils.fromFXImage(image, null));
                setOriginalImage(image);
            }
        }
        if (copyImageKeyCombination.match(keyEvent)) {
            WritableImage snapshot = sampleImage.snapshot(new SnapshotParameters(), null);
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putImage(snapshot);
            clipboard.setContent(content);
        }
        if (cancelKeyCombination.match(keyEvent)) {
            resetChanges();
        }
    }

    public void openLocal() {
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

            image = ImageIO.read(inputFile);
            setOriginalImage(SwingFXUtils.toFXImage(image, null));
            setImageToImageView(image);
        } catch (Exception e) {
            setLogs(e.getMessage());
            sampleImage.setImage(null);
        }
    }

    public void openUrl() {
        BufferedImage image;
        try {
            String website = Utils.showUrlInputTextDialog();
            if (!website.matches("http(|s)://.*(.([a-zA-z]{2,3})/?).*")) {
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

    public void savePictureAs() {
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

    public void doGreyscale() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage image =
                SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
        BufferedImage greyscaledImage;
        if (getSelectedImage() != null && goingToBeSelected) {
            greyscaledImage = NativeProcessing.getGreyscaledImage(image,
                    startX, startY, releaseX, releaseY);
        } else {
            greyscaledImage = NativeProcessing.getGreyscaledImage(image,
                    0, 0, image.getWidth(), image.getHeight());
        }
        setLogs("The greyscale effect was successfully applied to image");
        setImageToImageView(greyscaledImage);
    }

    public void doSepia() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage image =
                SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
        BufferedImage sepiaImage;
        if (getSelectedImage() != null && goingToBeSelected) {
            sepiaImage = NativeProcessing.getSepiaImage(image,
                    startX, startY, releaseX, releaseY);
        } else {
            sepiaImage = NativeProcessing.getSepiaImage(image,
                    0, 0, image.getWidth(), image.getHeight());
        }
        setLogs("The sepia effect was successfully applied to image");
        setImageToImageView(sepiaImage);
    }

    public void doNegative() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage image =
                SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
        BufferedImage negativeImage;
        if (getSelectedImage() != null && goingToBeSelected) {
            negativeImage = NativeProcessing.getNegativeImage(image,
                    startX, startY, releaseX, releaseY);
        } else {
            negativeImage = NativeProcessing.getNegativeImage(image,
                    0, 0, image.getWidth(), image.getHeight());
        }
        setLogs("The negative effect was successfully applied to image");
        setImageToImageView(negativeImage);
    }

    public void doPixelRandomizing(ActionEvent actionEvent) throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage image =
                SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
        BufferedImage randomizedImage;
        if (getSelectedImage() != null && goingToBeSelected) {
            randomizedImage = NativeProcessing.getRandomPixeledImage(image,
                    startX, startY, releaseX, releaseY);
        } else {
            randomizedImage = NativeProcessing.getRandomPixeledImage(image,
                    0, 0, image.getWidth(), image.getHeight());
        }
        setLogs("The randomizing effect was successfully applied to image");
        setImageToImageView(randomizedImage);
    }

    public void doHorizontalMirroring() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage image =
                SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
        BufferedImage mirroredImage =
                NativeProcessing.getMirroredImage(image, NativeProcessing.HORIZONTAL);
        setLogs("The horizontal mirroring effect was successfully applied to image");
        setOriginalImage(SwingFXUtils.toFXImage(mirroredImage, null));
        setImageToImageView(mirroredImage);
    }

    public void doVerticalMirroring() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage image =
                SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
        BufferedImage mirroredImage =
                NativeProcessing.getMirroredImage(image, NativeProcessing.VERTICAL);
        setLogs("The vertical mirroring effect was successfully applied to image");
        setOriginalImage(SwingFXUtils.toFXImage(mirroredImage, null));
        setImageToImageView(mirroredImage);
    }

    public void rotateRight() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage image =
                SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
        BufferedImage rotatedImage =
                NativeProcessing.getRotatedImage(image, NativeProcessing.RIGHT);
        setLogs("The left rotation effect was successfully applied to image");
        setOriginalImage(SwingFXUtils.toFXImage(rotatedImage, null));
        setImageToImageView(rotatedImage);
    }

    public void rotateLeft() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage image =
                SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
        BufferedImage rotatedImage =
                NativeProcessing.getRotatedImage(image, NativeProcessing.LEFT);
        setLogs("The left rotation effect was successfully applied to image");
        setOriginalImage(SwingFXUtils.toFXImage(rotatedImage, null));
        setImageToImageView(rotatedImage);
    }

    private void changeRedCustom() {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            try {
                throw new NoProcessedImageException();
            } catch (NoProcessedImageException e) {
                e.printStackTrace();
            }
        }
        BufferedImage image =
                SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
        BufferedImage originalImage =
                SwingFXUtils.fromFXImage(getOriginalImage(), null);
        BufferedImage changedImage;
        if (getSelectedImage() != null && goingToBeSelected) {
            changedImage = NativeProcessing.getChangedImage(image,
                    originalImage,
                    startX,
                    startY,
                    releaseX,
                    releaseY,
                    redSlider.getValue(),
                    NativeProcessing.RED);
        } else {
            changedImage = NativeProcessing.getChangedImage(image,
                    originalImage,
                    0, 0,
                    image.getWidth(),
                    image.getHeight(),
                    redSlider.getValue(),
                    NativeProcessing.RED);
        }
        setLogs("The red channel was successfully changed");
        setImageToImageView(changedImage);
    }

    private void changeGreenCustom() {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            try {
                throw new NoProcessedImageException();
            } catch (NoProcessedImageException e) {
                e.printStackTrace();
            }
        }
        BufferedImage image =
                SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
        BufferedImage originalImage =
                SwingFXUtils.fromFXImage(getOriginalImage(), null);
        BufferedImage changedImage;
        if (getSelectedImage() != null && goingToBeSelected) {
            changedImage = NativeProcessing.getChangedImage(image,
                    originalImage,
                    startX,
                    startY,
                    releaseX,
                    releaseY,
                    greenSlider.getValue(),
                    NativeProcessing.GREEN);
        } else {
            changedImage = NativeProcessing.getChangedImage(image,
                    originalImage, 0, 0,
                    image.getWidth(),
                    image.getHeight(),
                    greenSlider.getValue(),
                    NativeProcessing.GREEN);
        }
        setLogs("The green channel was successfully changed");
        setImageToImageView(changedImage);
    }

    private void changeBlueCustom() {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            try {
                throw new NoProcessedImageException();
            } catch (NoProcessedImageException e) {
                e.printStackTrace();
            }
        }
        BufferedImage image =
                SwingFXUtils.fromFXImage(getCurrentProcessedImage(), null);
        BufferedImage originalImage =
                SwingFXUtils.fromFXImage(getOriginalImage(), null);
        BufferedImage changedImage;
        if (getSelectedImage() != null && goingToBeSelected) {
            changedImage =
                    NativeProcessing.getChangedImage(image,
                            originalImage,
                            startX,
                            startY,
                            releaseX,
                            releaseY,
                            blueSlider.getValue(),
                            NativeProcessing.BLUE);
        } else {
            changedImage = NativeProcessing.getChangedImage(image,
                    originalImage,
                    0, 0,
                    image.getWidth(),
                    image.getHeight(),
                    blueSlider.getValue(),
                    NativeProcessing.BLUE);
        }
        setLogs("The blue channel was successfully changed");
        setImageToImageView(changedImage);
    }

    public void resetChanges() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        centerAnchorPane.getChildren().remove(selectionRectangle);
        setImageToImageView(
                SwingFXUtils.fromFXImage(getOriginalImage(), null));
        setLogs("The original image was restored");
    }

    public void applyChanges() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        setOriginalImage(getCurrentProcessedImage());
        setLogs("The changes were applied");
    }

    public void getCoordinates(MouseEvent mouseEvent) {
        setCoordinates(mouseEvent.getX(), mouseEvent.getY());
    }

    public void onPressed(MouseEvent mouseEvent) {
        setStartX((int) mouseEvent.getX());
        setStartY((int) mouseEvent.getY());
    }

    public void onReleased(MouseEvent mouseEvent) throws NoProcessedImageException {
        centerAnchorPane.getChildren().remove(selectionRectangle);
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        if (!goingToBeSelected) {
            try {
                throw new Exception("Click the 'Select'-button first!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setReleaseX((int) mouseEvent.getX());
        setReleaseY((int) mouseEvent.getY());
        if (getStartY() > getReleaseY()) {
            int temp = getStartY();
            setStartY(getReleaseY());
            setReleaseY(temp);
        }
        if (getStartX() > getReleaseX()) {
            int temp = getStartX();
            setStartX(getReleaseX());
            setReleaseX(temp);
        }
        if (getReleaseX() > sampleImage.getFitWidth()) {
            setReleaseX((int) sampleImage.getFitWidth());
        }
        if (getReleaseY() > sampleImage.getFitHeight()) {
            setReleaseY((int) sampleImage.getFitHeight());
        }
        int newWidth = Math.abs(getReleaseX() - getStartX());
        int newHeight = Math.abs(getReleaseY() - getStartY());
        selectionRectangle =
                Utils.getSelectionRectangle(startX,
                        startY, newWidth, newHeight);
        centerAnchorPane.getChildren().add(selectionRectangle);

        Image oldImage = getOriginalImage();
        Image imageToBeSelected =
                new WritableImage(oldImage.getPixelReader(),
                        startX, startY, newWidth, newHeight);
        if (goingToBeSelected) {
            setSelectedImage(imageToBeSelected);
        }
        cut.setOnAction(event -> {
            if (goingToBeSelected) {
                doSelection();
                setImageToImageView(
                        SwingFXUtils.fromFXImage(imageToBeSelected, null));
                setOriginalImage(imageToBeSelected);
            }

        });
    }

    public void doSelection() {
        if (!goingToBeSelected) {
            goingToBeSelected = true;
            selectionImageView.setImage(
                    new Image("icons/selection_clicked.png"));
        } else {
            goingToBeSelected = false;
            selectionImageView.setImage(new Image("icons/selection.png"));
            setSelectedImage(null);
        }
    }

    public void addSimpleSign() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }

        Stage addSignStage = new Stage();
        addSignStage.setTitle("Add Sign");
        addSignStage.setHeight(135);
        addSignStage.setWidth(260);
        addSignStage.setResizable(false);
        HBox upperHBox = new HBox(3);
        Label upperLabel = new Label("Upper string: ");
        TextField upperSignTextField = new TextField();
        upperHBox.getChildren().addAll(upperLabel, upperSignTextField);
        upperHBox.setAlignment(Pos.CENTER);

        HBox bottomHBox = new HBox(3);
        Label bottomLabel = new Label("Bottom string: ");
        TextField bottomSignTextField = new TextField();
        bottomHBox.getChildren().addAll(bottomLabel, bottomSignTextField);
        bottomHBox.setAlignment(Pos.CENTER);

        HBox confirmHBox = new HBox(3);
        ToggleGroup group = new ToggleGroup();
        RadioButton lobster = new RadioButton("Lobster");
        RadioButton impact = new RadioButton("Impact");
        lobster.setToggleGroup(group);
        impact.setToggleGroup(group);
        impact.setSelected(true);
        Button confirm = new Button("Confirm");
        confirmHBox.getChildren().addAll(lobster, impact, confirm);
        confirmHBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(4);
        vBox.getChildren().addAll(upperHBox, bottomHBox, confirmHBox);
        vBox.setAlignment(Pos.CENTER);

        addSignStage.setScene(new Scene(vBox));
        addSignStage.show();

        ChangeListener<String>
                textFieldChangeListener = (observable, oldValue, newValue) -> {
            Font font = null;
            if (lobster.isSelected()) {
                try {
                    File file = null;
                    String resource = "/fonts/lobster.ttf";
                    URL res = getClass().getResource(resource);
                    if (res.getProtocol().equals("jar")) {
                        try {
                            InputStream input = getClass().getResourceAsStream(resource);
                            file = File.createTempFile("tempfile", ".tmp");
                            OutputStream out = new FileOutputStream(file);
                            int read;
                            byte[] bytes = new byte[1024];
                            while ((read = input.read(bytes)) != -1) {
                                out.write(bytes, 0, read);
                            }
                            out.close();
                            file.deleteOnExit();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        file = new File(res.getFile());
                    }
                    font = Font.createFont(
                            Font.TRUETYPE_FONT,
                            file);
                } catch (FontFormatException | IOException e) {
                    e.printStackTrace();
                }
                setSign(upperSignTextField.getText(),
                        bottomSignTextField.getText(), font.deriveFont(48f));
            } else {
                font = new Font("Impact", Font.PLAIN, 40);
                setSign(upperSignTextField.getText(),
                        bottomSignTextField.getText(), font);
            }
        };

        upperSignTextField.textProperty().
                addListener(textFieldChangeListener);
        bottomSignTextField.textProperty().
                addListener(textFieldChangeListener);

        addSignStage.setOnCloseRequest(event -> setImageToImageView(SwingFXUtils.fromFXImage(
                getOriginalImage(), null)));

        confirm.setOnAction(event -> {
            setOriginalImage(sampleImage.getImage());
            setCurrentProcessedImage(sampleImage.getImage());
            addSignStage.close();
        });
    }

    public void addWatermark() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Set watermark text");
        dialog.setHeaderText("Enter your Watermark:");
        dialog.setContentText("Watermark:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
        });
        String watermark = dialog.getEditor().getText();

        BufferedImage imageWithWatermark =
                SwingFXUtils.fromFXImage(getOriginalImage(), null);
        Graphics2D graphics = imageWithWatermark.createGraphics();

        AlphaComposite alphaChannel =
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);
        graphics.setComposite(alphaChannel);
        graphics.setColor(java.awt.Color.BLUE);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fontMetrics = graphics.getFontMetrics();
        Rectangle2D rect =
                fontMetrics.getStringBounds(watermark, graphics);

        int bottomX = (int) (imageWithWatermark.getWidth() * 0.75) -
                (watermark.length() * 20) / 4;
        int bottomY = (int) (imageWithWatermark.getHeight() * 0.9);

        graphics.drawString(watermark, bottomX, bottomY);
        graphics.dispose();

        setImageToImageView(imageWithWatermark);
        setOriginalImage(sampleImage.getImage());
        setCurrentProcessedImage(sampleImage.getImage());
    }

    private void setSign(String upperString, String bottomString, Font font) {
        BufferedImage imageWithFont =
                SwingFXUtils.fromFXImage(getOriginalImage(), null);

        int upperX = (imageWithFont.getWidth() / 2) -
                (upperString.length() * 40) / 4;
        int upperY = (int) (imageWithFont.getHeight() * 0.2);
        int bottomX = (imageWithFont.getWidth() / 2) -
                (bottomString.length() * 40) / 4;
        int bottomY = (int) (imageWithFont.getHeight() * 0.9);

        Graphics2D upperSignGraphics = Utils.getGraphics(imageWithFont,
                upperX, upperY, upperString, font);
        Graphics2D bottomSignGraphics = Utils.getGraphics(imageWithFont,
                bottomX, bottomY, bottomString, font);

        setImageToImageView(imageWithFont);
    }

    public void enhanceContrast() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage enhancedImage = OpenCvProcessing.enhanceContrast(getOriginalImage());
        setImageToImageView(enhancedImage);
        setOriginalImage(SwingFXUtils.toFXImage(enhancedImage, null));
        setLogs("The contrast-enhacing effect was applied");
    }

    public void enhanceBrightness() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        setLogs("Swipe the alpha- and beta-sliders to change the image");
        ChangeListener<Number> changeListener = (observable, oldValue, newValue) -> {
            alphaLabel.setText(String.valueOf((int) alphaSlider.getValue()));
            betaLabel.setText(String.valueOf((int) betaSlider.getValue()));
            BufferedImage enhancedImage = OpenCvProcessing.enhanceBrightness(getOriginalImage(), alphaSlider.getValue(), betaSlider.getValue());
            setImageToImageView(enhancedImage);
            setLogs("The brightness-enhacing effect was applied");
        };
        alphaSlider.valueProperty().addListener(changeListener);
        betaSlider.valueProperty().addListener(changeListener);
    }

    public void enhanceSharpness() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage enhancedImage = OpenCvProcessing.enhanceSharpness(getOriginalImage());
        setImageToImageView(enhancedImage);
        setOriginalImage(SwingFXUtils.toFXImage(enhancedImage, null));
        setLogs("The sharpness-enhacing effect was applied");
    }

    public void doGaussianBlur() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage blurredImage = OpenCvProcessing.doGaussianBlur(getOriginalImage());
        setImageToImageView(blurredImage);
        setOriginalImage(SwingFXUtils.toFXImage(blurredImage, null));
        setLogs("The gaussian-blurring effect was applied");
    }

    public void doMedianBlur() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage blurredImage = OpenCvProcessing.doMedianBlur(getOriginalImage());
        setImageToImageView(blurredImage);
        setOriginalImage(SwingFXUtils.toFXImage(blurredImage, null));
        setLogs("The median-blurring effect was applied");
    }

    public void doPixelation() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        setLogs("Swipe the slider to make image more pixelated");
        pixelationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int pixelationCoefficient = (int) pixelationSlider.getValue();
            pixelationLabel.setText(String.valueOf(pixelationCoefficient));
            BufferedImage pixelatedImage = OpenCvProcessing.doPixelation(getOriginalImage(), pixelationCoefficient);
            setImageToImageView(pixelatedImage);
            setLogs("The pixelation effect was applied");
        });
    }

    public void doBilateralFilter() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage filteredImage = OpenCvProcessing.doBilateralFilter(getOriginalImage());
        setImageToImageView(filteredImage);
        setOriginalImage(SwingFXUtils.toFXImage(filteredImage, null));
        setLogs("The bilateral filter was applied");
    }

    public void doBoxFilter() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage filteredImage = OpenCvProcessing.doBoxFilter(getOriginalImage());
        setImageToImageView(filteredImage);
        setOriginalImage(SwingFXUtils.toFXImage(filteredImage, null));
        setLogs("The box filter was applied");
    }

    public void doSQRBoxFilter() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage filteredImage = OpenCvProcessing.doSQRBoxFilter(getOriginalImage());
        setImageToImageView(filteredImage);
        setOriginalImage(SwingFXUtils.toFXImage(filteredImage, null));
        setLogs("The SQR-box filter was applied");
    }

    public void do2DFilter() throws NoProcessedImageException {
        if (getCurrentProcessedImage() == null) {
            setLogs(NoProcessedImageException.NO_PROCESSED_IMAGE);
            throw new NoProcessedImageException();
        }
        BufferedImage filteredImage = OpenCvProcessing.do2DFilter(getOriginalImage());
        setImageToImageView(filteredImage);
        setOriginalImage(SwingFXUtils.toFXImage(filteredImage, null));
        setLogs("The 2D-filter was applied");
    }

}
