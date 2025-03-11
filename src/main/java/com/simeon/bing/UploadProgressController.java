package com.simeon.bing;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UploadProgressController {
    private Stage stage;
    private Callback<Void, Void> refreshTable;
    @FXML
    protected ProgressBar progressBar;
    @FXML
    protected Label msgLabel;
    @FXML
    protected Button btnOk;
    @FXML
    protected TextArea logTextArea;

    @FXML
    private void initialize() {
    }

    @FXML
    protected void handleOk(){
        stage.close();
        if(refreshTable != null) {
            refreshTable.call(null);
        }
    }
}
