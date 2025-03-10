package me.sofiavicedomini.mboxconverter;

import javafx.application.Application;
import javafx.stage.Stage;
import me.sofiavicedomini.mboxconverter.controllers.HomeController;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {
    public static SceneManager sceneManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        sceneManager = new SceneManager(primaryStage);
        sceneManager.changeScene(HomeController.FXML, null);  // Prima schermata
    }

    public static void main(String[] args) {
        launch(args);
    }
}