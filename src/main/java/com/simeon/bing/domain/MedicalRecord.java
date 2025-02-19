package com.simeon.bing.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="bing_medical_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {
    private Long id;
    private String institutionCode;
    private String institutionName;
    private String medicalRecordNumber;
    private Integer numberOfAdmissions;
    private Date admissionDate;
    private Date dischargeDate;
    private String name;
    private String gender;
    private Date dateOfBirth;
    private Integer age;
    private Integer dischargeMethod;
}
