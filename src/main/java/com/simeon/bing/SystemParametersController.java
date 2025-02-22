package com.simeon.bing;

import com.simeon.bing.model.LocalParameter;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemParametersController {
    private Stage stage;
    @FXML
    protected TableView<LocalParameter> tableView;
    @FXML
    protected TableColumn<LocalParameter, String> nameCol;
    @FXML
    protected TableColumn<LocalParameter, String> valueCol;

    @FXML
    private void initialize() {
        // Setting up the cell value factories for the table columns
        nameCol.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getName() != null
                        ? param.getValue().getName()
                        : ""
        ));
        valueCol.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getValue() != null
                        ? param.getValue().getValue()
                        : ""
        ));
        valueCol.setCellFactory(TextFieldTableCell.forTableColumn());

        LocalParameter localStorage = new LocalParameter();
        localStorage.setName("本地暂存空间");
        localStorage.setValue(System.getProperty("user.home"));
        tableView.getItems().add(localStorage);

        LocalParameter SDKCode = new LocalParameter();
        SDKCode.setName("SDK授权码");
        SDKCode.setValue("1BNDIEOFAZ1Z8R8VNNG4W07HLC9173JJW3RT0P2G9Y28L9YFFIWDBRFNFLFDQBKXAHO9ZE");
        tableView.getItems().add(SDKCode);

        LocalParameter resolution = new LocalParameter();
        resolution.setName("图像处理分辨率");
        resolution.setValue("1024, 1024");
        tableView.getItems().add(resolution);
    }

    @FXML
    protected void handleSaving() {
        Alert alert = new Alert (Alert.AlertType.INFORMATION);
        alert.initOwner(stage);
        alert.setTitle("提示");
        alert.setHeaderText("保存成功");
        alert.setContentText("");
        alert.show();
    }
}
