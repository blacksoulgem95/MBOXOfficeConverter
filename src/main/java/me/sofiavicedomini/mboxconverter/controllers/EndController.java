package me.sofiavicedomini.mboxconverter.controllers;

import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import me.sofiavicedomini.mboxconverter.SceneManager;
import me.sofiavicedomini.mboxconverter.services.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

public class EndController extends Controller<EndController.EndData> {

    public static final String FXML = "views/End.fxml";

    @Builder
    @lombok.Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class EndData extends Controller.Data {
        private Task<File> fileTask;
        private String filePath;
        private Logger taskLogger;
    }

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Label statusLabel;

    @FXML
    private TextArea logging;

    @Override
    public void initialize(SceneManager sceneManager, Data data) {
        super.initialize(sceneManager, data);
        // Collega le proprietà del task agli elementi dell'interfaccia
        progressIndicator.progressProperty().bind(this.data.fileTask.progressProperty());
        statusLabel.textProperty().bind(this.data.fileTask.messageProperty());
        logging.setText(String.join("\n", this.data.taskLogger.getLogHistory()));
    }

    public void openFolder() throws IOException {
        Desktop.getDesktop().open(new File(data.filePath));
    }

    public void goHome() throws IOException {
        this.sceneManager.changeScene(HomeController.FXML, null);
    }
}
