package com.simeon.bing;

import com.simeon.bing.model.User;
import javafx.application.Application;
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

    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("prism.subpixeltext", "false");
        this.stage = stage;
        loginLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(loginLoader.load(), 600, 400);
        registerEnterKey(scene);
//        File style = new File(MainApplication.class.getResource("style.css").getFile());
//        scene.getStylesheets().add(style.toURI().toURL().toExternalForm());

        LoginController loginController = loginLoader.getController();
        loginController.initialize(this);
        // enable style
        stage.setTitle(Constants.SYS_NAME);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * 进入主界面
     *
     * @param loginUser 登录用户
     */
    public void enter(User loginUser) {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(30, 30);
        BorderPane card = new BorderPane();
//        card.setStyle("-fx-background-color: rgb(52,53,65);");
        card.setLeft(progressIndicator);

        Scene scene = new Scene(card, 1200, 800);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();

        Task<FXMLLoader> task = new Task<>() {
            @Override
            protected FXMLLoader call() throws Exception {
            return new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
            }
        };
        task.setOnSucceeded(event -> {
            FXMLLoader mainLoader = task.getValue();
            try {
                scene.setRoot(mainLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            MainController mainController = mainLoader.getController();
            mainController.initialize(MainApplication.this, loginUser);
        });
        // 启动任务并在窗口中显示
        Thread thread = new Thread(task);
        thread.start();
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
}