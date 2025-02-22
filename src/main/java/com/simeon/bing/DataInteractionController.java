package com.simeon.bing;

import com.simeon.bing.model.PatientRecord;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Getter
@Setter
public class DataInteractionController {
    private static final String FORMAT = "yyyy-MM-dd";
    private Stage stage;
    @FXML
    protected BorderPane searchPane;
    @FXML
    protected TableView<PatientRecord> tableView;
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
    private void initialize() {
        // 设置查询组件
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("data-interaction-search-view.fxml"));
        try {
            BorderPane pane = loader.load();
            // 设置 pane 占满整个区域
            pane.setMaxWidth(Double.MAX_VALUE);
            pane.setMaxHeight(Double.MAX_VALUE);
            searchPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 初始化表格列
        DateFormat format = new SimpleDateFormat(FORMAT);
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

        PatientRecord d = new PatientRecord();
        d.setInstitutionCode("AB");
        d.setDischargeMethod(1);
        tableView.getItems().add(d);

    }

    @FXML
    protected void handleImport() {
        Stage importStage = new Stage();
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("data-interaction-import-view.fxml"));
        try {
            BorderPane pane = loader.load();
            DataInteractionImportController controller = loader.getController();
            controller.setStage(importStage);
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
}
