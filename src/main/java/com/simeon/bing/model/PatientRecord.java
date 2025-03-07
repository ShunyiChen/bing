package com.simeon.bing.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class PatientRecord extends BaseEntity {
    /** 选择框 */
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    /** ID主键 */
    private Long id;
    /** 机构代码 */
    private String institutionCode;
    /** 机构名称 */
    private String institutionName;
    /** 病案号 */
    private String medicalRecordNumber;
    /** 住院次数 */
    private Integer hospitalizationCount;
    /** 入院日期 */
    private Date admissionDate;
    /** 出院日期 */
    private Date dischargeDate;
    /** 姓名 */
    private String patientName;
    /** 性别 */
    private String gender;
    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    /** 年龄 */
    private Integer age;
    /** 离院方式 */
    private Integer dischargeMethod;
    /** 病案类型 0-西医 1中医 */
    private Integer type;
    /** 病案状态 New (新建)/Submitted (已提交)/Modified (有改动) */
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
}
