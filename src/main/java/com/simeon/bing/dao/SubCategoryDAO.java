package com.simeon.bing.dao;

import com.simeon.bing.model.SubCategory;
import com.simeon.bing.model.User;
import com.simeon.bing.utils.DBUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubCategoryDAO {

    public static List<SubCategory> findListByCategoryId(Long categoryId) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT id, name, RFE, category_id, shortcut FROM sys_subcategory WHERE category_id = "+categoryId;
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rs = DBUtils.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getUserFromResultSet method and get User object
            return getSubCategoriesFromResultSet(rs);
        } catch (SQLException e) {
            System.out.println("While findListByCategoryId, an error occurred: " + e);
            //Return exception
            throw e;
        }
    }

    private static List<SubCategory> getSubCategoriesFromResultSet(ResultSet rs) throws SQLException {
        List<SubCategory> list = new ArrayList<>();
        while(rs.next()) {
            SubCategory sc = new SubCategory();
            sc.setId(rs.getLong(1));
            sc.setName(rs.getString(2));
            sc.setRFE(rs.getBoolean(3));
            sc.setCategoryId(rs.getLong(4));
            sc.setShortcut(rs.getString(5));
            list.add(sc);
        }
        return list;
    }

    private static SubCategory getSubCategoryFromResultSet(ResultSet rs) throws SQLException {
        SubCategory subCategory = null;
        if(rs.next()) {
            subCategory = new SubCategory();
            subCategory.setId(rs.getLong(1));
        }
        return subCategory;
    }


    public static Long insertSubCategory(SubCategory subCategory) throws SQLException, ClassNotFoundException {
        //Declare a INSERT statement
        String insertStmt =
                "INSERT INTO sys_subcategory(name, RFE, category_id) VALUES('"+subCategory.getName()+"',"+subCategory.getRFE()+", "+subCategory.getCategoryId()+")";
        //Execute UPDATE operation
        try {
            return DBUtils.dbExecuteUpdate(insertStmt);

        } catch (SQLException e) {
            System.out.print("Error occurred while insertSubCategory Operation: " + e);
            throw e;
        }
    }

    public static void updateSubCategory(SubCategory subCategory) throws SQLException, ClassNotFoundException {
        //Declare a UPDATE statement
        String updateStmt =
                "UPDATE sys_subcategory SET name='"+subCategory.getName()+"', RFE = " + subCategory.getRFE() +", category_id='"+subCategory.getCategoryId()+"' WHERE id = " + subCategory.getId();
        //Execute UPDATE operation
        try {
            DBUtils.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while updateSubCategory Operation: " + e);
            throw e;
        }
    }

    public static void deleteSubCategory(Long id) throws SQLException, ClassNotFoundException {
        //Declare a UPDATE statement
        String updateStmt =
                "DELETE from sys_subcategory WHERE id = " + id;
        //Execute UPDATE operation
        try {
            DBUtils.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while deleteSubCategory Operation: " + e);
            throw e;
        }
    }

    /**
     * 检查分类名是否存在
     *
     * @param name 用户名
     * @return true存在  false不存在
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static boolean hasExist(String name, String oldName) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM sys_subcategory WHERE name='" + name + "'";
        if(!StringUtils.isEmpty(oldName)) {
            selectStmt = "SELECT * FROM sys_subcategory WHERE name='" + name + "' AND name != '"+oldName+"'";
        }
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rs = DBUtils.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getUserFromResultSet method and get User object
            SubCategory subCategory = getSubCategoryFromResultSet(rs);
            //Return subCategory object
            return subCategory != null;
        } catch (SQLException e) {
            System.out.println("While hasExist an subCategory with " + name + ", an error occurred: " + e);
            //Return exception
            throw e;
        }
    }
}
