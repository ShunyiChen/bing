package com.simeon.bing.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class GetRecordsReq {
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
    private Date birthDate;
    /** 年龄 */
    private Integer age;
    /** 离院方式 */
    private Integer dischargeMethod;
    /** 病案类型 0-西医 1中医 */
    private Integer type;
    /** 病案状态 New (新建)/Submitted (已提交)/Modified (有改动) */
    private String status;
    /** 创建者 */
    private String createBy;
    /** 创建时间 */
    private Date createTime;
    /** 更新者 */
    private String updateBy;
    /** 更新时间 */
    private Date updateTime;
    /** pageNum(非字段) 页数 */
    private Integer pageNum;
    /** pageSize(非字段) 每页数量 */
    private Integer pageSize;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date admissionDateFrom;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date admissionDateTo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dischargeDateFrom;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dischargeDateTo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDateFrom;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDateTo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTimeFrom;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTimeTo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTimeFrom;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTimeTo;
}
