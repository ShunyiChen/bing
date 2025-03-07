package com.simeon.bing;

import com.simeon.bing.model.PatientRecord;
import com.simeon.bing.request.GetRecordsReq;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Getter
@Setter
public class DataInteractionController {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private Stage stage;
    private GetRecordsReq queryParam = GetRecordsReq.builder().pageNum(1).pageSize(30).build();
    private TablePageController tablePageController;
    private BorderPane formPane;
    @FXML
    protected Button btnCollapse;
    @FXML
    protected BorderPane searchPane;
    @FXML
    protected TableView<PatientRecord> tableView;
    @FXML
    protected TableColumn<PatientRecord, Integer> rowNumberCol;
    @FXML
    protected TableColumn<PatientRecord, String> institutionCodeCol;
    @FXML
    protected TableColumn<PatientRecord, String> institutionNameCol;
    @FXML
    protected TableColumn<PatientRecord, String> medicalRecordNumberCol;
    @FXML
    protected TableColumn<PatientRecord, Integer> hospitalizationCountCol;
    @FXML
    protected TableColumn<PatientRecord, String> admissionDateCol;
    @FXML
    protected TableColumn<PatientRecord, String> dischargeDateCol;
    @FXML
    protected TableColumn<PatientRecord, String> patientNameCol;
    @FXML
    protected TableColumn<PatientRecord, String> genderCol;
    @FXML
    protected TableColumn<PatientRecord, String> birthDateCol;
    @FXML
    protected TableColumn<PatientRecord, Integer> ageCol;
    @FXML
    protected TableColumn<PatientRecord, Integer> dischargeMethodCol;
    @FXML
    protected TableColumn<PatientRecord, String> typeCol;
    @FXML
    protected TableColumn<PatientRecord, String> statusCol;
    @FXML
    protected TableColumn<PatientRecord, String> createByCol;
    @FXML
    protected TableColumn<PatientRecord, String> createTimeCol;
    @FXML
    protected TableColumn<PatientRecord, String> updateByCol;
    @FXML
    protected TableColumn<PatientRecord, String> updateTimeCol;
    @FXML
    protected BorderPane pageFooter;

    @FXML
    private void initialize() {
        // 设置查询组件
        FXMLLoader searchLoader = new FXMLLoader(MainApplication.class.getResource("data-interaction-search-view.fxml"));
        // 设置查询组件
        FXMLLoader pageLoader = new FXMLLoader(MainApplication.class.getResource("table-page-view.fxml"));
        try {
            formPane = searchLoader.load();
            DataInteractionSearchController controller = searchLoader.getController();

            BorderPane pagePane = pageLoader.load();
            tablePageController = pageLoader.getController();

            controller.setQueryParam(queryParam);
            controller.setSearchCallBack(callbackParam -> {
                queryParam = callbackParam.getQueryParam();
                tablePageController.updatePagination(callbackParam);
                tableView.getItems().clear();
                tableView.getItems().addAll(callbackParam.getResults());
                tableView.refresh();
                return null;
            });
            // 设置 pane 占满整个区域
            formPane.setMaxWidth(Double.MAX_VALUE);
            formPane.setMaxHeight(Double.MAX_VALUE);
            searchPane.setCenter(formPane);

            tablePageController.setQueryParam(queryParam);
            tablePageController.setSearchCallBack(callbackParam -> {
                queryParam = callbackParam.getQueryParam();
                tableView.getItems().clear();
                tableView.getItems().addAll(callbackParam.getResults());
                tableView.refresh();
                return null;
            });
            pageFooter.setCenter(pagePane);
            //首次查询
            tablePageController.goToTheHomepage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FontIcon icon = new FontIcon("mdal-expand_less");
        icon.setIconSize(14);
        btnCollapse.setGraphic(icon);
        // 设置按钮的样式，使其变为圆形
        btnCollapse.setShape(new Circle(10)); // 10 是圆的半径
        btnCollapse.setMinSize(20, 20); // 设置按钮的最小大小
        btnCollapse.setMaxSize(20, 20); // 设置按钮的最大大小
        btnCollapse.setStyle("-fx-background-radius: 10;"); // 设置背景圆角半径为 10


        // 初始化表格列
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        DateFormat dateTimeFormat = new SimpleDateFormat(DATETIME_FORMAT);
        // 设置每一行序号
        rowNumberCol.setCellValueFactory(param -> new SimpleIntegerProperty( tableView.getItems().indexOf(param.getValue()) + 1).asObject());
        // Setting up the cell value factories for the table columns
        institutionCodeCol.setCellValueFactory(param -> new SimpleStringProperty(
            param.getValue().getInstitutionCode() != null
            ? param.getValue().getInstitutionCode()
            : ""
        ));
        institutionNameCol.setCellValueFactory(param -> new SimpleStringProperty(
            param.getValue().getInstitutionName() != null
            ? param.getValue().getInstitutionName()
            : ""
        ));
        medicalRecordNumberCol.setCellValueFactory(param -> new SimpleStringProperty(
            param.getValue().getMedicalRecordNumber() != null
            ? param.getValue().getMedicalRecordNumber()
            : ""
        ));
        medicalRecordNumberCol.setCellValueFactory(param -> {
            PatientRecord record = param.getValue();
            String medicalRecordNumber = (record.getMedicalRecordNumber() != null) ? record.getMedicalRecordNumber() : "";
            return new SimpleObjectProperty<>(medicalRecordNumber);
        });
        hospitalizationCountCol.setCellValueFactory(param -> {
            Integer hospitalizationCount = param.getValue().getHospitalizationCount();
            // 如果 hospitalizationCount 为 null，使用 0 作为默认值
            return new SimpleIntegerProperty(hospitalizationCount != null ? hospitalizationCount : 0).asObject();
        });
        admissionDateCol.setCellValueFactory(param -> new SimpleStringProperty(
            param.getValue().getAdmissionDate() != null
            ? format.format(param.getValue().getAdmissionDate())
            : ""
        ));
        dischargeDateCol.setCellValueFactory(param -> new SimpleStringProperty(
            param.getValue().getDischargeDate() != null
            ? format.format(param.getValue().getDischargeDate())
            : ""
        ));
        patientNameCol.setCellValueFactory(param -> {
            String patientName = (param.getValue().getPatientName() != null) ? param.getValue().getPatientName() : "";
            return new SimpleObjectProperty<>(patientName);
        });
        genderCol.setCellValueFactory(param -> {
            String gender = (param.getValue().getGender() != null) ? param.getValue().getGender() : "";
            return new SimpleObjectProperty<>(gender);
        });
        birthDateCol.setCellValueFactory(param -> new SimpleStringProperty(
            param.getValue().getBirthDate() != null
            ? format.format(param.getValue().getBirthDate())
            : ""
        ));
        ageCol.setCellValueFactory(param -> {
            Integer age = param.getValue().getAge();
            // 如果 hospitalizationCount 为 null，使用 0 作为默认值
            return new SimpleIntegerProperty(age != null ? age : 0).asObject();
        });
        dischargeMethodCol.setCellValueFactory(param -> {
            Integer dischargeMethod = param.getValue().getDischargeMethod();
            // 如果 hospitalizationCount 为 null，使用 0 作为默认值
            return new SimpleIntegerProperty(dischargeMethod != null ? dischargeMethod : 0).asObject();
        });
        typeCol.setCellValueFactory(param -> {
            String type = (param.getValue().getType() != null) ? param.getValue().getType() == 0?"西医":"中医" : "";
            return new SimpleObjectProperty<>(type);
        });
        statusCol.setCellValueFactory(param -> {
            String status = (param.getValue().getStatus() != null) ? param.getValue().getStatus() : "";
            return new SimpleObjectProperty<>(status);
        });
        createByCol.setCellValueFactory(param -> {
            String createBy = (param.getValue().getCreateBy() != null) ? param.getValue().getCreateBy() : "";
            return new SimpleObjectProperty<>(createBy);
        });
        createTimeCol.setPrefWidth(160);
        createTimeCol.setCellValueFactory(param -> new SimpleStringProperty(
            param.getValue().getCreateTime() != null
            ? dateTimeFormat.format(param.getValue().getCreateTime())
            : ""
        ));
        updateByCol.setCellValueFactory(param -> {
            String updateBy = (param.getValue().getUpdateBy() != null) ? param.getValue().getUpdateBy() : "";
            return new SimpleObjectProperty<>(updateBy);
        });
        updateTimeCol.setPrefWidth(160);
        updateTimeCol.setCellValueFactory(param -> new SimpleStringProperty(
            param.getValue().getUpdateTime() != null
            ? dateTimeFormat.format(param.getValue().getUpdateTime())
            : ""
        ));
    }

    @FXML
    protected void handleImport() {
        Stage importStage = new Stage();
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("data-interaction-import-view.fxml"));
        try {
            BorderPane pane = loader.load();
            DataInteractionImportController controller = loader.getController();
            controller.setStage(importStage);
            controller.setRefreshDataCallback(unused -> {
                //导入后刷新表格
                tablePageController.goToTheHomepage();
                return null;
            });
            Scene scene = new Scene(pane);
            importStage.setResizable(false);
            importStage.setScene(scene);
            importStage.initOwner(stage);
            importStage.initModality(Modality.APPLICATION_MODAL);
            importStage.setTitle("导入病案信息");
            importStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handleExport() {
        if(tableView.getItems().isEmpty()) {
            Alert alert = new Alert (Alert.AlertType.INFORMATION);
            alert.initOwner(stage);
            alert.setTitle("提示");
            alert.setHeaderText("表格数据不能为空");
            alert.setContentText("");
            alert.show();
        } else {
            FileChooser fileChooser = new FileChooser();
            // 设置文件过滤器，例如只允许保存文本文件
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            // 设置初始目录，例如用户的文档目录
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Documents"));
            // 设置默认的文件名
            fileChooser.setInitialFileName("newfile.csv");
            // 显示保存对话框
            File file = fileChooser.showSaveDialog(stage);
            if(file == null) {
                Alert alert = new Alert (Alert.AlertType.INFORMATION);
                alert.initOwner(stage);
                alert.setTitle("提示");
                alert.setHeaderText("未选择任何文件");
                alert.setContentText("");
                alert.show();
            } else {
                System.out.println("保存到 "+file.getPath());
            }
        }
    }

    @FXML
    protected void handleCollapse() {
        if(searchPane.getChildren().contains(formPane)) {
            searchPane.setCenter(null);
            FontIcon icon = new FontIcon("mdal-expand_more");
            icon.setIconSize(14);
            btnCollapse.setGraphic(icon);
        } else {
            searchPane.setCenter(formPane);
            FontIcon icon = new FontIcon("mdal-expand_less");
            icon.setIconSize(14);
            btnCollapse.setGraphic(icon);
        }
    }
}
