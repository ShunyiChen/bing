package com.simeon.bing.dao;

import com.simeon.bing.model.Settings;
import com.simeon.bing.utils.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.simeon.bing.utils.DBUtils.dbConnect;
import static com.simeon.bing.utils.DBUtils.dbDisconnect;

public class ParamsDAO {

    public static Settings findParam(String name) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String sql = "SELECT * FROM sys_param where name='"+name+"'";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rs = DBUtils.dbExecuteQuery(sql);
            //Send ResultSet to the getEmployeeList method and get employee object
            if(rs.next()) {
                return Settings.builder().id(rs.getLong(1)).name(rs.getString(2)).value(rs.getString(3)).remark(rs.getString(4)).build();
            }
        } catch (SQLException e) {
            System.out.println("While findAllSettings an error occurred: " + e);
            //Return exception
            throw e;
        }
        return null;
    }

    public static ObservableList<Settings> findAllParams() throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM bing_param";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rs = DBUtils.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEmployeeList method and get employee object
            ObservableList<Settings> params = getParamsList(rs);
            //Return employee object
            return params;
        } catch (SQLException e) {
            System.out.println("While findAllSettings an error occurred: " + e);
            //Return exception
            throw e;
        }
    }

    private static ObservableList<Settings> getParamsList(ResultSet rs) throws SQLException, ClassNotFoundException {
        ObservableList<Settings> list = FXCollections.observableArrayList();
        while (rs.next()) {
            Settings settings = Settings.builder().id(rs.getLong(1)).name(rs.getString(2)).value(rs.getString(3)).remark(rs.getString(4)).build();
            list.add(settings);
        }
        return list;
    }

    public static void updateBatch(List<Settings> params) throws SQLException, ClassNotFoundException {
        //Execute UPDATE operation
        String sql = "UPDATE sys_param SET value=? WHERE name=?";
        dbConnect();
        PreparedStatement statement = DBUtils.getConn().prepareStatement(sql);
        for (Settings settings : params) {
            statement.setString(1, settings.getValue());
            statement.setString(2, settings.getName());

            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();
        //Close connection
        dbDisconnect();
    }
}
