package com.simeon.bing;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.simeon.bing.dao.RecordDAO;
import com.simeon.bing.model.Record;
import com.simeon.bing.utils.UIUtils;
import com.simeon.bing.utils.YmlUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DIController {

    @Setter
    private Stage stage;
    @FXML
    private TableView<Record> tableView;
    @FXML
    protected TableColumn<Record, String> colOrgCode;
    @FXML
    protected TableColumn<Record, String> colOrgName;
    @FXML
    protected TableColumn<Record, String> colRecordNumber;
    @FXML
    protected TableColumn<Record, String> colTimes;
    @FXML
    protected TableColumn<Record, String> colAdmitDate;
    @FXML
    protected TableColumn<Record, String> colDischargeDate;
    @FXML
    protected TableColumn<Record, String> colName;
    @FXML
    protected TableColumn<Record, String> colGender;
    @FXML
    protected TableColumn<Record, String> colBirthdate;
    @FXML
    protected TableColumn<Record, String> colAge;
    @FXML
    protected TableColumn<Record, String> colManner;
    private File csvFile;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @FXML
    private void initialize() {
        colOrgCode.setCellValueFactory(param -> {
            if(param.getValue().getOrgCode() == null) {
                return new SimpleObjectProperty<>("");
            } else {
                return new SimpleObjectProperty<>(param.getValue().getOrgCode());
            }
        });
        colOrgName.setCellValueFactory(param -> {
            if(param.getValue().getOrgName() == null) {
                return new SimpleObjectProperty<>("");
            } else {
                return new SimpleObjectProperty<>(param.getValue().getOrgName());
            }
        });
        colRecordNumber.setCellValueFactory(param -> {
            if(param.getValue().getRecordNumber() == null) {
                return new SimpleObjectProperty<>("");
            } else {
                return new SimpleObjectProperty<>(param.getValue().getRecordNumber());
            }
        });
        colTimes.setCellValueFactory(param -> {
            if(param.getValue().getTimes() == null) {
                return new SimpleObjectProperty<>("");
            } else {
                return new SimpleObjectProperty<>(param.getValue().getTimes());
            }
        });
        colAdmitDate.setCellValueFactory(param -> {
            if(param.getValue().getAdmitDate() == null) {
                return new SimpleObjectProperty<>("");
            } else {
                return new SimpleObjectProperty<>(dateFormat.format(param.getValue().getAdmitDate()));
            }
        });
        colDischargeDate.setCellValueFactory(param -> {
            if(param.getValue().getDischargeDate() == null) {
                return new SimpleObjectProperty<>("");
            } else {
                return new SimpleObjectProperty<>(dateFormat.format(param.getValue().getDischargeDate()));
            }
        });
        colName.setCellValueFactory(param -> {
            if(param.getValue().getName() == null) {
                return new SimpleObjectProperty<>("");
            } else {
                return new SimpleObjectProperty<>(param.getValue().getName());
            }
        });
        colGender.setCellValueFactory(param -> {
            if(param.getValue().getGender() == null) {
                return new SimpleObjectProperty<>("");
            } else {
                return new SimpleObjectProperty<>(param.getValue().getGender()+"");
            }
        });
        colBirthdate.setCellValueFactory(param -> {
            if(param.getValue().getBirthdate() == null) {
                return new SimpleObjectProperty<>("");
            } else {
                return new SimpleObjectProperty<>(dateFormat.format(param.getValue().getBirthdate()));
            }
        });
        colAge.setCellValueFactory(param -> {
            if(param.getValue().getAge() == null) {
                return new SimpleObjectProperty<>("");
            } else {
                return new SimpleObjectProperty<>(param.getValue().getAge()+"");
            }
        });
        colManner.setCellValueFactory(param -> {
            if(param.getValue().getManner() == null) {
                return new SimpleObjectProperty<>("");
            } else {
                return new SimpleObjectProperty<>(param.getValue().getManner()+"");
            }
        });
    }

    public void resetTable() {
        tableView.getItems().clear();
    }

    @FXML
    protected void onOpenAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开文件");
        String path = YmlUtils.get("csvFileDir");
        if(StringUtils.isNotEmpty(path)) {
            fileChooser.setInitialDirectory(new File(path));
        } else {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }
        FileChooser.ExtensionFilter pngExtensionFilter = new FileChooser.ExtensionFilter(".csv)", "*.csv");
        fileChooser.getExtensionFilters().add(pngExtensionFilter);
        fileChooser.setSelectedExtensionFilter(pngExtensionFilter);
        csvFile = fileChooser.showOpenDialog(stage);
        if(csvFile != null) {
            YmlUtils.set("csvFileDir", csvFile.getParent());
            loadCSVFile(csvFile);
        }
    }

    private void loadCSVFile(File csvFile) {
        try {
            // Create an object of file reader class with CSV file as a parameter.
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(csvFile), "GBK");
            BufferedReader br = new BufferedReader(inputStreamReader);
            // custom separator semi-colon
            CSVParser parser = new CSVParserBuilder().withSeparator(',').build();
            // create csvReader object with parameter
            // filereader and parser
            CSVReader csvReader = new CSVReaderBuilder(br)
                    .withCSVParser(parser)
                    .build();
            // Read all data at once
            List<String[]> allData = csvReader.readAll();
            tableView.getItems().clear();
            // Print Data.
            for (String[] row : allData) {
                Record record = new Record();
                record.setOrgCode(row[0]);
                record.setOrgName(row[1]);
                record.setRecordNumber(row[2]);
                record.setTimes(row[3]);
                record.setAdmitDate(dateFormat.parse(row[4]));
                record.setDischargeDate(dateFormat.parse(row[5]));
                record.setName(row[6]);
                record.setGender(Integer.parseInt(row[7]));
                record.setBirthdate(dateFormat.parse(row[8]));
                record.setAge(Integer.parseInt(row[9]));
                record.setManner(Integer.parseInt(row[10]));
                tableView.getItems().add(record);
            }
            tableView.refresh();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onImportAction() {
        if(tableView.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            alert.setHeaderText("无法导入空数据");
            alert.show();
            return;
        }
        Callback<String, String> callback = s -> {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                RecordDAO.insertRecord(tableView.getItems());
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return null;
        };
        UIUtils uiUtils = new UIUtils(callback, "正在导入数据请稍后");
        uiUtils.loading();
    }

}
