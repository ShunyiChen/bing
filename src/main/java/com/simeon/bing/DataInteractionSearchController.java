package com.simeon.bing;

import com.simeon.bing.model.CallbackParam;
import com.simeon.bing.request.GetRecordsReq;
import com.simeon.bing.response.GetRecordsRes;
import com.simeon.bing.utils.HttpUtil;
import com.simeon.bing.utils.JsonUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
public class DataInteractionSearchController {
    private Callback<CallbackParam, Void> searchCallBack;
    private GetRecordsReq queryParam;
    @FXML
    protected TextField institutionCodeTxt;
    @FXML
    protected TextField institutionNameTxt;
    @FXML
    protected TextField medicalRecordNumberTxt;
    @FXML
    protected TextField hospitalizationCountTxt;
    @FXML
    protected DatePicker admissionDateFromTxt;
    @FXML
    protected DatePicker admissionDateToTxt;
    @FXML
    protected DatePicker dischargeDateFromTxt;
    @FXML
    protected DatePicker dischargeDateToTxt;
    @FXML
    protected TextField patientNameTxt;
    @FXML
    protected ComboBox<String> genderTxt;
    @FXML
    protected DatePicker birthDateFromTxt;
    @FXML
    protected DatePicker birthDateToTxt;
    @FXML
    protected TextField ageTxt;
    @FXML
    protected ComboBox<Integer> dischargeMethodTxt;
    @FXML
    protected RadioButton btnWM;
    @FXML
    protected RadioButton btnTCM;

    @FXML
    private void initialize() {
        genderTxt.getItems().addAll("男", "女");
        // 添加整数 1 到 5
        dischargeMethodTxt.getItems().addAll(1, 2, 3, 4, 5);
    }

    @FXML
    protected void handleSearch() {
        // 获取用户输入的搜索条件
        String institutionCode = institutionCodeTxt.getText();
        String institutionName = institutionNameTxt.getText();
        String medicalRecordNumber = medicalRecordNumberTxt.getText();
        String hospitalizationCount = hospitalizationCountTxt.getText();
        LocalDate admissionDateFrom = admissionDateFromTxt.getValue();
        LocalDate admissionDateTo = admissionDateToTxt.getValue();
        LocalDate dischargeDateFrom = dischargeDateFromTxt.getValue();
        LocalDate dischargeDateTo = dischargeDateToTxt.getValue();
        String patientName = patientNameTxt.getText();
        String gender = genderTxt.getValue();
        LocalDate birthDateFrom = birthDateFromTxt.getValue();
        LocalDate birthDateTo = birthDateToTxt.getValue();
        String age = ageTxt.getText();
        Integer dischargeMethod = dischargeMethodTxt.getValue();
        boolean isWMSelected = btnWM.isSelected();
        boolean isTCMSelected = btnTCM.isSelected();

        queryParam.setInstitutionCode(institutionCode);
        queryParam.setInstitutionName(institutionName);
        queryParam.setMedicalRecordNumber(medicalRecordNumber);
        queryParam.setHospitalizationCount(StringUtils.isNotEmpty(hospitalizationCount)?Integer.parseInt(hospitalizationCount):null);

        if(admissionDateFrom != null && admissionDateTo != null) {
            Date admissionFromDate = Date.from(admissionDateFrom.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            queryParam.setAdmissionDateFrom(admissionFromDate);
            Date admissionToDate = Date.from(admissionDateTo.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            queryParam.setAdmissionDateTo(admissionToDate);
        }
        if(dischargeDateFrom != null && dischargeDateTo != null) {
            Date dischargeFromDate = Date.from(dischargeDateFrom.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            queryParam.setDischargeDateFrom(dischargeFromDate);
            Date dischargeToDate = Date.from(dischargeDateTo.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            queryParam.setDischargeDateTo(dischargeToDate);
        }
        queryParam.setPatientName(patientName);
        queryParam.setGender(gender);
        if(birthDateFrom != null && birthDateTo != null) {
            Date birthFromDate = Date.from(birthDateFrom.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            queryParam.setBirthDateFrom(birthFromDate);
            Date birthToDate = Date.from(birthDateTo.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            queryParam.setBirthDateTo(birthToDate);
        }
        queryParam.setAge(StringUtils.isNotEmpty(age)?Integer.parseInt(age):null);
        queryParam.setDischargeMethod(dischargeMethod);
        if(isWMSelected || isTCMSelected) {
            queryParam.setType(isWMSelected?0:1);
        }
        String jsonInputString;
        try {
            jsonInputString = JsonUtil.toJson(queryParam); // 将对象转换为JSON字符串
            String response = HttpUtil.sendPostRequest(APIs.GET_RECORDS, jsonInputString, TokenStore.getToken());
            GetRecordsRes res = JsonUtil.fromJson(response, GetRecordsRes.class);
            CallbackParam param = new CallbackParam(queryParam, res.getRows(), res.getTotal());
            searchCallBack.call(param);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handleReset() {
        institutionCodeTxt.clear();
        institutionNameTxt.clear();
        medicalRecordNumberTxt.clear();
        hospitalizationCountTxt.clear();
        admissionDateFromTxt.setValue(null);
        admissionDateToTxt.setValue(null);
        dischargeDateFromTxt.setValue(null);
        dischargeDateToTxt.setValue(null);
        patientNameTxt.clear();
        genderTxt.getSelectionModel().clearSelection();
        birthDateFromTxt.setValue(null);
        birthDateToTxt.setValue(null);
        ageTxt.clear();
        dischargeMethodTxt.getSelectionModel().clearSelection();
        btnWM.setSelected(false);
        btnTCM.setSelected(false);
    }
}
