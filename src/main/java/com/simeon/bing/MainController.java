package com.simeon.bing;

import com.simeon.bing.model.User;
import com.simeon.bing.utils.AuthUtils;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.HashMap;


public class MainController {

    @FXML
    protected Button btnParamSettings;
    @FXML
    protected Button btnDataInteraction;
    @FXML
    protected Button btnCaseMgt;
    @FXML
    protected Button btnDigital;
    @FXML
    protected Label systemTitleLabel;

    private Stage paramSettingsStage = new Stage(StageStyle.UNDECORATED);
    private MainApplication mainApp;
    public static HashMap<String, String> SETTINGS = new HashMap<>();
    @FXML
    private HBox userProfilePane;
    @FXML
    private FlowPane buttonPane;
    @FXML
    private HBox returnPane;
    @FXML
    private BorderPane contentBorderPane;
    @FXML
    private TabPane paramSettingsPane;
    @FXML
    private Label loginUserLabel;
    @FXML
    private Button btnReture;
    private Color color = Color.rgb(3,158,211);
    private ContextMenu menu = new ContextMenu();


    public void initialize(MainApplication mainApp, User loginUser) {
        FontIcon icon = new FontIcon("mdral-account_circle");
        icon.setIconSize(40);
        icon.setFill(Color.WHITE);
        loginUserLabel.setText(loginUser.getUserName());
        loginUserLabel.setGraphic(icon);

        AuthUtils.loginUser = loginUser;
        this.mainApp = mainApp;

        initSystemTitle();
        initButtons();
        initParamSettingsPane();
        initDataInterchangePane();
        initReturnPane();
        initUserProfileMenu();
    }

    private void initUserProfileMenu() {
        MenuItem item = new MenuItem("退出系统");
        item.setStyle("-fx-font-size: 13px;-fx-font-family:SimSun;");
        menu.getItems().add(item);
        item.setOnAction(e -> {
            Platform.exit();
        });
    }

    private void initSystemTitle() {
        FontIcon titleIcon = new FontIcon("mdal-cloud_upload");
        titleIcon.setIconSize(30);
        titleIcon.setFill(Color.WHITE);
        systemTitleLabel.setGraphic(titleIcon);
    }

    private void initButtons() {
        FontIcon returnIcon = new FontIcon("mdal-keyboard_return");
        returnIcon.setIconSize(20);
        btnReture.setGraphic(returnIcon);

        FontIcon paramSettingsIcon = new FontIcon("mdmz-settings");
        paramSettingsIcon.setIconSize(80);
        paramSettingsIcon.setFill(color);
        btnParamSettings.setGraphic(paramSettingsIcon);

        FontIcon dataInteractionIcon = new FontIcon("mdsmz-psychology");
        dataInteractionIcon.setIconSize(80);
        dataInteractionIcon.setFill(color);
        btnDataInteraction.setGraphic(dataInteractionIcon);

        FontIcon caseMgtIcon = new FontIcon("mdal-airline_seat_recline_normal");
        caseMgtIcon.setIconSize(80);
        caseMgtIcon.setFill(color);
        btnCaseMgt.setGraphic(caseMgtIcon);

        FontIcon digitalIcon = new FontIcon("mdomz-picture_as_pdf");
        digitalIcon.setIconSize(80);
        digitalIcon.setFill(color);
        btnDigital.setGraphic(digitalIcon);
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

    }

    private void initReturnPane() {
        contentBorderPane.getChildren().remove(returnPane);
    }


    @FXML
    protected void onParamSettingsAction() {
        contentBorderPane.getChildren().clear();
        contentBorderPane.setCenter(paramSettingsPane);
        contentBorderPane.setTop(returnPane);
        returnPane.toFront();
        btnReture.toFront();
    }

    @FXML
    protected void onDataInterchangeAction() {
    }

    @FXML
    protected void onCaseManagementAction() {

    }

    @FXML
    protected void onDigitalProcessingAction() {

    }

    @FXML
    protected void onProfileClick() {
    }

    @FXML
    protected void onReturnAction() {
        contentBorderPane.setCenter(buttonPane);
        contentBorderPane.getChildren().remove(returnPane);
    }

    @FXML
    protected void onOpenProfileMenu(MouseEvent e) {
        menu.show(loginUserLabel, e.getScreenX(), e.getScreenY()+10);
    }
}