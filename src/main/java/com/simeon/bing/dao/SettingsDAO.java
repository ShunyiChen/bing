package com.simeon.bing.dao;

import com.simeon.bing.model.Settings;
import com.simeon.bing.model.User;
import com.simeon.bing.utils.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.simeon.bing.utils.DBUtils.dbConnect;
import static com.simeon.bing.utils.DBUtils.dbDisconnect;

public class SettingsDAO {

    public static ObservableList<Settings> findAllSettings() throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM sys_param";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmps = DBUtils.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEmployeeList method and get employee object
            ObservableList<Settings> list = getSettingsList(rsEmps);
            //Return employee object
            return list;
        } catch (SQLException e) {
            System.out.println("While findAllSettings an error occurred: " + e);
            //Return exception
            throw e;
        }
    }

    private static ObservableList<Settings> getSettingsList(ResultSet rs) throws SQLException, ClassNotFoundException {
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
