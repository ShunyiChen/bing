package com.simeon.bing;

import com.simeon.bing.model.PatientRecord;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Setter
@Getter
public class DataInteractionImportController {
    private static final String FORMAT = "yyyy-MM-dd";
    private Stage stage;
    @FXML
    protected RadioButton btnWM;
    @FXML
    protected RadioButton btnTCM;
    @FXML
    protected TextField pathTextField;
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
    }

    @FXML
    protected void handleBrowsing() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开CSV文件");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        FileChooser.ExtensionFilter pngExtensionFilter = new FileChooser.ExtensionFilter(".csv)", "*.csv");
        fileChooser.getExtensionFilters().add(pngExtensionFilter);
        fileChooser.setSelectedExtensionFilter(pngExtensionFilter);
        File file = fileChooser.showOpenDialog(stage);
        if(file != null) {
            pathTextField.setText(file.getPath());
            loadCsvData();
        }
    }

    private void loadCsvData() {
        String filePath = pathTextField.getText();
        if(!new File(filePath).exists()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(stage);
            alert.setTitle("提示");
            alert.setHeaderText("文件不存在");
            alert.setContentText("");
            alert.show();
        } else {
            String line;
            String cvsSplitBy = ",";
            // 日期解析器
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT);
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                // 跳过表头
                br.readLine();
                while ((line = br.readLine()) != null) {
                    // 使用逗号分割
                    String[] values = line.split(cvsSplitBy);

                    // 创建PatientRecord对象
                    PatientRecord record = new PatientRecord();
                    record.setId(Long.parseLong(values[0].trim()));
                    record.setInstitutionCode(values[1].trim());
                    record.setInstitutionName(values[2].trim());
                    record.setMedicalRecordNumber(values[3].trim());
                    record.setHospitalizationCount(Integer.parseInt(values[4].trim()));
                    record.setAdmissionDate(dateFormat.parse(values[5].trim()));
                    record.setDischargeDate(dateFormat.parse(values[6].trim()));
                    record.setPatientName(values[7].trim());
                    record.setGender(values[8].trim());
                    record.setBirthDate(dateFormat.parse(values[9].trim()));
                    record.setAge(Integer.parseInt(values[10].trim()));
                    record.setDischargeMethod(Integer.parseInt(values[11].trim()));

                    // 添加到记录列表
                    tableView.getItems().add(record);
                }
                tableView.refresh();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void handleImport() {
        if(tableView.getItems().isEmpty()) {
            Alert alert = new Alert (Alert.AlertType.INFORMATION);
            alert.initOwner(stage);
            alert.setTitle("提示");
            alert.setHeaderText("列表不能为空");
            alert.setContentText("");
            alert.show();
        } else {


            Alert alert = new Alert (Alert.AlertType.INFORMATION);
            alert.initOwner(stage);
            alert.setTitle("提示");
            alert.setHeaderText("导入成功");
            alert.setContentText("");
            alert.show();
        }
    }
}
