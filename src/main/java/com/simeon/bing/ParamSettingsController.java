package com.simeon.bing;

import com.simeon.bing.dao.CategoryDAO;
import com.simeon.bing.dao.ParamsDAO;
import com.simeon.bing.dao.SubCategoryDAO;
import com.simeon.bing.dao.UserDAO;
import com.simeon.bing.domain.User;
import com.simeon.bing.model.Category;
import com.simeon.bing.model.Settings;
import com.simeon.bing.model.SubCategory;
import com.simeon.bing.utils.ParamUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 参数设置controller
 */
public class ParamSettingsController {

    /**********************************************/
    /***               用户管理                   ***/
    /**********************************************/
    @FXML
    protected TableView<User> userTableView;
    @FXML
    protected TableColumn<User, String> colUserName;
    @FXML
    protected TableColumn<User, String> colPassword;
    @FXML
    protected TableColumn<User, String> colEnabled;
    private Stage editingUserStage = new Stage();
    private EditingUserController editingUserController;
    /**********************************************/
    /***               参数管理                   ***/
    /**********************************************/
    @FXML
    private TextField tfServerHost;
    @FXML
    private TextField tfServerPort;
    @FXML
    private TextField tfServerUserName;
    @FXML
    private PasswordField tfServerPassword;
    @FXML
    private TextField tfFilePath;
    private List<Settings> paramList;
    @FXML
    private Label filePathInfo;
    /**********************************************/
    /***               分类管理                   ***/
    /**********************************************/
    @FXML
    protected TableView<SubCategory> subCategoriesTableView;
    @FXML
    protected TableColumn<SubCategory, String> colSubCategoryName;
    @FXML
    protected TableColumn<SubCategory, String> colSubCategoryRFE;
    @FXML
    protected TableColumn<SubCategory, String> colSubCategoryShortcut;
    @FXML
    protected TreeView<Category> categoryTreeView;
    private Stage editingCategoryStage = new Stage();
    private EditingCategoryController editingCategoryController;
    private List<SubCategory> masterData = new ArrayList<>();

    /**********************************************/
    /***               子分类管理                 ***/
    /**********************************************/
    private Stage editingSubCategoryStage = new Stage();
    private EditingSubCategoryController editingSubCategoryController;
    @FXML
    private TextField subCategoryQueryField;

    @FXML
    private void initialize() {
        initEditingUserStage();
        initEditingUserTable();

        initSettingParams();

        initEditingCategoryStage();
        initCategoryTreeView();

        initEditingSubCategoryStage();
        initSubCategoryTable();
    }

    private void initCategoryTreeView() {
        ContextMenu menu = new ContextMenu();
        MenuItem itemNew = new MenuItem("新建");
        MenuItem itemRM = new MenuItem("删除");
        MenuItem itemRN = new MenuItem("重命名");
        MenuItem itemRE = new MenuItem("刷新");
        menu.getItems().addAll(itemNew, itemRM, itemRN, new SeparatorMenuItem(), itemRE);

        categoryTreeView.setEditable(false);
        categoryTreeView.setContextMenu(menu);
        itemNew.setOnAction(event -> {
            TreeItem<Category> parent = categoryTreeView.getSelectionModel().getSelectedItem();
            if(parent != null) {
                editingCategoryController.setMode("insert");
                editingCategoryController.resetFields();
                editingCategoryStage.show();
                editingCategoryController.focus();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                alert.setHeaderText("选择一个分类");
                alert.show();
            }
        });
        itemRM.setOnAction(event -> {
            Platform.runLater(() ->  {
                TreeItem<Category> item = categoryTreeView.getSelectionModel().getSelectedItem();
                if(item != null) {
                    if(item.getValue().getId() == 0L) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                        alert.setHeaderText("无法删除根分类");
                        alert.show();
                    } else {
                        try {
                            List<Category> list = CategoryDAO.findAllCategories();
                            // 递归删除自节点
                            findDeleteNodes(item, list);
                            // 删除本身
                            CategoryDAO.deleteCategory(item.getValue().getId());
                            TreeItem<Category> parent = item.getParent();
                            parent.getChildren().remove(item);
                            parent.setExpanded(true);
                            subCategoriesTableView.getItems().clear();
                            categoryTreeView.getSelectionModel().select(parent);

                        } catch (SQLException | ClassNotFoundException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                            alert.setHeaderText("数据库连接超时");
                            alert.show();
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        });
        itemRN.setOnAction(event -> {
            TreeItem<Category> item = categoryTreeView.getSelectionModel().getSelectedItem();
            if (item != null) {
                editingCategoryController.setMode("update");
                editingCategoryController.willRename(item.getValue());
                editingCategoryStage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                alert.setHeaderText("选择一个分类");
                alert.show();
            }
        });
        itemRE.setOnAction(event -> {
            Platform.runLater(() ->  {
                categoryTreeView.getRoot().getChildren().clear();
                try {
                    List<Category> data = CategoryDAO.findAllCategories();
                    getNodes(categoryTreeView.getRoot(), data);
                    categoryTreeView.refresh();
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        categoryTreeView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1){
                TreeItem<Category> item = categoryTreeView.getSelectionModel().getSelectedItem();
                subCategoriesTableView.getItems().clear();
                Platform.runLater(() ->  {
                    try {
                        masterData = SubCategoryDAO.findListByCategoryId(item.getValue().getId());
                        subCategoriesTableView.getItems().addAll(masterData);
                        subCategoriesTableView.refresh();
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
        Category root = new Category(0L, "所有分类", null);
        TreeItem<Category> rootTreeItem = new TreeItem<>(root);
        categoryTreeView.setRoot(rootTreeItem);
        try {
            List<Category> data = CategoryDAO.findAllCategories();
            getNodes(rootTreeItem, data);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void getNodes(TreeItem<Category> parent, List<Category> list) {
        list.forEach(sc -> {
            if(sc.getPid().equals(parent.getValue().getId())) {
                TreeItem<Category> node = new TreeItem<>(sc);
                parent.getChildren().add(node);
                parent.setExpanded(true);
                getNodes(node, list);
            }
        });
    }

    private void findDeleteNodes(TreeItem<Category> parent, List<Category> list) {
        list.forEach(sc -> {
            if(sc.getPid().equals(parent.getValue().getId())) {
                try {
                    CategoryDAO.deleteCategory(sc.getId());
                    TreeItem<Category> node = new TreeItem<>(sc);
                    parent.getChildren().remove(node);
                    findDeleteNodes(node, list);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
//            editingSubCategoryController.init(editingSubCategoryStage, categoryTreeView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
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
            editingCategoryController.init(categoryTreeView, editingCategoryStage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        editingCategoryStage.setScene(scene);
        editingCategoryStage.initModality(Modality.APPLICATION_MODAL);
        editingCategoryStage.setTitle("新建分类");
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
        colSubCategoryRFE.setCellValueFactory(param -> {
            if(param.getValue().getRFE()) {
                return new SimpleStringProperty("是");
            } else {
                return new SimpleStringProperty("否");
            }
        });
        colSubCategoryShortcut.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getShortcut()));

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
        Platform.runLater(() ->  {
            try {
                List<User> userList = UserDAO.findAllUsers();
                userTableView.getItems().addAll(userList);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            userTableView.refresh();
        });
    }

    private void initSettingParams() {
        FontIcon pathInfo = new FontIcon("mdal-info");
        pathInfo.setIconSize(20);
        pathInfo.setFill(Color.BLACK);
        filePathInfo.setText("");
        filePathInfo.setGraphic(pathInfo);
        Tooltip tooltip = new Tooltip();
        tooltip.setText("本地暂存空间");
        filePathInfo.setTooltip(tooltip);
        tfServerHost.setText(ParamUtils.getValue("FTP_SERVER_HOST"));
        tfServerPort.setText(ParamUtils.getValue("FTP_PORT"));
        tfServerUserName.setText(ParamUtils.getValue("FTP_LOGIN_USER"));
        tfServerPassword.setText(ParamUtils.getValue("FTP_LOGIN_PASS"));
        tfFilePath.setText(ParamUtils.getValue("LOCAL_FILE_PATH"));
    }

    @FXML
    protected void onSubCategoryRefreshAction() {
        subCategoriesTableView.getItems().clear();
        TreeItem<Category> item = categoryTreeView.getSelectionModel().getSelectedItem();
        if(item != null) {
            Platform.runLater(() ->  {
                try {
                    List<SubCategory> list = SubCategoryDAO.findListByCategoryId(item.getValue().getId());
                    subCategoriesTableView.getItems().addAll(list);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @FXML
    protected void onAddSubCategoryAction() {
        TreeItem<Category> item = categoryTreeView.getSelectionModel().getSelectedItem();
        if(item == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            alert.setHeaderText("选择一个分类");
            alert.show();
        }
        else {
            editingSubCategoryController.setMode("insert");
            editingSubCategoryController.init(subCategoriesTableView, editingSubCategoryStage, item.getValue());
            editingSubCategoryController.cleanFields();
            editingSubCategoryStage.show();
        }
    }

    @FXML
    protected void onEditSubCategoryAction() {
        TreeItem<Category> item = categoryTreeView.getSelectionModel().getSelectedItem();
        if(item == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            alert.setHeaderText("选择一个分类");
            alert.show();
        } else {
            editingSubCategoryController.setMode("update");
            SubCategory subCategory = subCategoriesTableView.getSelectionModel().getSelectedItem();
            if(subCategory == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                alert.setHeaderText("选择一行子分类");
                alert.show();
            } else {
                editingSubCategoryController.willEdit(editingSubCategoryStage, subCategoriesTableView, subCategory);
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
    protected void onQuerySubCategoryAction() {
        TreeItem<Category> item = categoryTreeView.getSelectionModel().getSelectedItem();
        if(item == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            alert.setHeaderText("选择一个分类");
            alert.show();
        } else {
            List<SubCategory> filtered = masterData.stream().filter(e -> e.getName().contains(subCategoryQueryField.getText().trim())).toList();
            subCategoriesTableView.getItems().clear();
            subCategoriesTableView.getItems().addAll(filtered);
            subCategoriesTableView.refresh();
        }
    }

    @FXML
    protected void onSubCategoryKeyAction(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            onQuerySubCategoryAction();
        }
    }

    @FXML
    protected void onSaveServerParamsAction() {
        if(StringUtils.isEmpty(tfServerHost.getText().trim())
                || StringUtils.isEmpty(tfServerPort.getText().trim())
                || StringUtils.isEmpty(tfServerUserName.getText().trim())
                || StringUtils.isEmpty(tfServerPassword.getText().trim())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            alert.setHeaderText("信息不能为空");
            alert.show();
        }
        else {
            try {
                List<Settings> params = new ArrayList<>();
                params.add(Settings.builder().name("FTP_SERVER_HOST").value(tfServerHost.getText().trim()).build());
                params.add(Settings.builder().name("FTP_PORT").value(tfServerPort.getText().trim()).build());
                params.add(Settings.builder().name("FTP_LOGIN_USER").value(tfServerUserName.getText().trim()).build());
                params.add(Settings.builder().name("FTP_LOGIN_PASS").value(tfServerPassword.getText().trim()).build());
                ParamsDAO.updateBatch(params);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                alert.setHeaderText("保存成功");
                alert.show();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    protected void onConnectionTestAction() {
        if(StringUtils.isEmpty(tfServerHost.getText().trim())
                || StringUtils.isEmpty(tfServerPort.getText().trim())
                || StringUtils.isEmpty(tfServerUserName.getText().trim())
                || StringUtils.isEmpty(tfServerPassword.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            alert.setHeaderText("信息不能为空");
            alert.show();
        } else {
            FTPClient ftp = new FTPClient();
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            try {
                ftp.connect(tfServerHost.getText().trim(), Integer.parseInt(tfServerPort.getText().trim()));
                int reply = ftp.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftp.disconnect();
                    throw new IOException("Exception in connecting to FTP Server");
                }
                ftp.login(tfServerUserName.getText().trim(), tfServerPassword.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                alert.setHeaderText("连接成功");
                alert.show();
                ftp.disconnect();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
                alert.setHeaderText("连接失败");
                alert.show();
            }
        }
    }

    @FXML
    protected void onSaveFilePathAction() {
        if(StringUtils.isEmpty(tfFilePath.getText().trim())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            alert.setHeaderText("路径不能为空");
            alert.show();
        } else {
            List<Settings> params = new ArrayList<>();
            params.add(Settings.builder().name("LOCAL_FILE_PATH").value(tfFilePath.getText().trim()).build());
            try {
                ParamsDAO.updateBatch(params);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                alert.setHeaderText("保存成功");
                alert.show();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    protected void onCheckFilePathAction() {
        if(StringUtils.isEmpty(tfFilePath.getText().trim())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            alert.setHeaderText("路径不能为空");
            alert.show();
        } else {
            if(Files.exists(Path.of(tfFilePath.getText().trim()))) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                alert.setHeaderText("路径正确");
                alert.show();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
                alert.setHeaderText("路径不存在");
                alert.show();
            }
        }
    }
}
