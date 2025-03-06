package com.simeon.bing;

import com.simeon.bing.model.PatientRecord;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Setter
@Getter
public class UploadRecordsController {
    private Stage stage;
    @FXML
    protected BorderPane topPane;
    @FXML
    protected BorderPane tableFooter;
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
    protected TableColumn<PatientRecord, String> statusCol;

    @FXML
    private void initialize() {
        // 设置查询组件
        FXMLLoader searchLoader = new FXMLLoader(MainApplication.class.getResource("data-interaction-search-view.fxml"));

        // 设置查询组件
        FXMLLoader pageLoader = new FXMLLoader(MainApplication.class.getResource("table-page-view.fxml"));
        try {
            BorderPane pane = searchLoader.load();
            DataInteractionSearchController controller = searchLoader.getController();

            // 设置 pane 占满整个区域
            pane.setMaxWidth(Double.MAX_VALUE);
            pane.setMaxHeight(Double.MAX_VALUE);
            topPane.setCenter(pane);


            BorderPane pagePane = pageLoader.load();
            TablePageController tablePageController = pageLoader.getController();
            tableFooter.setCenter(pagePane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
