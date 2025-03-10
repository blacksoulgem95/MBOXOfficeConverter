package me.sofiavicedomini.mboxconverter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.sofiavicedomini.mboxconverter.controllers.Controller;

import java.io.IOException;

public class SceneManager {
    private Stage stage;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void changeScene(String fxml, Controller.Data data) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        var controller = loader.getController();

        if (controller instanceof Controller) {
            ((Controller<?>) controller).initialize(this, data);
        }

        stage.setScene(new Scene(root));
        stage.show();
    }
}
