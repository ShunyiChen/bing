package com.simeon.bing.utils;

import com.simeon.bing.MainApplication;
import com.simeon.bing.ProgressController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;

public class UIUtils {
    private  Stage progressStage = new Stage(StageStyle.UNDECORATED);

    public UIUtils(Callback<String, String> callback, String info) {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("progress-view.fxml"));
        BorderPane root;
        try {
            root = loader.load();
            ProgressController controller = loader.getController();
            controller.setStage(progressStage);
            controller.setCallback(callback);
            controller.getLabelInfo().setText(info);
            Scene scene = new Scene(root);
            progressStage.setScene(scene);
            progressStage.initModality(Modality.APPLICATION_MODAL);
            progressStage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loading() {
        RefUtils.labelState.setText("开始执行");
        progressStage.show();
    }
}
