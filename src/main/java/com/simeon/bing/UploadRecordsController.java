package com.simeon.bing;

import com.simeon.bing.model.PatientRecord;
import com.simeon.bing.request.GetRecordsReq;
import com.simeon.bing.utils.JsonUtil;
import com.simeon.bing.utils.enums.PatientRecordState;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
public class UploadRecordsController {
    private Stage stage;
    private GetRecordsReq queryParam = GetRecordsReq.builder().pageNum(1).pageSize(30).status(PatientRecordState.MODIFIED.getState()).build();
    private TablePageController tablePageController;
    // 全局列表，用于存储所有选中的行的病案号
    private final HashSet<String> selectedItems = new HashSet<>();
    private BorderPane formPane;

    @FXML
    protected Button btnCollapse;
    @FXML
    protected BorderPane topPane;
    @FXML
    protected BorderPane tableFooter;
    @FXML
    protected TableView<PatientRecord> tableView;
    @FXML
    protected TableColumn<PatientRecord, Boolean> selectCol;
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
    private void initialize() {
        // 设置查询组件
        FXMLLoader searchLoader = new FXMLLoader(MainApplication.class.getResource("data-interaction-search-view.fxml"));

        // 设置查询组件
        FXMLLoader pageLoader = new FXMLLoader(MainApplication.class.getResource("table-page-view.fxml"));
        try {
            formPane = searchLoader.load();
            DataInteractionSearchController controller = searchLoader.getController();
            controller.getStatusComboBox().getSelectionModel().select(PatientRecordState.MODIFIED.getState());

            controller.setQueryParam(queryParam);
            controller.setSearchCallBack(callbackParam -> {
                queryParam = callbackParam.getQueryParam();
                tablePageController.updatePagination(callbackParam);
                tableView.getItems().clear();
                tableView.getItems().addAll(callbackParam.getResults());
                tableView.refresh();
                selectedItems.clear();
                return null;
            });

            // 设置 pane 占满整个区域
            formPane.setMaxWidth(Double.MAX_VALUE);
            formPane.setMaxHeight(Double.MAX_VALUE);
            topPane.setCenter(formPane);

            FontIcon icon = new FontIcon("mdal-expand_less");
            icon.setIconSize(14);
            btnCollapse.setGraphic(icon);
            // 设置按钮的样式，使其变为圆形
            btnCollapse.setShape(new Circle(10)); // 10 是圆的半径
            btnCollapse.setMinSize(20, 20); // 设置按钮的最小大小
            btnCollapse.setMaxSize(20, 20); // 设置按钮的最大大小
            btnCollapse.setStyle("-fx-background-radius: 10;"); // 设置背景圆角半径为 10

            initTable();

            BorderPane pagePane = pageLoader.load();
            tablePageController = pageLoader.getController();
            tablePageController.setQueryParam(queryParam);
            tablePageController.setSearchCallBack(callbackParam -> {
                queryParam = callbackParam.getQueryParam();
                tableView.getItems().clear();
                // 记住选中id
                callbackParam.getResults().forEach(e -> {
                    if(selectedItems.contains(e.getMedicalRecordNumber())) {
                        e.setSelected(true);
                    }
                });
                tableView.getItems().addAll(callbackParam.getResults());
                tableView.refresh();
                return null;
            });
            tableFooter.setCenter(pagePane);

            //首次查询
            tablePageController.goToTheHomepage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initTable() {
        String DATE_FORMAT = "yyyy-MM-dd";
        String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        // 初始化表格列
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        DateFormat dateTimeFormat = new SimpleDateFormat(DATETIME_FORMAT);

        // 设置 Select 列的单元格工厂
        selectCol.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));

        // 在列头添加全选 CheckBox
        CheckBox selectAllCheckBox = new CheckBox();
        selectAllCheckBox.setOnAction(event -> {
            boolean selected = selectAllCheckBox.isSelected();
            tableView.getItems().forEach(r -> {
                r.setSelected(selected);
                if(selected) {
                    selectedItems.add(r.getMedicalRecordNumber());
                } else {
                    selectedItems.remove(r.getMedicalRecordNumber());
                }
            });
        });

        // 监听每行 CheckBox 的状态变化
        selectCol.setCellFactory(column -> {
            TableCell<PatientRecord, Boolean> cell = new TableCell<>() {
                private final CheckBox checkBox = new CheckBox();
                {
                    checkBox.setOnAction(event -> {
                        PatientRecord r = getTableView().getItems().get(getIndex());
                        if (checkBox.isSelected()) {
                            selectedItems.add(r.getMedicalRecordNumber());
                        } else {
                            selectedItems.remove(r.getMedicalRecordNumber());
                        }
                    });
                }

                @Override
                protected void updateItem(Boolean selected, boolean empty) {
                    super.updateItem(selected, empty);
                    if (empty || selected == null) {
                        setGraphic(null);
                    } else {
                        checkBox.setSelected(selected);
                        setGraphic(checkBox);
                    }
                }
            };
            return cell;
        });

        // 将 CheckBox 放入列头
        HBox header = new HBox(selectAllCheckBox);
        selectCol.setGraphic(header);

        // 设置每一行序号
        rowNumberCol.setCellValueFactory(param -> new SimpleIntegerProperty(
                tableView.getItems().indexOf(param.getValue()) + 1).asObject());
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
            String patientName = (param.getValue().getType() != null) ? param.getValue().getType() == 1?"中医":"西医" : "";
            return new SimpleObjectProperty<>(patientName);
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
    protected void handleUploadSelected() {
        StringBuffer content = new StringBuffer();
        selectedItems.forEach(e -> {
            content.append(e+",");
        });
        // 检查 content 是否为空并去掉最后的逗号
        if (!content.isEmpty()) {
            content.deleteCharAt(content.length() - 1); // 去掉最后一个字符

            Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
            alert.initOwner(stage);
            alert.setTitle("提示");
            alert.setHeaderText("请确认是否要上传选中的病案？");
            alert.setContentText("病案号："+content);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                alert = new Alert (Alert.AlertType.INFORMATION);
                alert.initOwner(stage);
                alert.setTitle("提示");
                alert.setHeaderText("上传成功");
                alert.setContentText("");
                alert.show();
            }
        }
        else {
            Alert alert = new Alert (Alert.AlertType.INFORMATION);
            alert.initOwner(stage);
            alert.setTitle("提示");
            alert.setHeaderText("请至少选中一个病案");
            alert.setContentText("");
            alert.show();
        }
    }

    @FXML
    protected void handleUploadAll() {

    }

    @FXML
    protected void handleCollapse() {
        if(topPane.getChildren().contains(formPane)) {
            topPane.setCenter(null);
            FontIcon icon = new FontIcon("mdal-expand_more");
            icon.setIconSize(14);
            btnCollapse.setGraphic(icon);
        } else {
            topPane.setCenter(formPane);
            FontIcon icon = new FontIcon("mdal-expand_less");
            icon.setIconSize(14);
            btnCollapse.setGraphic(icon);
        }
    }
}
