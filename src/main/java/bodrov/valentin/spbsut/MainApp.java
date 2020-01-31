package bodrov.valentin.spbsut;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = "/fxml/hello.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(
                getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("JavaFX Image Processor");
        stage.setScene(new Scene(root));
        stage.show();
    }
}