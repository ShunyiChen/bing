package com.simeon.bing.dao;

import com.simeon.bing.model.Category;
import com.simeon.bing.model.SubCategory;
import com.simeon.bing.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public static Long insertCategory(Category category) throws SQLException, ClassNotFoundException {
        //Declare a INSERT statement
        String insertStmt =
                "INSERT INTO bing_category(name, pid) VALUES('"+category.getName()+"',"+category.getPid()+")";
        //Execute UPDATE operation
        try {
            return DBUtils.dbExecuteUpdate(insertStmt);

        } catch (SQLException e) {
            System.out.print("Error occurred while insertCategory Operation: " + e);
            throw e;
        }
    }

    public static List<Category> findAllCategories() throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT id, name, pid FROM bing_category";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmp = DBUtils.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getUserFromResultSet method and get User object
            return getCategoriesFromResultSet(rsEmp);
        } catch (SQLException e) {
            System.out.println("While findAllCategories, an error occurred: " + e);
            //Return exception
            throw e;
        }
    }

    private static List<Category> getCategoriesFromResultSet(ResultSet rs) throws SQLException {
        List<Category> list = new ArrayList<>();
        while(rs.next()) {
            Category sc = new Category(rs.getLong(1), rs.getString(2), rs.getLong(3));
            list.add(sc);
        }
        return list;
    }

    public static void deleteCategory(Long id) throws SQLException, ClassNotFoundException {
        //Declare a UPDATE statement
        String updateStmt =
                "DELETE from bing_category WHERE id = " + id;
        try {
            DBUtils.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while deleteCategory Operation: " + e);
            throw e;
        }

        updateStmt =
                "DELETE from bing_subcategory WHERE category_id = " + id;
        try {
            DBUtils.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while deleteCategory Operation: " + e);
            throw e;
        }
    }

    public static void updateCategory(Category category) throws SQLException, ClassNotFoundException {
        //Declare a UPDATE statement
        String updateStmt =
                "UPDATE bing_category set name='"+category.getName()+"' WHERE id = " + category.getId();
        try {
            DBUtils.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while updateCategory Operation: " + e);
            throw e;
        }
    }

}
