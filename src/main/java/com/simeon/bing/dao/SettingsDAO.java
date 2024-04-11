package com.simeon.bing.dao;

import com.simeon.bing.model.Settings;
import com.simeon.bing.utils.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsDAO {

    public static ObservableList<Settings> searchSettingsList() throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM settings";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmps = DBUtils.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEmployeeList method and get employee object
            ObservableList<Settings> empList = getSettingsList(rsEmps);
            //Return employee object
            return empList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

    private static ObservableList<Settings> getSettingsList(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of Employee objects
        ObservableList<Settings> empList = FXCollections.observableArrayList();
        while (rs.next()) {
            Settings emp = new Settings();
            emp.setId(rs.getString("id"));
            emp.setKey(rs.getString("key"));
            emp.setValue(rs.getString("value"));
            emp.setNotes(rs.getString("notes"));
            //Add employee to the ObservableList
            empList.add(emp);
        }
        //return empList (ObservableList of Employees)
        return empList;
    }
}
