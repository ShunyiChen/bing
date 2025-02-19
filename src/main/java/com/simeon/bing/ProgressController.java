package com.simeon.bing;

import com.simeon.bing.utils.RefUtils;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;

public class ProgressController {
    @Getter
    @FXML
    private Label labelInfo;
    @FXML
    private ProgressBar progressBar;
    @Setter
    private Stage stage;

    @Setter
    private Callback<String, String>  callback;

    @FXML
    private void initialize() {
        final ServiceExample serviceExample = new ServiceExample();
        //Here you tell your progress indicator is visible only when the service is runing
        progressBar.visibleProperty().bind(serviceExample.runningProperty());
        serviceExample.setOnSucceeded(workerStateEvent -> {
            done();
        });
        serviceExample.setOnFailed(workerStateEvent -> {
            //DO stuff on failed
        });
        serviceExample.restart(); //here you start your service
    }

    private void done() {
        stage.close();
        RefUtils.labelState.setText("执行完毕");

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
        alert.setHeaderText(" ".repeat(30)+"执行完毕");
        FontIcon icon = new FontIcon("mdal-done_outline");
        icon.setIconSize(40);
//        icon.setFill(Constants.primaryColor);
        alert.setGraphic(icon);
        alert.show();
    }

    class ServiceExample extends Service<String> {
        @Override
        protected Task<String> createTask() {
            return new Task<>() {
                @Override
                protected String call() throws Exception {
                    return callback.call("");
                }
            };
        }
    }
}
