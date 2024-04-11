package com.simeon.bing;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ExportController {

    private Stage stage;
    private VBox root;
    @FXML
    private Button startExportButton;
    @FXML
    private TextField distPathField;
    @FXML
    private HBox progressBarBox;

    @FXML
    private void initialize() {
    }

    public void initialize(Stage stage, VBox root) {
        this.stage = stage;
        this.root = root;
        root.getChildren().remove(progressBarBox);
    }

    @FXML
    protected void onStartExportClick() {
        root.getChildren().add(3, progressBarBox);
    }

    @FXML
    protected void onExportChooseClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开包文件");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.setInitialFileName("数据文件.png");
        FileChooser.ExtensionFilter pngExtensionFilter =
                new FileChooser.ExtensionFilter(
                        "数据包文件格式 (.gz)", "*.png", "*jpg");
        fileChooser.getExtensionFilters().add(pngExtensionFilter);
        fileChooser.setSelectedExtensionFilter(pngExtensionFilter);
        File selectedFile = fileChooser.showSaveDialog(stage);
        if(selectedFile != null) {
            startExportButton.setDisable(false);
            distPathField.setText(selectedFile.getPath());
        }
    }

    @FXML
    protected void onCancelClick() {
        stage.close();
    }

    public void reset() {
        root.getChildren().remove(progressBarBox);
        distPathField.setText("");
        startExportButton.setDisable(true);
    }
}
