package com.simeon.bing;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
//6.24.3
//import com.teamdev.jxbrowser.chromium.be;

public class MainApplication extends Application {

//    static {
//        try {
//            Field e = be.class.getDeclaredField("e");
//            e.setAccessible(true);
//            Field f = be.class.getDeclaredField("f");
//            f.setAccessible(true);
//
//            Method getDeclaredFields0 = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
//            getDeclaredFields0.setAccessible(true);
//            Field[] fields = (Field[]) getDeclaredFields0.invoke(Field.class, false);
//            Field modifiers = null;
//            for (Field each : fields) {
//                if ("modifiers".equals(each.getName())) {
//                    modifiers = each;
//                }
//            }
//            modifiers.setAccessible(true);
//            modifiers.setInt(e, e.getModifiers() & ~Modifier.FINAL);
//            modifiers.setInt(f, f.getModifiers() & ~Modifier.FINAL);
//            e.set(null, new BigInteger("1"));
//            f.set(null, new BigInteger("1"));
//            modifiers.setAccessible(false);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//    }

    static KeyCodeCombination KCC = new KeyCodeCombination(KeyCode.ENTER);
    public Stage stage;
    private FXMLLoader loginLoader;
    private final ProgressIndicator progressIndicator = new ProgressIndicator();

    private static final Logger logger = LogManager.getLogger("bing");


    @Override
    public void start(Stage stage) throws IOException {
//        30天
//                System.setProperty("jxbrowser.license.key", "OK6AEKNYF1GLZBS9DGMMCVAEVGUAQGQJBDSPYCNGOVXIVLGTUGJM6BYDTYSYE8HZTKJP21L4H23SH2SKFUGTG3KDCDWV614TCPN5QK98WN9Z6NUE9F9KXF0HKG440Y1LRPNNI4NW15CM1FLXT");
//        帆软
//        7.22
        System.setProperty("jxbrowser.license.key","1BNDIEOFAZ1Z8R8VNNG4W07HLC9173JJW3RT0P2G9Y28L9YFFIWDBRFNFLFDQBKXAHO9ZE");

        logger.info("Application Starting");
        System.setProperty("prism.subpixeltext", "false");
        this.stage = stage;
        this.stage.setResizable(false);
        this.stage.setMaximized(false);
        loginLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(loginLoader.load());
        registerEnterKey(scene);
        LoginController loginController = loginLoader.getController();
        loginController.initialize(this);
        stage.setOnCloseRequest(e -> System.exit(0));
        // enable style
//        stage.setTitle(Constants.SYS_NAME);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        System.exit(0);
//        JPAUtils.closeEntityManagerFactory();
        logger.debug("Application stopped");
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
     */
    public void enter() {
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
                mainController.initialize(MainApplication.this);
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

