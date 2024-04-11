package com.simeon.bing;

import com.simeon.bing.model.Reg;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

public class HelloApplication {
    public static void main(String[] args) {
        int wrapLine = 10;

        for(int i=0;i<57;i++) {
            int rowIndex = i / 10;
            int columnIndex = i % 10;
            System.out.println(rowIndex+", "+columnIndex);

        }
    }


    public static void aa(String[] args) {
        System.out.println("---------------------");

        File source = new File("J:\\zhangtao\\样例\\兰店乡-样例\\鲍码村\\兰店乡鲍码村一组土地承包经营权公示表.xls");
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

                System.out.println(reg);
                System.out.println();




            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
}