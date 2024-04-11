package com.simeon.bing;

import com.simeon.bing.dao.BaseDAO;
import com.simeon.bing.dao.RegDAO;
import com.simeon.bing.dao.SettingsDAO;
import com.simeon.bing.model.Base;
import com.simeon.bing.model.Reg;
import com.simeon.bing.model.Settings;
import com.simeon.bing.model.User;
import com.simeon.bing.utils.AuthUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class MainController {

    private Stage paramSettingsStage = new Stage(StageStyle.UNDECORATED);




    private MainApplication mainApp;
    public static HashMap<String, String> SETTINGS = new HashMap<>();



    @FXML
    private FlowPane buttonPane;
    @FXML
    private HBox returnPane;
    @FXML
    private BorderPane contentBorderPane;
    @FXML
    private TabPane paramSettingsPane;


    @FXML
    private Label usrLabel;
    @FXML
    private Hyperlink currentUser;

    @FXML
    private void initialize() {
    }


    public void initialize(MainApplication mainApp, User loginUser) {
        currentUser.setText(loginUser.getUserName());
        AuthUtils.loginUser = loginUser;
        this.mainApp = mainApp;
        initParamSettingsPane();
        initDataInterchangePane();
        initReturnPane();
    }

    private void initParamSettingsPane() {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("param-settings.fxml"));
//        paramSettingsPane = loader.getRoot();
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
}