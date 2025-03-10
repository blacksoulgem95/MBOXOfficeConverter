package me.sofiavicedomini.mboxconverter.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;
import me.sofiavicedomini.mboxconverter.SceneManager;

import java.io.File;
import java.io.IOException;

import static me.sofiavicedomini.mboxconverter.Utils.areValidString;

public class HomeController extends Controller<Controller.NoData> {

    public static final String FXML = "views/Home.fxml";

    @FXML
    private TextField mboxPath;

    @FXML
    private TextField outputPath;

    @FXML
    private Button confirmBtn;

    @Setter
    private Stage stage;

    @Override
    public void initialize(SceneManager sceneManager, Controller.Data data) {
        super.initialize(sceneManager, data);
        updateConfirmEnabled();
    }

    @FXML
    private void selectMboxFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MBOX Files", "*.mbox"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            mboxPath.setText(file.getAbsolutePath());
        }

        updateConfirmEnabled();
    }

    @FXML
    private void selectOutputDirectory() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        File file = fileChooser.showDialog(stage);
        if (file != null) {
            outputPath.setText(file.getAbsolutePath());
        }

        updateConfirmEnabled();
    }

    private void updateConfirmEnabled() {
        var pstFile = this.outputPath.getText();
        var mboxFile = this.mboxPath.getText();

        this.confirmBtn.setDisable(!areValidString(pstFile, mboxFile));
    }

    @FXML
    private void confirmSelection() throws IOException {
        String mboxFile = mboxPath.getText();
        String ouputPath = outputPath.getText();

        if (mboxFile.isEmpty() || ouputPath.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Errore");
            alert.setContentText("Seleziona entrambi i file prima di confermare.");

            alert.show();

            System.err.println("Seleziona entrambi i file prima di confermare.");
        } else {
            System.out.println("MBOX selezionato: " + mboxFile);
            System.out.println("Cartella di Output: " + ouputPath);
            this.sceneManager.changeScene(
                    ProcessingController.FXML,
                    ProcessingController.ProcessingData.builder()
                            .mboxFile(mboxFile)
                            .outputPath(ouputPath)
                            .build());
        }
    }
}
