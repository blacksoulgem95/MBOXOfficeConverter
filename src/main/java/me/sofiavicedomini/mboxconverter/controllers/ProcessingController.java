package me.sofiavicedomini.mboxconverter.controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import me.sofiavicedomini.mboxconverter.SceneManager;
import me.sofiavicedomini.mboxconverter.services.Logger;
import me.sofiavicedomini.mboxconverter.services.MboxConversionService;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class ProcessingController extends Controller<ProcessingController.ProcessingData> {

    public static final String FXML = "views/Processing.fxml";

    @Builder
    @lombok.Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class ProcessingData extends Controller.Data {
        private String mboxFile;
        private String outputPath;
    }

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    @FXML
    private Label percentageLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private TextArea logging;

    private Task<File> currentTask;

    private final Logger operationLogger = new Logger();

    private final MboxConversionService mboxConversionService = new MboxConversionService(operationLogger);

    @Override
    public void initialize(SceneManager sceneManager, Controller.Data data) {
        super.initialize(sceneManager, data);
        // Al caricamento della schermata, puoi avviare il task
        startProcessingTask();
    }

    private void startProcessingTask() {
        currentTask = mboxConversionService.convertMboxToMSGList(data.getMboxFile(), data.getOutputPath());

        // Collega le proprietà del task agli elementi dell'interfaccia
        progressBar.progressProperty().bind(currentTask.progressProperty());
        progressIndicator.progressProperty().bind(currentTask.progressProperty());
        statusLabel.textProperty().bind(currentTask.messageProperty());

        operationLogger.addLogListener(update -> {
            logging.setText(String.join("\n", update.getHistory()));
        });

        // Aggiorna la percentuale
        currentTask.progressProperty().addListener((obs, oldVal, newVal) -> {
            int percentage = (int) Math.round(newVal.doubleValue() * 100);
            percentageLabel.setText(percentage + "%");
        });

        // Azione da eseguire al termine del task
        currentTask.setOnSucceeded(e -> {
            try {
                // Naviga alla prossima schermata quando l'elaborazione è completata
                sceneManager.changeScene(EndController.FXML, EndController.EndData.builder()
                        .filePath(data.getOutputPath())
                        .fileTask(currentTask)
                        .taskLogger(operationLogger)
                        .build());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Avvia il task in un thread separato
        Thread thread = new Thread(currentTask);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void cancelOperation(ActionEvent event) {
        if (currentTask != null) {
            currentTask.cancel();
        }

        try {
            // Torna alla schermata precedente
            sceneManager.changeScene("views/Home.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}