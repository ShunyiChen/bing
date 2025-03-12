package com.simeon.bing;

import com.simeon.bing.response.GetRoutersResponse;
import com.simeon.bing.utils.HttpUtil;
import com.simeon.bing.utils.JsonUtil;
import com.simeon.bing.utils.UIUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Objects;

@Getter
@Setter
public class MainController {
    private Stage stage;
    protected DigitalProcessController digitalProcessController;
    private Stage paramSettingsStage = new Stage(StageStyle.UNDECORATED);
    private MainApplication mainApp;
    private ContextMenu menu = new ContextMenu();
    private Stage aboutStage = new Stage();
    @FXML
    protected Label labelLogo;
    @FXML
    protected Button btnExpand;
    @FXML
    protected Button btnCollapse;
    @FXML
    protected BorderPane topLayerPane;
    @FXML
    protected BorderPane borderBar;
    @FXML
    protected BorderPane sideBar;
    @FXML
    protected BorderPane mainContainer;
    @FXML
    protected TreeView<String> routerTree;
    @FXML
    protected ToggleButton btnProject;
    @FXML
    protected Label systemTitleLabel;
    @FXML
    private Label taskStateLabel;
    @FXML
    protected BorderPane headerPane;

    @FXML
    protected void handleShowOrHideProject() {
        sideBar.setVisible(!sideBar.isVisible());
        topLayerPane.setMouseTransparent(!sideBar.isVisible());
    }

    public void initialize(MainApplication mainApp) {
        // 设置上层 BorderPane 为不可点击
        topLayerPane.setMouseTransparent(false);

        FontIcon icon = new FontIcon("mdoal-folder");
        icon.setIconSize(20);
        icon.setFill(Color.rgb(0,150,201));
        btnProject.setGraphic(icon);
        btnProject.setPadding(new Insets(0)); // 设置内边距为0

        this.mainApp = mainApp;
        initMenuTree();

        FontIcon expandIcon = new FontIcon("mdmz-open_in_full");
        expandIcon.setIconSize(15);
        expandIcon.setFill(Color.rgb(0,150,201));
        btnExpand.setGraphic(expandIcon);
        btnExpand.setPadding(new Insets(0)); // 设置内边距为0

        FontIcon collapseIcon = new FontIcon("mdal-close_fullscreen");
        collapseIcon.setIconSize(15);
        collapseIcon.setFill(Color.rgb(0,150,201));
        btnCollapse.setGraphic(collapseIcon);
        btnCollapse.setPadding(new Insets(0)); // 设置内边距为0

        FontIcon upload = new FontIcon("mdsal-cloud_upload");
        upload.setIconSize(25);
        upload.setFill(Color.WHITE);
        labelLogo.setGraphic(upload);

        // 监听全局鼠标点击事件
        mainContainer.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, this::handleGlobalMouseClick);


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
        });


//        initSystemTitle();
//        initButtons();
//        initParamSettingsPane();
//        initDataInterchangePane();
//        initDigitalProcessingPane();
//        initReturnPane();
//        initUserProfileMenu();
//        initAboutStage();
    }

    @FXML
    protected void handleExpand() {
        expandAll(routerTree.getRoot());
    }

    @FXML
    protected void handleCollapse() {
        collapseAll(routerTree.getRoot());
    }

    /**
     * 递归展开所有 TreeItem 节点
     *
     * @param item 当前节点
     */
    private void expandAll(TreeItem<?> item) {
        if (item != null && !item.isLeaf()) {
            item.setExpanded(true); // 展开当前节点
            for (TreeItem<?> child : item.getChildren()) {
                expandAll(child); // 递归展开子节点
            }
        }
    }

    /**
     * 递归折叠所有 TreeItem 节点
     *
     * @param item 当前节点
     */
    private void collapseAll(TreeItem<?> item) {
        if (item != null && !item.isLeaf()) {
            item.setExpanded(false); // 折叠当前节点
            for (TreeItem<?> child : item.getChildren()) {
                collapseAll(child); // 递归折叠子节点
            }
        }
    }

    /**
     * 处理全局鼠标点击事件
     */
    private void handleGlobalMouseClick(MouseEvent event) {
        // 判断点击事件是否发生在 sideBar 或其子节点中
        if (!isClickInsideNode(event, sideBar, borderBar)) {
            // 如果点击事件发生在 sideBar 外，则隐藏 sideBar
            sideBar.setVisible(false);
            topLayerPane.setMouseTransparent(true); // 允许点击底层面板
        }
    }

    /**
     * 判断鼠标点击事件是否发生在指定节点或其子节点中
     *
     * @param event 鼠标点击事件
     * @param node  要检查的节点
     * @return 如果点击事件发生在节点或其子节点中，返回 true；否则返回 false
     */
    private boolean isClickInsideNode(MouseEvent event, javafx.scene.Node node, javafx.scene.Node node2) {
        if(event.getTarget() instanceof javafx.scene.Scene) {
            return true;
        }
        // 获取点击事件的目标节点
        javafx.scene.Node target = (javafx.scene.Node) event.getTarget();

        // 递归检查目标节点是否为指定节点或其子节点
        while (target != null) {
            if (target == node || target == node2) {
                return true;
            }
            target = target.getParent();
        }

        return false;
    }

    private void loadRouters(TreeItem<String> parentItem, GetRoutersResponse.D[] data) {
        if(data != null) {
            for(GetRoutersResponse.D d : data) {
                if(!d.getHidden()) {
                    // 创建子节点
                    TreeItem<String> item = new TreeItem<>(d.getMeta().getTitle());
                    parentItem.getChildren().add(item);
                    loadRouters(item, d.getChildren());
                }
            }
        }
    }

    private void initMenuTree() {
        // 创建根节点
        TreeItem<String> rootNode = new TreeItem<>("功能列表");
        try {
            String response = HttpUtil.sendGetRequest(APIs.GET_ROUTERS, TokenStore.getToken());
            GetRoutersResponse res = JsonUtil.fromJson(response, GetRoutersResponse.class);
            loadRouters(rootNode, res.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        routerTree.setRoot(rootNode);
        // 监听节点选择事件
        routerTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue != null && oldValue.getValue().equals("数字化加工(高拍)")) {
                if(digitalProcessController != null) {
                    digitalProcessController.handleCloseCamera();
                }
            }
        });
        // 为TreeView设置鼠标点击事件
        routerTree.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                TreeItem<String> selectedItem = routerTree.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // 处理双击事件
                    if("病案管理".equals(selectedItem.getValue())) {
                        Callback<String, String> callback = s -> {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            Platform.runLater(() -> {
                                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("data-interaction-view.fxml"));
                                try {
                                    BorderPane pane = loader.load();
                                    DataInteractionController controller = loader.getController();
                                    controller.setStage(stage);
                                    mainContainer.setCenter(pane);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            return null;
                        };
                        UIUtils uiUtils = new UIUtils(stage, callback);
                        uiUtils.show();
                    } else if("数字化加工(高拍)".equals(selectedItem.getValue())) {
                        Callback<String, String> callback = s -> {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            Platform.runLater(() -> {
                                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("digital-processing-view.fxml"));
                                try {
                                    BorderPane pane = loader.load();
                                    digitalProcessController = loader.getController();
                                    digitalProcessController.setStage(stage);
                                    mainContainer.setCenter(pane);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            return null;
                        };
                        UIUtils uiUtils = new UIUtils(stage, callback);
                        uiUtils.show();
                    } else if("上传病案".equals(selectedItem.getValue())) {
                        Callback<String, String> callback = s -> {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            Platform.runLater(() -> {
                                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("upload-records-view.fxml"));
                                try {
                                    BorderPane pane = loader.load();
                                    UploadRecordsController controller = loader.getController();
                                    controller.setStage(stage);
                                    mainContainer.setCenter(pane);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            return null;
                        };
                        UIUtils uiUtils = new UIUtils(stage, callback);
                        uiUtils.show();
                    } else if("参数设置".equals(selectedItem.getValue())) {
                        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("param-settings-view.fxml"));
                        try {
                            BorderPane pane = loader.load();
                            ParamSettingsController controller = loader.getController();
                            mainContainer.setCenter(pane);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
        expandAll(routerTree.getRoot());
    }

    private void initUserProfileMenu() {
        MenuItem itemAboutSys = new MenuItem("关于系统");
        MenuItem itemExit = new MenuItem("退出系统");
        itemAboutSys.setStyle("-fx-font-size: 13px;-fx-font-family:SimSun;");
        itemExit.setStyle("-fx-font-size: 13px;-fx-font-family:SimSun;");
        menu.getItems().addAll(itemAboutSys, new SeparatorMenuItem(),itemExit);
        itemAboutSys.setOnAction(e -> {
            aboutStage.show();
        });
        itemExit.setOnAction(e -> System.exit(0));
    }

    private void initSystemTitle() {
        FontIcon titleIcon = new FontIcon("mdal-cloud_upload");
        titleIcon.setIconSize(30);
        titleIcon.setFill(Color.WHITE);
        systemTitleLabel.setGraphic(titleIcon);
    }

    private void initButtons() {
//        FontIcon returnIcon = new FontIcon("mdal-keyboard_return");
//        returnIcon.setIconSize(20);
//        btnReture.setGraphic(returnIcon);
//
//        FontIcon paramSettingsIcon = new FontIcon("mdmz-settings");
//        paramSettingsIcon.setIconSize(80);
//        paramSettingsIcon.setFill(Constants.primaryColor);
//        btnParamSettings.setGraphic(paramSettingsIcon);
//
//        FontIcon dataInteractionIcon = new FontIcon("mdsmz-psychology");
//        dataInteractionIcon.setIconSize(80);
//        dataInteractionIcon.setFill(Constants.primaryColor);
//        btnDataInteraction.setGraphic(dataInteractionIcon);
//
//        FontIcon caseMgtIcon = new FontIcon("mdal-airline_seat_recline_normal");
//        caseMgtIcon.setIconSize(80);
//        caseMgtIcon.setFill(Constants.primaryColor);
//        btnCaseMgt.setGraphic(caseMgtIcon);
//
//        FontIcon digitalIcon = new FontIcon("mdomz-picture_as_pdf");
//        digitalIcon.setIconSize(80);
//        digitalIcon.setFill(Constants.primaryColor);
//        btnDigital.setGraphic(digitalIcon);
    }

    private void initParamSettingsPane() {
//        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("param-settings.fxml"));
//        ParamSettingsController controller;
//        try {
//            paramSettingsPane = loader.load();
//            controller = loader.getController();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void initDataInterchangePane() {
//        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("di-view.fxml"));
//        try {
//            diPane = loader.load();
//            diController = loader.getController();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void initDigitalProcessingPane() {
//        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("dp-view.fxml"));
//        try {
//            dpPane = loader.load();
//            dpController = loader.getController();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void initAboutStage() {
//        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("about-view.fxml"));
//        try {
//            VBox root = loader.load();
//            AboutController controller = loader.getController();
//            controller.setStage(aboutStage);
//            Scene scene = new Scene(root);
//            aboutStage.setResizable(false);
//            aboutStage.setScene(scene);
//            aboutStage.initModality(Modality.APPLICATION_MODAL);
//            aboutStage.setTitle("关于系统");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void initReturnPane() {
//        contentBorderPane.getChildren().remove(returnPane);
    }


    @FXML
    protected void onParamSettingsAction() {
//        contentBorderPane.getChildren().clear();
//        contentBorderPane.setCenter(paramSettingsPane);
//        contentBorderPane.setTop(returnPane);
    }

    @FXML
    protected void onDataInterchangeAction() {
//        contentBorderPane.getChildren().clear();
//        contentBorderPane.setCenter(diPane);
//        contentBorderPane.setTop(returnPane);
//        diController.resetImportTable();
    }

    @FXML
    protected void onCaseManagementAction() {

    }

    @FXML
    protected void onDigitalProcessingAction() {
//        contentBorderPane.getChildren().clear();
//        contentBorderPane.setCenter(dpPane);
//        contentBorderPane.setTop(returnPane);
//        dpController.resetImportTable();
    }

    @FXML
    protected void onProfileClick() {
    }

    @FXML
    protected void onReturnAction() {
//        contentBorderPane.setCenter(buttonPane);
//        contentBorderPane.getChildren().remove(returnPane);
    }

    @FXML
    protected void onOpenProfileMenu(MouseEvent e) {
//        menu.show(loginUserLabel, e.getScreenX(), e.getScreenY()+10);
    }
}