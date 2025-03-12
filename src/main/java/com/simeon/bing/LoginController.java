package com.simeon.bing;

import com.simeon.bing.request.Login;
import com.simeon.bing.response.GetUserInfoRes;
import com.simeon.bing.response.LoginResponse;
import com.simeon.bing.utils.AESUtils;
import com.simeon.bing.utils.HttpUtil;
import com.simeon.bing.utils.JsonUtil;
import com.simeon.bing.utils.YamlUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginController {
    private MainApplication app;
    @FXML
    protected HBox headerPane;
    @FXML
    private CheckBox remember;
    @FXML
    private TextField userName;
    @FXML
    private TextField password;
    @FXML
    private Button signIn;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label passLabel;

    @FXML
    private void initialize() {
        FontIcon userIcon = new FontIcon("mdal-account_box");
        userIcon.setIconSize(16);
        userNameLabel.setGraphic(userIcon);

        FontIcon passIcon = new FontIcon("mdmz-vpn_key");
        passIcon.setIconSize(16);
//        passIcon.setFill(Constants.primaryColor);
        passLabel.setGraphic(passIcon);


        String yamlPath = System.getProperty("user.home") + "/" + "bing.yaml";
        try {
            Map<String, String> parameters = YamlUtils.loadFromYaml(yamlPath, HashMap.class);
            userName.setText(parameters.get(Settings.REMEMBER_USERNAME_KEY));
            if(!userName.getText().equals("")) {
                remember.setSelected(true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(() -> {
            Image image  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bg.jpg")));
            // 设置背景图像
            // 创建背景图片
            BackgroundImage backgroundImage = new BackgroundImage(
                image ,
                BackgroundRepeat.NO_REPEAT, // 不重复
                BackgroundRepeat.NO_REPEAT, // 不重复
                BackgroundPosition.CENTER,  // 居中
                new BackgroundSize(2000, 190, true, true, true, true) // 背景大小
            );
            // 设置背景
            headerPane.setBackground(new Background(backgroundImage));

            signIn.requestFocus();
        });
    }

    @FXML
    public void onSignInClick() {
        if(userName.getText().trim().equals("") || password.getText().trim().equals("")) {
            Alert alert = new Alert (Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.initOwner(app.stage);
            alert.setHeaderText("用户名或密码不能为空");
            alert.setContentText("");
            alert.show();
        } else {
            try {
                String name = AESUtils.complexAESEncrypt(userName.getText().trim());
                String pwd = AESUtils.complexAESEncrypt(password.getText().trim());
                Login data = new Login(name, pwd, "true"); // 假设有一个Java对象
                String jsonInputString = JsonUtil.toJson(data); // 将对象转换为JSON字符串
                String response = HttpUtil.sendPostRequest(APIs.LOGIN, jsonInputString, "");
                LoginResponse res = JsonUtil.fromJson(response, LoginResponse.class);
                if(res.getData() == null) {
                    Alert alert = new Alert (Alert.AlertType.INFORMATION);
                    alert.setTitle("提示");
                    alert.initOwner(app.stage);
                    alert.setHeaderText("用户名或密码输入不正确");
                    alert.setContentText("");
                    alert.show();
                    return;
                }
                // 保存token
                TokenStore.saveToken(res.getData().getAccess_token());

                response = HttpUtil.sendGetRequest(APIs.GET_USER_INFO, res.getData().getAccess_token());
                GetUserInfoRes resp = JsonUtil.fromJson(response, GetUserInfoRes.class);
                // 保存用户信息
                UserInfoStore.setUserName(resp.getUser().getUserName());
                // 检查License是否有效
                if(!checkLicense(resp.getUser().getUserName())) {
                    Alert alert = new Alert (Alert.AlertType.INFORMATION);
                    alert.setTitle("提示");
                    alert.initOwner(app.stage);
                    alert.setHeaderText("登录错误");
                    alert.setContentText("");
                    alert.show();
                    return;
                }
                // 加载本地yaml配置
                loadLocalSettings();

                app.unregisterEnterKey(app.stage.getScene());
                //打开主界面
                app.openMainWindow();
                app.runLoadingTask();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过用户名检查License是否有效
     *
     * @param userName
     * @return
     */
    private boolean checkLicense(String userName) {
        String licenseInfo = LicenseStore.getEncryptedLicenseInfo(userName);
        if(!licenseInfo.isEmpty()) {
            licenseInfo = AESUtils.complexAESDecrypt(licenseInfo);
            String expiration = licenseInfo.substring(licenseInfo.indexOf("|") + 1);
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date expirationDate = f.parse(expiration);
                if(new Date().compareTo(expirationDate) > 0) {
                    return false;
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return true;
        } else {
            return false;
        }
    }

    @FXML
    public void onQuitClick() {
        System.exit(0);
    }

    /**
     * 创建配置文件
     */
    private void loadLocalSettings() {
        String yamlPath = System.getProperty("user.home") + "/" + "bing.yaml";
        try {
            Map<String, String> parameters = YamlUtils.loadFromYaml(yamlPath, HashMap.class);
            Settings.LOCAL_STORAGE_PATH = parameters.get(Settings.LOCAL_STORAGE_PATH_KEY);
            Settings.REMOTE_STORAGE_PATH = parameters.get(Settings.REMOTE_STORAGE_PATH_KEY);
            Settings.CAPTURE_PLUGIN = parameters.get(Settings.CAPTURE_PLUGIN_KEY);

            if(remember.isSelected()) {
                parameters.put(Settings.REMEMBER_USERNAME_KEY, userName.getText());
            } else {
                parameters.put(Settings.REMEMBER_USERNAME_KEY, "");
            }
            YamlUtils.saveToYaml(parameters, yamlPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize(MainApplication app) {
        this.app = app;
    }
}