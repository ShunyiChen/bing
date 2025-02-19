package com.simeon.bing;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;

public class EditingUserController {
    @FXML
    private RadioButton rbEnabled;
    @FXML
    private RadioButton rbDisabled;
    @FXML
    protected TextField tfUserName;
    @FXML
    protected PasswordField tfPassword;

    private Stage stage;
//    private TableView<User> userTableView;
//    private User user;

    /** 0-insert 1-update */
    @Setter
    private int opt = 0;

//    public void init(Stage stage, TableView<User> userTableView) {
//        this.stage = stage;
//        this.userTableView = userTableView;
//    }
//
//    public void initEdit(User user) {
//        this.user = user;
//        tfUserName.setText(user.getUserName());
//        tfPassword.setText(user.getPassword());
//        rbEnabled.setSelected(user.isEnabled());
//    }

    public void cleanFields() {
        tfUserName.setText("");
        tfPassword.setText("");
        rbEnabled.setSelected(true);
    }

    @FXML
    protected void onOkAction() {
        if(StringUtils.isEmpty(tfUserName.getText().trim()) || StringUtils.isEmpty(tfPassword.getText())) {
            Alert alert = new Alert (Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText("用户名或密码不能为空");
            alert.setContentText("");
            alert.show();
            return;
        }
        // insert
        if(opt == 0) {
            if(addExistCheck(tfUserName.getText().trim())) {
                Alert alert = new Alert (Alert.AlertType.WARNING);
                alert.setTitle("提示");
                alert.setHeaderText("用户名已存在");
                alert.setContentText("");
                alert.show();
            } else {
                handleAdd();
                stage.close();
            }
        }
        // update
        else {
//            if(updateExistCheck(tfUserName.getText().trim(), user.getUserName())) {
//                Alert alert = new Alert (Alert.AlertType.WARNING);
//                alert.setTitle("提示");
//                alert.setHeaderText("用户名已存在");
//                alert.setContentText("");
//                alert.show();
//            } else {
//                handleUpdate();
//                stage.close();
//            }
        }
    }

    @FXML
    protected void onCancelAction() {
        stage.close();
    }

    private boolean addExistCheck(String userName) {
//        try {
//            return UserDAO.hasExist(userName, null);
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
        return true;
    }

    private boolean updateExistCheck(String newUserName, String oldUserName) {
//        try {
//            return UserDAO.hasExist(newUserName, oldUserName);
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
        return true;
    }

    private void handleAdd() {
//        try {
//            Long id = UserDAO.insertUser(tfUserName.getText().trim(), tfPassword.getText(), rbEnabled.isSelected());
//            User user = new User(id, tfUserName.getText().trim(), tfPassword.getText(), rbEnabled.isSelected());
//            userTableView.getItems().add(user);
//            userTableView.refresh();
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void handleUpdate() {
//        try {
//            User user = userTableView.getSelectionModel().getSelectedItem();
//            user.setUserName(tfUserName.getText());
//            user.setPassword(tfPassword.getText());
//            user.setEnabled(rbEnabled.isSelected());
//            UserDAO.updateUser(user);
//            userTableView.refresh();
//
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }
}
