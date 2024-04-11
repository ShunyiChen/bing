package com.simeon.bing;

import com.simeon.bing.dao.CategoryDAO;
import com.simeon.bing.dao.SubCategoryDAO;
import com.simeon.bing.dao.UserDAO;
import com.simeon.bing.model.Category;
import com.simeon.bing.model.SubCategory;
import com.simeon.bing.model.User;
import com.simeon.bing.utils.AuthUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;

/**
 * 参数设置controller
 */
public class ParamSettingsController {
    @FXML
    protected TableView<User> userTableView;
    @FXML
    protected TableColumn<User, String> colUserName;
    @FXML
    protected TableColumn<User, String> colPassword;
    @FXML
    protected TableColumn<User, String> colEnabled;


    @FXML
    protected TableView<SubCategory> subCategoriesTableView;
    @FXML
    protected TableColumn<SubCategory, String> colSubCategoryName;
    @FXML
    protected TableColumn<SubCategory, String> colCategoryRFE;
    @FXML
    protected TableColumn<SubCategory, String> colCategoryName;
    @FXML
    protected ComboBox<Category> categoryComboBox;


    private Stage editingUserStage = new Stage();
    private Stage editingCategoryStage = new Stage();
    private Stage editingSubCategoryStage = new Stage();
    private EditingUserController editingUserController;
    private EditingCategoryController editingCategoryController;

    private EditingSubCategoryController editingSubCategoryController;


    @FXML
    private void initialize() {
        initEditingUserStage();
        initEditingUserTable();
        initSubCategoryTable();
        initCategoryComboBox();
        initEditingCategoryStage();
        initEditingSubCategoryStage();
    }

    private void initCategoryComboBox() {
        List<Category> list;
        categoryComboBox.getItems().clear();
        try {
            list = CategoryDAO.findAllCategories();
            categoryComboBox.getItems().addAll(list);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void initEditingUserStage() {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("editing-user.fxml"));
        BorderPane root;
        try {
            root = loader.load();
            editingUserController = loader.getController();
            editingUserController.init(editingUserStage, userTableView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        File style = new File("./css/style.css");
        try {
            scene.getStylesheets().add(style.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        editingUserStage.setScene(scene);
        editingUserStage.initModality(Modality.APPLICATION_MODAL);
        editingUserStage.setTitle("编辑用户信息");
        editingUserStage.setResizable(false);
    }

    private void initEditingSubCategoryStage() {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("editing-subcategory.fxml"));
        BorderPane root;
        try {
            root = loader.load();
            editingSubCategoryController = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        File style = new File("./css/style.css");
        try {
            scene.getStylesheets().add(style.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        editingSubCategoryStage.setScene(scene);
        editingSubCategoryStage.initModality(Modality.APPLICATION_MODAL);
        editingSubCategoryStage.setTitle("编辑子分类");
        editingSubCategoryStage.setResizable(false);
    }

    private void initEditingCategoryStage() {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("editing-category.fxml"));
        BorderPane root;
        try {
            root = loader.load();
            editingCategoryController = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        File style = new File("./css/style.css");
        try {
            scene.getStylesheets().add(style.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        editingCategoryStage.setScene(scene);
        editingCategoryStage.initModality(Modality.APPLICATION_MODAL);
        editingCategoryStage.setTitle("编辑分类");
        editingCategoryStage.setResizable(false);
    }

    private void initSubCategoryTable() {
        colSubCategoryName.setCellValueFactory(param -> {
            if(param.getValue().getName() == null) {
                return new SimpleObjectProperty<>("");
            } else {
                return new SimpleObjectProperty<>(param.getValue().getName());
            }
        });
        colCategoryRFE.setCellValueFactory(param -> {
            if(param.getValue().getRFE()) {
                return new SimpleStringProperty("是");
            } else {
                return new SimpleStringProperty("否");
            }
        });
        colCategoryName.setCellValueFactory(param -> {
            if(param.getValue().getCategoryName() == null) {
                return new SimpleStringProperty("");
            } else {
                return new SimpleStringProperty(param.getValue().getCategoryName());
            }
        });
        onSubCategoryRefreshAction();
    }

    private void initEditingUserTable() {
        colUserName.setCellValueFactory(param -> {
            if(param.getValue().getUserName() == null) {
                return new SimpleObjectProperty<>("");
            } else {
                return new SimpleObjectProperty<>(param.getValue().getUserName());
            }
        });
        colPassword.setCellValueFactory(param -> {
            if(param.getValue().getPassword() == null) {
                return new SimpleObjectProperty<>("");
            } else {
                return new SimpleObjectProperty<>(param.getValue().getPassword());
            }
        });
        colEnabled.setCellValueFactory(param -> {
            if(param.getValue().isEnabled()) {
                return new SimpleStringProperty("是");
            } else {
                return new SimpleStringProperty("否");
            }
        });
        onUserRefreshAction();
    }

    @FXML
    protected void onAddUserAction() {
        editingUserController.setOpt(0);
        editingUserController.cleanFields();
        editingUserStage.show();
    }

    @FXML
    protected void onEditUserAction() {
        User user = userTableView.getSelectionModel().getSelectedItem();
        if(user == null) {
            Alert alert = new Alert (Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText("请选择一个用户");
            alert.setContentText("");
            alert.show();
        } else {
            editingUserController.setOpt(1);
            editingUserController.initEdit(user);
            editingUserStage.show();
        }
    }

    @FXML
    protected void onRemoveUserAction() {
        User user = userTableView.getSelectionModel().getSelectedItem();
        if(user == null) {
            Alert alert = new Alert (Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText("请选择一个用户");
            alert.setContentText("");
            alert.show();
        } else {
            if(user.getUserName().equals("admin")) {
                Alert alert = new Alert (Alert.AlertType.WARNING);
                alert.setTitle("提示");
                alert.setHeaderText("管理员账号不允许删除");
                alert.setContentText("");
                alert.show();
            } else {
                Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION, "");
                alertConfirm.setHeaderText("请确认是否要删除该用户?");
                alertConfirm.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                    try {
                        UserDAO.deleteUser(user.getId());
                        userTableView.getItems().remove(user);
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });

            }
        }
    }

    @FXML
    protected void onUserRefreshAction() {
        userTableView.getItems().clear();
        try {
            List<User> userList = UserDAO.findAllUsers();
            userTableView.getItems().addAll(userList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        userTableView.refresh();
    }

    @FXML
    protected void onSubCategoryRefreshAction() {
        subCategoriesTableView.getItems().clear();
        Category category = categoryComboBox.getSelectionModel().getSelectedItem();
        if(category != null) {
            try {
                List<SubCategory> list = SubCategoryDAO.findAllSubCategories(category.getId());
                subCategoriesTableView.getItems().addAll(list);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    protected void onAddSubCategoryAction() {
        Category category = categoryComboBox.getSelectionModel().getSelectedItem();
        if(category == null) {
            Alert alert = new Alert (Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText("选择一个上级分类");
            alert.setContentText("");
            alert.show();
        }
        else {
            editingSubCategoryController.setOpt(0);
            editingSubCategoryController.init(editingSubCategoryStage, subCategoriesTableView, category);
            editingSubCategoryController.cleanFields();
            editingSubCategoryStage.show();
        }
    }

    @FXML
    protected void onEditSubCategoryAction() {
        Category category = categoryComboBox.getSelectionModel().getSelectedItem();
        if(category == null) {
            Alert alert = new Alert (Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText("选择一个上级分类");
            alert.setContentText("");
            alert.show();
        } else {
            editingSubCategoryController.setOpt(1);
            SubCategory subCategory = subCategoriesTableView.getSelectionModel().getSelectedItem();
            if(subCategory == null) {
                Alert alert = new Alert (Alert.AlertType.WARNING);
                alert.setTitle("提示");
                alert.setHeaderText("选择一个子分类");
                alert.setContentText("");
                alert.show();
            } else {
                editingSubCategoryController.initEdit(editingSubCategoryStage, subCategoriesTableView, subCategory);
                editingSubCategoryStage.show();
            }
        }
    }

    @FXML
    protected void onRemoveSubCategoryAction() {
        SubCategory subCategory = subCategoriesTableView.getSelectionModel().getSelectedItem();
        if(subCategory == null) {
            Alert alert = new Alert (Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText("请选择一个子分类");
            alert.setContentText("");
            alert.show();
        } else {
            Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION, "");
            alertConfirm.setHeaderText("请确认是否要删除该子分类?");
            alertConfirm.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                try {
                    SubCategoryDAO.deleteSubCategory(subCategory.getId());
                    subCategoriesTableView.getItems().remove(subCategory);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @FXML
    protected void onCategoryChangeAction() {
        onSubCategoryRefreshAction();
    }

    @FXML
    protected void onCategoryMgtAction() {
        editingCategoryStage.show();
    }
}
