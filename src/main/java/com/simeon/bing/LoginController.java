package com.simeon.bing;

import com.simeon.bing.dao.UserDAO;
import com.simeon.bing.domain.User;
import com.simeon.bing.utils.ParamUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.SQLException;
import java.util.HashMap;
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

        FontIcon titleIcon = new FontIcon("mdal-cloud_upload");
        titleIcon.setIconSize(30);
        titleIcon.setFill(Color.WHITE);
        sysTitle.setGraphic(titleIcon);

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
                loginUser = UserDAO.findUser(userName.getText(), password.getText());
            } catch (SQLException e) {
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setTitle("提示");
                alert.setHeaderText("数据库连接不上");
                alert.setContentText("");
                alert.show();
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(loginUser == null) {
                Alert alert = new Alert (Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                alert.setHeaderText("用户名或密码不正确");
                alert.setContentText("");
                alert.show();
            } else {
                System.out.println("remember.isSelected()="+remember.isSelected());
                //保存配置文件
                Map<String, Object> data = new HashMap<>();
                data.put("remember", remember.isSelected());
                data.put("userName", remember.isSelected()?userName.getText():"");
                data.put("password", remember.isSelected()?password.getText():"");
                saveYamlConfFile(data);


                app.unregisterEnterKey(app.stage.getScene());
                //进入主界面
                app.initMainApp();
                app.enter(loginUser);

            }
        }
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