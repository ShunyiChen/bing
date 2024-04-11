package com.simeon.bing;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ImportController {

    private Stage stage;
    private VBox root;
    @FXML
    private Button importChooseButton;
    @FXML
    private Button startImportButton;
    @FXML
    private TextField importPathField;
    @FXML
    private HBox progressBarBox;

    @FXML
    private void initialize() {
        startImportButton.setDisable(true);
    }

    public void initialize(Stage stage, VBox root) {
        this.stage = stage;
        this.root = root;
        root.getChildren().remove(progressBarBox);
    }

    @FXML
    protected void onImportChooseClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开包文件");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        FileChooser.ExtensionFilter pngExtensionFilter =
                new FileChooser.ExtensionFilter(
                        "数据包文件格式 (.gz)", "*.png", "*jpg");
        fileChooser.getExtensionFilters().add(pngExtensionFilter);
        fileChooser.setSelectedExtensionFilter(pngExtensionFilter);
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null) {
            startImportButton.setDisable(false);
            importPathField.setText(selectedFile.getPath());
        }
    }

    @FXML
    protected void onImportStartClick() {
        root.getChildren().add(1, progressBarBox);
    }

    @FXML
    protected void onCancelClick() {
        stage.close();
    }

    public void reset() {
        root.getChildren().remove(progressBarBox);
        importPathField.setText("");
        startImportButton.setDisable(true);
    }
}
