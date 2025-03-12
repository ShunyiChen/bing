package com.simeon.bing.utils;

import com.simeon.bing.MainApplication;
import com.simeon.bing.ProgressController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;

public class UIUtils {
    private final Stage primaryStage = new Stage();
    private ProgressController controller;

    public UIUtils(Stage stage, Callback<String, String> callback) {
        primaryStage.initOwner(stage);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setMaximized(false);
        primaryStage.setResizable(false);
        primaryStage.setIconified(false);
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("progress-view.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.setScene(new Scene(loader.getRoot()));
        controller = loader.getController();
        controller.setStage(primaryStage);
        controller.setCallback(callback);
    }

    public void show() {
        primaryStage.show();
        controller.start();
    }
}
