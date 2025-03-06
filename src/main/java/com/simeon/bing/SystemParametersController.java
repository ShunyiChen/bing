package com.simeon.bing;

import com.simeon.bing.model.LocalParameter;
import com.simeon.bing.utils.YamlUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SystemParametersController {
    private Stage stage;
    private ObservableList<LocalParameter> data = null;
    private String yamlPath = System.getProperty("user.home") + "/" + "bing.yaml";
    @FXML
    protected TableView<LocalParameter> tableView;
    @FXML
    protected TableColumn<LocalParameter, String> nameColumn;
    @FXML
    protected TableColumn<LocalParameter, String> valueColumn;

    @FXML
    private void initialize() {
        // 创建表格
        tableView.setEditable(true);

        // 创建参数名称列
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            LocalParameter parameter = event.getTableView().getItems().get(event.getTablePosition().getRow());
            parameter.setName(event.getNewValue());
        });

        // 创建参数值列
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueColumn.setOnEditCommit(event -> {
            LocalParameter parameter = event.getTableView().getItems().get(event.getTablePosition().getRow());
            parameter.setValue(event.getNewValue());
        });

        // 从YAML文件中加载参数数据
        data = FXCollections.observableArrayList();
        try {
            Map<String, String> parameters = YamlUtils.loadFromYaml(yamlPath, HashMap.class);
            if (parameters != null) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    data.add(new LocalParameter(entry.getKey(), entry.getValue()));
                }
            }
        } catch (IOException e) {
            System.out.println("无法加载YAML文件，将使用默认数据。");
            // 如果文件不存在或加载失败，使用默认数据
//            data.add(new LocalParameter("param1", "none"));
//            data.add(new LocalParameter("param2", "none"));
        }
        tableView.setItems(data);
    }

    @FXML
    protected void handleSaving() {
        // 将数据保存到YAML文件
        Map<String, String> parameters = new HashMap<>();
        for (LocalParameter param : data) {
            parameters.put(param.getName(), param.getValue());
        }
        try {
            YamlUtils.saveToYaml(parameters, yamlPath);
            Alert alert = new Alert (Alert.AlertType.INFORMATION);
            alert.initOwner(stage);
            alert.setTitle("提示");
            alert.setHeaderText("保存成功");
            alert.setContentText("");
            alert.show();

            Settings.LOCAL_STORAGE_PATH = parameters.get(Settings.LOCAL_STORAGE_PATH_KEY);
            Settings.CAPTURE_PLUGIN = parameters.get(Settings.CAPTURE_PLUGIN_KEY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
