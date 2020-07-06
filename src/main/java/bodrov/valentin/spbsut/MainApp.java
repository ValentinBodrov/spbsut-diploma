package bodrov.valentin.spbsut;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the main executable class
 */
public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        String fxmlFile = "/fxml/application.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(
                getClass().getResourceAsStream(fxmlFile));
        root.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
        stage.setTitle("JavaFX Image Processor");
        stage.setScene(new Scene(root));
        stage.show();
    }
}