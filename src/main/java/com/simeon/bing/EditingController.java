package com.simeon.bing;

import com.simeon.bing.dao.BaseDAO;
import com.simeon.bing.dao.RegDAO;
import com.simeon.bing.model.Base;
import com.simeon.bing.model.Reg;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.UUID;

public class EditingController {
    @FXML
    private RadioButton rd1;
    @FXML
    private RadioButton rd2;
    @FXML
    private RadioButton rd3;
    @FXML
    private Label pname;
    @FXML
    private TextField codeField;
    @FXML
    private TextField nameField;
    @FXML
    private Button btnOK;
    @FXML
    private Button btnReimport;
    @FXML
    private TextField gstlj;
    @FXML
    private TextField gsblj;
    @FXML
    private HBox cj;
    @FXML
    private HBox gst;
    @FXML
    private HBox gsb;
    private Callback<String, String> cancel;
    private Callback<Base, String> ok;
    private String id;
    private Stage stage;
    private int level = 0;
    private String pid;
    private String pCode;
    private String gstxzlj;//公示图选择路径
    private String gsbxzlj;//公示表选择路径

    public void initialize(int i, TreeItem<Base> parent, Callback<String, String> cancel, Callback<Base, String> ok) {
        id = UUID.randomUUID().toString();
        level = i;
        pCode = parent == null?"":parent.getValue().getNo();
        pname.setText(parent == null?"":parent.getValue().getName());
        codeField.setText("");
        nameField.setText("");
        pid = parent == null? "":parent.getValue().getId();
        cj.setDisable(true);
        //镇级
        if(i == 0) {
            rd1.setSelected(true);
            gst.setDisable(true);
            gsb.setDisable(true);
        }
        //村级
        else if(i == 1) {
            rd2.setSelected(true);
            gst.setDisable(true);
            gsb.setDisable(true);
        }
        //屯或组级
        else {
            rd3.setSelected(true);
            gst.setDisable(false);
            gsb.setDisable(false);
        }
        setPromptText(i);
        this.cancel = cancel;
        this.ok = ok;
        resetInputFields();
    }

    private void setPromptText(int level) {
        //镇级
        if(level == 0) {
            codeField.setPromptText("例如:2102832142");
            nameField.setPromptText("例如:兰店乡");
        }
        //村级
        else if(level == 1) {
            codeField.setPromptText("例如:210283214203");
            nameField.setPromptText("例如:磨石房村");
        }
        //屯或组级
        else {
            codeField.setPromptText("例如:21028321420301");
            nameField.setPromptText("例如:大亮屯");
        }
    }

    private void resetInputFields() {
        gstlj.setText("");
        gsblj.setText("");
    }

    public void edit(Stage stage, TreeItem<Base> self, Callback<String, String> cancel, Callback<Base, String> ok) {
        this.stage = stage;
        cj.setDisable(true);
        id = self.getValue().getId();
        level = self.getValue().getLevel();
        pCode = level == 0?"":self.getParent().getValue().getNo();
        pname.setText(level == 0?"":self.getParent().getValue().getName());
        codeField.setText(self.getValue().getNo());
        codeField.requestFocus();
        nameField.setText(self.getValue().getName());
        pid = level == 0?"":self.getParent().getValue().getId();
        //镇级
        if(level == 0) {
            rd1.setSelected(true);
            gst.setDisable(true);
            gsb.setDisable(true);
        }
        //村级
        else if(level == 1) {
            rd2.setSelected(true);
            gst.setDisable(true);
            gsb.setDisable(true);
        }
        //屯或组级
        else {
            rd3.setSelected(true);
            gst.setDisable(false);
            gsb.setDisable(false);
        }
        this.cancel = cancel;
        this.ok = ok;
    }

    @FXML
    protected void onKeyReleased() {
        try {
            onOkClick();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onCancelClick() {
        cancel.call("");
    }

    @FXML
    protected void onOkClick() throws SQLException, ClassNotFoundException {
        if(codeField.getText().trim().equals("")) {
            codeField.requestFocus();
        } else if(nameField.getText().trim().equals("")) {
            nameField.requestFocus();
        } else {
            boolean flag = codeField.getText().startsWith(pCode) && codeField.getText().length() > pCode.length();
            if(!flag) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                alert.setHeaderText("当前编码与上级编码不匹配,编码不能与上级编码相同，请重新输入");
                alert.show();
            } else {
                int c = BaseDAO.searchCount(id, codeField.getText());
                if(c > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                    alert.setHeaderText("编码\""+codeField.getText()+"\"已存在，请重新输入");
                    alert.show();
                } else {

                    Base base = new Base();
                    base.setId(id);
                    base.setNo(codeField.getText());
                    base.setName(nameField.getText());
                    base.setLevel(level);
                    base.setPid(pid);

                    //镇级或村级
                    if(level == 0 || level == 1) {
                        ok.call(base);
                    }
                    //组级
                    else {
                        //公示图
                        if(!StringUtils.isEmpty(gstlj.getText())) {
                            File source = new File(gstlj.getText());
                            File target = new File(MainController.SETTINGS.get("pdf_storage")+"/"+source.getName());
                            if(target.exists()) {
                                ButtonType yes = new ButtonType("是", ButtonBar.ButtonData.YES);
                                ButtonType no = new ButtonType("否", ButtonBar.ButtonData.NO);
                                Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION, "", no, yes);
                                alertConfirm.setHeaderText(source.getName()+"已存在，是否覆盖原文件?");
                                alertConfirm.showAndWait().filter(response -> response == ButtonType.YES).ifPresent(response -> {
                                    try {
                                        Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            } else {
                                try {
                                    Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        //公示表
                        if(!StringUtils.isEmpty(gsblj.getText())) {
                            File source = new File(gsblj.getText());
                            String dw = StringUtils.substringBefore(source.getName(), "土地承包经营权公示表.xls");
                            System.out.println("dw="+dw);
                            if(RegDAO.exist(dw)) {
                                ButtonType yes = new ButtonType("是", ButtonBar.ButtonData.YES);
                                ButtonType no = new ButtonType("否", ButtonBar.ButtonData.NO);
                                Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION, "", no, yes);
                                alertConfirm.setHeaderText(dw+"数据已存在，是否删除原有数据并重新创建?");
                                alertConfirm.showAndWait().filter(response -> response == ButtonType.YES).ifPresent(response -> {
                                    RegDAO.deleteByDw(dw);
                                    insert(source);
                                });
                            } else {
                                insert(source);
                            }
                        }
                        ok.call(base);
                    }
                }
            }
        }
    }

    private void insert(File source) {
        FileInputStream inputStream = null;
        Workbook baeuldungWorkBook = null;
        try {
            inputStream = new FileInputStream(source);
            baeuldungWorkBook = new HSSFWorkbook(inputStream);
            Sheet sheet = baeuldungWorkBook.getSheet("鱼鳞图");

            Row r = sheet.getRow(1);
            HSSFCell c = (HSSFCell) r.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String dw = StringUtils.substringAfter(c.getStringCellValue(), ":").trim();
            HSSFCell c1 = (HSSFCell) r.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String rq = StringUtils.substringAfter(c1.getStringCellValue(), ":").trim();

            String cbfbh = StringUtils.EMPTY;
            String cbfmc = StringUtils.EMPTY;
            String cbflx = StringUtils.EMPTY;
            String jtcy = StringUtils.EMPTY;
            String elhtzmj = StringUtils.EMPTY;
            String sczmj = StringUtils.EMPTY;
            String qqzmj = StringUtils.EMPTY;
            String bz2 = StringUtils.EMPTY;

            int firstRow = sheet.getFirstRowNum() + 4; //跳过表头
            int lastRow = sheet.getLastRowNum();
            for (int index = firstRow + 1; index <= lastRow; index++) {
                Row row = sheet.getRow(index);
                c1 = (HSSFCell) row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if(!StringUtils.isEmpty(c1.getStringCellValue())) {
                    cbfbh = c1.getStringCellValue();
                    if(cbfbh.equals("合计")) {
                        break;
                    }
                }
                HSSFCell c2 = (HSSFCell) row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if(!StringUtils.isEmpty(c2.getStringCellValue())) {
                    cbfmc = c2.getStringCellValue();
                }
                HSSFCell c3 = (HSSFCell) row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if(!StringUtils.isEmpty(c3.getStringCellValue())) {
                    cbflx = c3.getStringCellValue();
                }
                HSSFCell c4 = (HSSFCell) row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if(!StringUtils.isEmpty(c4.getStringCellValue())) {
                    jtcy = c4.getStringCellValue();
                }

                HSSFCell c5 = (HSSFCell) row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c6 = (HSSFCell) row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c7 = (HSSFCell) row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c8 = (HSSFCell) row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c9 = (HSSFCell) row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c10 = (HSSFCell) row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c11 = (HSSFCell) row.getCell(10, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c12 = (HSSFCell) row.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c13 = (HSSFCell) row.getCell(12, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c14 = (HSSFCell) row.getCell(13, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if(!StringUtils.isEmpty(c14.getStringCellValue())) {
                    elhtzmj = c14.getStringCellValue();
                }
                HSSFCell c15 = (HSSFCell) row.getCell(14, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c16 = (HSSFCell) row.getCell(15, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if(!StringUtils.isEmpty(c16.getStringCellValue())) {
                    elhtzmj = c16.getStringCellValue();
                }
                HSSFCell c17 = (HSSFCell) row.getCell(16, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c18 = (HSSFCell) row.getCell(17, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if(!StringUtils.isEmpty(c18.getStringCellValue())) {
                    qqzmj = c18.getStringCellValue();
                }
                HSSFCell c19 = (HSSFCell) row.getCell(18, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c20 = (HSSFCell) row.getCell(19, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c21 = (HSSFCell) row.getCell(20, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c22 = (HSSFCell) row.getCell(21, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c23 = (HSSFCell) row.getCell(22, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c24 = (HSSFCell) row.getCell(23, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c25 = (HSSFCell) row.getCell(24, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c26 = (HSSFCell) row.getCell(25, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c27 = (HSSFCell) row.getCell(26, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c28 = (HSSFCell) row.getCell(27, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c29 = (HSSFCell) row.getCell(28, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c30 = (HSSFCell) row.getCell(29, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                HSSFCell c31 = (HSSFCell) row.getCell(30, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                bz2 = c31.getStringCellValue();

                Reg reg = new Reg()
                        .id(UUID.randomUUID().toString())
                        .dw(dw)
                        .rq(rq)
                        .cbfbh(cbfbh)
                        .cbfmc(cbfmc)
                        .cbflx(cbflx)
                        .jtcy(jtcy)
                        .xm(c5.getStringCellValue())
                        .zjhm(c6.getStringCellValue())
                        .jtgx(c7.getStringCellValue())
                        .sfgyr(c8.getStringCellValue())
                        .bz(c9.getStringCellValue())
                        .dkmc(c10.getStringCellValue())
                        .dkbm(c11.getStringCellValue())
                        .tfbh(c12.getStringCellValue())
                        .elhtmj(c13.getStringCellValue())
                        .elhtzmj(elhtzmj)
                        .scmj(c15.getStringCellValue())
                        .sczmj(sczmj)
                        .qqmj(c17.getStringCellValue())
                        .qqzmj(qqzmj)
                        .east(c19.getStringCellValue())
                        .south(c20.getStringCellValue())
                        .west(c21.getStringCellValue())
                        .north(c22.getStringCellValue())
                        .tdyt(c23.getStringCellValue())
                        .dj(c24.getStringCellValue())
                        .tdlylx(c25.getStringCellValue())
                        .sfjbnt(c26.getStringCellValue())
                        .dklb(c27.getStringCellValue())
                        .zzlx(c28.getStringCellValue())
                        .jyfs(c29.getStringCellValue())
                        .gblx(c30.getStringCellValue())
                        .bzz(bz2);

//                System.out.println(reg);
//                System.out.println();
                RegDAO.insert(reg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baeuldungWorkBook.close();
                inputStream.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    @FXML
    protected void onGstClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择文件");
        fileChooser.setInitialDirectory(
                new File(StringUtils.isEmpty(gstxzlj)?System.getProperty("user.home"):gstxzlj)
        );
        FileChooser.ExtensionFilter pngExtensionFilter =
                new FileChooser.ExtensionFilter(
                        "文件类型 (.pdf)", "*pdf");
        fileChooser.getExtensionFilters().add(pngExtensionFilter);
        fileChooser.setSelectedExtensionFilter(pngExtensionFilter);
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null) {
            gstxzlj = selectedFile.getParent();
            gstlj.setText(selectedFile.getPath());
        }
    }

    @FXML
    protected void onGsbClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择文件");
        fileChooser.setInitialDirectory(
            new File(StringUtils.isEmpty(gsbxzlj)?System.getProperty("user.home"):gsbxzlj)
        );
        FileChooser.ExtensionFilter pngExtensionFilter =
                new FileChooser.ExtensionFilter(
                        "文件类型 (.xls)", "*xls");
        fileChooser.getExtensionFilters().add(pngExtensionFilter);
        fileChooser.setSelectedExtensionFilter(pngExtensionFilter);
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null) {
//            private String gstxzlj;//公示图选择路径
//            private String gsbxzlj;//公示表选择路径
            gsbxzlj = selectedFile.getParent();
            gsblj.setText(selectedFile.getPath());
        }
    }
}
