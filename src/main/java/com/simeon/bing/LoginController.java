package com.simeon.bing;

import com.simeon.bing.request.Login;
import com.simeon.bing.domain.User;
import com.simeon.bing.response.LoginResponse;
import com.simeon.bing.utils.AESUtils;
import com.simeon.bing.utils.HttpUtil;
import com.simeon.bing.utils.JsonUtil;
import com.simeon.bing.utils.ParamUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Map;

public class LoginController {
    private User loginUser;

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
        ParamUtils.init();
        versionLabel.setText(ParamUtils.getValue("SYS_VERSION"));

//        FontIcon titleIcon = new FontIcon("mdal-cloud_upload");
//        titleIcon.setIconSize(30);
//        titleIcon.setFill(Color.BLUE);
//        sysTitle.setGraphic(titleIcon);

        FontIcon userIcon = new FontIcon("mdal-account_box");
        userIcon.setIconSize(16);
        userIcon.setFill(Constants.primaryColor);
        userNameLabel.setGraphic(userIcon);

        FontIcon passIcon = new FontIcon("mdmz-vpn_key");
        passIcon.setIconSize(16);
        passIcon.setFill(Constants.primaryColor);
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
                String response = HttpUtil.sendPostRequest(APIs.LOGIN, jsonInputString);
                LoginResponse res = JsonUtil.fromJson(response, LoginResponse.class);

                System.out.println("POST Response: " + res.getData().getAccess_token());

                app.unregisterEnterKey(app.stage.getScene());
                //进入主界面
                app.initMainApp();
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
    private void saveYamlConfFile(Map<String, Object> data) {
//        if(!Constants.YAML_CONF.exists()) {
//            File home = new File("./preference");
//            home.mkdir();
//        }
//        try {
//            YamlUtil.write(data, Constants.YAML_CONF);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
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