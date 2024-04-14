package com.simeon.bing;

import com.simeon.bing.model.User;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    static KeyCodeCombination KCC = new KeyCodeCombination(KeyCode.ENTER);
    public Stage stage;
    private FXMLLoader loginLoader;
    private final ProgressIndicator progressIndicator = new ProgressIndicator();

    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("prism.subpixeltext", "false");
        this.stage = stage;
        loginLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(loginLoader.load());
        registerEnterKey(scene);
        LoginController loginController = loginLoader.getController();
        loginController.initialize(this);
        // enable style
        stage.setTitle(Constants.SYS_NAME);
        stage.setScene(scene);
        stage.show();
    }

    public void initMainApp() {
        stage.hide();
        BorderPane card = new BorderPane();
        progressIndicator.setMaxSize(30, 30);
        card.setLeft(progressIndicator);
        Scene scene = new Scene(card, 1200, 800);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * 进入主界面
     *
     * @param loginUser 登录用户
     */
    public void enter(User loginUser) {
        final ServiceExample serviceExample = new ServiceExample();

        //Here you tell your progress indicator is visible only when the service is runing
        progressIndicator.visibleProperty().bind(serviceExample.runningProperty());
        serviceExample.setOnSucceeded(workerStateEvent -> {
            FXMLLoader loader = serviceExample.getValue();   //here you get the return value of your service
            BorderPane root;
            try {
                root = loader.load();
                stage.getScene().setRoot(root);
                MainController mainController = loader.getController();
                mainController.initialize(MainApplication.this, loginUser);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        serviceExample.setOnFailed(workerStateEvent -> {
            //DO stuff on failed
        });
        serviceExample.restart(); //here you start your service
    }

    /**
     * 按回车键登录
     *
     * @param scene
     */
    private void registerEnterKey(Scene scene) {
        LoginController controller = loginLoader.getController();
        scene.getAccelerators().put(
                KCC,
                controller::onSignInClick
        );
    }

    /**
     * 注销Enter键登录
     *
     * @param scene
     */
    public void unregisterEnterKey(Scene scene) {
        System.out.println("unregistered enter key");
        scene.getAccelerators().remove(KCC);
    }

    public static void main(String[] args) {
        launch();
    }

    static class ServiceExample extends Service<FXMLLoader> {
        @Override
        protected Task<FXMLLoader> createTask() {
            return new Task<>() {
                @Override
                protected FXMLLoader call() throws Exception {
                    //DO YOU HARD STUFF HERE
                    Thread.sleep(1000);
                    return new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
                }
            };
        }
    }
}

