package com.simeon.bing.model;

import lombok.Data;

import java.util.Date;

@Data
public class Record {
    /** ID */
    private Long id;
    /** 机构代码 */
    private String orgCode;
    /** 机构名称 */
    private String orgName;
    /** 病案号 */
    private String recordNumber;
    /** 住院次数 */
    private String times;
    /** 入院日期 */
    private Date admitDate;
    /** 出院日期 */
    private Date dischargeDate;
    /** 性名 */
    private String name;
    /** 性别 */
    private Integer gender;
    /** 出生日期 */
    private Date birthdate;
    /** 年龄 */
    private Integer age;
    /** 高院方式 */
    private Integer manner;
    /** 制作状态 */
    private Integer creationStatus;
    /** 制作页数 */
    private Integer creationPage;
    /** 住院号 */
    private String hospitalNum;
    /** 制作日期 */
    private Date creationDate;
    /** 入院科室 */
    private String dept;
    /** 制作用户 */
    private String creatior;
}
