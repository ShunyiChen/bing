package com.simeon.bing;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


@Setter
@Getter
public class ProgressController {
    @FXML
    private Label labelMsg;
    @FXML
    private ImageView gearIcon;
    @FXML
    private ImageView gearIcon2;
    private Stage stage;
    private Callback<String, String> callback;
    private Task<Void> task;
    @FXML
    private void initialize() {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/gear.png")));
        gearIcon.setImage(image);

        Image image2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/gear2.png")));
        gearIcon2.setImage(image2);

        // 创建一个后台任务
        task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                callback.call("");
                return null;
            }
        };
        // 任务完成后的处理
        task.setOnSucceeded(e -> {
            stage.close(); // 关闭窗口
        });
    }

    public void start() {
        // 创建旋转动画
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), gearIcon);
        rotateTransition.setByAngle(360); // 旋转 360 度
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // 无限循环
        rotateTransition.setAutoReverse(false); // 不反向旋转
        rotateTransition.play(); // 开始动画

        RotateTransition rotateTransition2 = new RotateTransition(Duration.seconds(1), gearIcon2);
        rotateTransition2.setByAngle(-360); // 旋转 360 度
        rotateTransition2.setInterpolator(Interpolator.LINEAR);
        rotateTransition2.setCycleCount(RotateTransition.INDEFINITE); // 无限循环
        rotateTransition2.setAutoReverse(false); // 不反向旋转
        rotateTransition2.play(); // 开始动画
        // 启动任务
        new Thread(task).start();
    }
}
