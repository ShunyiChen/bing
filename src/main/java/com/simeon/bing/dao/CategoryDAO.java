package com.simeon.bing.dao;

import com.simeon.bing.model.Category;
import com.simeon.bing.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public static List<Category> findAllCategories() throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM sys_category";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmp = DBUtils.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getUserFromResultSet method and get User object
            return getCategoriesFromResultSet(rsEmp);
        } catch (SQLException e) {
            System.out.println("While findAllUsers, an error occurred: " + e);
            //Return exception
            throw e;
        }
    }

    private static List<Category> getCategoriesFromResultSet(ResultSet rs) throws SQLException {
        List<Category> list = new ArrayList<>();
        while(rs.next()) {
            Category sc = new Category(rs.getLong("id"), rs.getString("name"));
            list.add(sc);
        }
        return list;
    }

}
