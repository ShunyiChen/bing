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
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginController {
    private MainApplication app;
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
    private Label sysTitle;
    @FXML
    private Label versionLabel;

    @FXML
    private void initialize() {
        versionLabel.setText("1.0.0");

//        FontIcon titleIcon = new FontIcon("mdal-cloud_upload");
//        titleIcon.setIconSize(30);
//        titleIcon.setFill(Color.BLUE);
//        sysTitle.setGraphic(titleIcon);

        FontIcon userIcon = new FontIcon("mdal-account_box");
        userIcon.setIconSize(16);
//        userIcon.setFill(Constants.primaryColor);
        userNameLabel.setGraphic(userIcon);

        FontIcon passIcon = new FontIcon("mdmz-vpn_key");
        passIcon.setIconSize(16);
//        passIcon.setFill(Constants.primaryColor);
        passLabel.setGraphic(passIcon);

        Platform.runLater(() -> {
            signIn.requestFocus();
        });
    }

    @FXML
    public void onSignInClick() {
        if(userName.getText().trim().equals("") || password.getText().trim().equals("")) {
            Alert alert = new Alert (Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize(MainApplication app) {
        this.app = app;
//        //设置记住用户名选中
//        if(Constants.YAML_CONF.exists()) {
//            try {
//                Map<String, Object> conf = YamlUtil.read(Constants.YAML_CONF);
//                remember.setSelected((Boolean) conf.get("remember"));
//                if((Boolean)conf.get("remember")) {
//                    userName.setText((String) conf.get("userName"));
//                    password.setText((String) conf.get("password"));
//                }
//            } catch (FileNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
}