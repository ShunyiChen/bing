package com.simeon.bing;

import com.simeon.bing.utils.ParamUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import lombok.Setter;

public class AboutController {
    @FXML
    private Label versionLabel;
    @Setter
    private Stage stage;

    @FXML
    private void initialize() {
        versionLabel.setText(ParamUtils.getValue("SYS_VERSION"));
    }

    @FXML
    private void onCopyAction() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(versionLabel.getText());
        clipboard.setContent(content);
        stage.close();
    }
}