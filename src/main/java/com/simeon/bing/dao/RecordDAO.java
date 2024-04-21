package com.simeon.bing.dao;

import com.simeon.bing.domain.Record;
import com.simeon.bing.utils.DBUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.simeon.bing.utils.DBUtils.dbConnect;
import static com.simeon.bing.utils.DBUtils.dbDisconnect;

public class RecordDAO {

    public static void insertRecord(List<Record> list) throws SQLException, ClassNotFoundException {
        //Execute UPDATE operation
        String insertSQL = "INSERT INTO sys_record(org_code, org_name, record_number, times, admit_date, discharge_date, name, gender, birth_date, age, manner) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        dbConnect();
        PreparedStatement statement = DBUtils.getConn().prepareStatement(insertSQL);
        for (Record record : list) {
            statement.setString(1, record.getOrgCode());
            statement.setString(2, record.getOrgName());
            statement.setString(3, record.getRecordNumber());
            statement.setString(4, record.getTimes());
            java.sql.Date admitDate
                    = new java.sql.Date(record.getAdmitDate().getTime());
            statement.setDate(5, admitDate);
            java.sql.Date dischargeDate
                    = new java.sql.Date(record.getDischargeDate().getTime());
            statement.setDate(6, dischargeDate);
            statement.setString(7, record.getName());
            statement.setInt(8, record.getGender());
            java.sql.Date birthdate = new java.sql.Date(record.getBirthdate().getTime());
            statement.setDate(9, birthdate);
            statement.setInt(10, record.getAge());
            statement.setInt(11, record.getManner());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();
        //Close connection
        dbDisconnect();
    }
}
