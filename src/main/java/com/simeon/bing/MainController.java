package com.simeon.bing;

import com.simeon.bing.response.GetRoutersResponse;
import com.simeon.bing.response.LoginResponse;
import com.simeon.bing.utils.HttpUtil;
import com.simeon.bing.utils.JsonUtil;
import com.simeon.bing.utils.RefUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.HashMap;


@Getter
@Setter
public class MainController {
    private Stage stage;
    @FXML
    protected BorderPane mainContainer;
    @FXML
    protected TreeView<String> routerTree;
    @FXML
    protected SplitPane splitPane;

//    @FXML
//    protected Button btnParamSettings;
//    @FXML
//    protected Button btnDataInteraction;
//    @FXML
//    protected Button btnCaseMgt;
//    @FXML
//    protected Button btnDigital;
    @FXML
    protected Label systemTitleLabel;

    private Stage paramSettingsStage = new Stage(StageStyle.UNDECORATED);
    private MainApplication mainApp;
    public static HashMap<String, String> SETTINGS = new HashMap<>();
//    @FXML
//    private HBox userProfilePane;
//    @FXML
//    private FlowPane buttonPane;
//    @FXML
//    private HBox returnPane;
//    @FXML
//    private BorderPane contentBorderPane;
    private TabPane paramSettingsPane;
    private TabPane diPane;
    private BorderPane dpPane;
//    @FXML
//    private Label loginUserLabel;
//    @FXML
//    private Button btnReture;
    @FXML
    private Label taskStateLabel;
    private ContextMenu menu = new ContextMenu();
    private DIController diController;
    private DPController dpController;

//    关于
    private Stage aboutStage = new Stage();

    public void initialize(MainApplication mainApp) {
        RefUtils.labelState = taskStateLabel;

        FontIcon icon = new FontIcon("mdral-account_circle");
        icon.setIconSize(40);
        icon.setFill(Color.WHITE);
//        loginUserLabel.setText("");
//        loginUserLabel.setGraphic(icon);

        this.mainApp = mainApp;


        initMenuTree();
//        initSystemTitle();
//        initButtons();
//        initParamSettingsPane();
//        initDataInterchangePane();
//        initDigitalProcessingPane();
//        initReturnPane();
//        initUserProfileMenu();
//        initAboutStage();
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
        rootNode.setExpanded(true); // 展开根节点
        try {
            String response = HttpUtil.sendGetRequest(APIs.GET_ROUTERS, TokenStore.getToken());
            GetRoutersResponse res = JsonUtil.fromJson(response, GetRoutersResponse.class);
            loadRouters(rootNode, res.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        // 构建树形结构
//        folder1.getChildren().addAll(file1, file2);
//        folder2.getChildren().add(file3);
//        rootNode.getChildren().addAll(folder1, folder2);

        // 创建 TreeView
//        TreeView<String> treeView = new TreeView<>(rootNode);
        routerTree.setRoot(rootNode);
        // 为TreeView设置鼠标点击事件
        routerTree.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                TreeItem<String> selectedItem = routerTree.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // 处理双击事件
                    if("系统参数".equals(selectedItem.getValue())) {
                        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("system-parameters-view.fxml"));
                        try {
                            BorderPane pane = loader.load();
                            SystemParametersController controller = loader.getController();
                            mainContainer.setCenter(pane);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else if("数据交互".equals(selectedItem.getValue())) {
                        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("data-interaction-view.fxml"));
                        try {
                            BorderPane pane = loader.load();
                            DataInteractionController controller = loader.getController();
                            controller.setStage(stage);
                            mainContainer.setCenter(pane);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else if("病案管理".equals(selectedItem.getValue())) {
                        mainContainer.setCenter(new BorderPane());
                    } else if("数字化加工（高拍）".equals(selectedItem.getValue())) {
                        mainContainer.setCenter(new BorderPane());
                    }
                }
            }
        });

    }

    private void initUserProfileMenu() {
//        病案数字化 - Case Digitalization
//        系统参数 - System Parameters
//        病案管理 - Case Management
//        数字化加工（高拍）- Digital Processing (High-Speed Scanning)
//        数据交互 - Data Interaction


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
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("param-settings.fxml"));
        ParamSettingsController controller;
        try {
            paramSettingsPane = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initDataInterchangePane() {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("di-view.fxml"));
        try {
            diPane = loader.load();
            diController = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initDigitalProcessingPane() {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("dp-view.fxml"));
        try {
            dpPane = loader.load();
            dpController = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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