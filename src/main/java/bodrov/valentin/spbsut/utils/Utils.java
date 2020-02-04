package bodrov.valentin.spbsut.utils;

import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class Utils {

    public static String showUrlInputTextDialog() {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Open URL");
        dialog.setHeaderText("Enter your URL:");
        dialog.setContentText("URL:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {

        });
        return dialog.getEditor().getText();
    }

}
