package com.simeon.bing.dao;

import com.simeon.bing.model.User;
import com.simeon.bing.utils.DBUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public static User findUser(String userName, String password) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM sys_user WHERE username='" + userName + "' and password='" + password + "'";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmp = DBUtils.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getUserFromResultSet method and get User object
            User User = getUserFromResultSet(rsEmp);
            //Return User object
            return User;
        } catch (SQLException e) {
            System.out.println("While findUser with " + userName + " id, an error occurred: " + e);
            //Return exception
            throw e;
        }
    }

    public static List<User> findAllUsers() throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM sys_user";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmp = DBUtils.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getUserFromResultSet method and get User object
            return getUserListFromResultSet(rsEmp);
        } catch (SQLException e) {
            System.out.println("While findAllUsers, an error occurred: " + e);
            //Return exception
            throw e;
        }
    }

    /**
     * 检查用户名是否存在
     *
     * @param userName 用户名
     * @return true存在  false不存在
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static boolean hasExist(String userName, String oldUserName) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM sys_user WHERE username='" + userName + "'";
        if(!StringUtils.isEmpty(oldUserName)) {
            selectStmt = "SELECT * FROM sys_user WHERE username='" + userName + "' AND username != '"+oldUserName+"'";
        }
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmp = DBUtils.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getUserFromResultSet method and get User object
            User user = getUserFromResultSet(rsEmp);
            //Return User object
            return user != null;
        } catch (SQLException e) {
            System.out.println("While hasExist an User with " + userName + " username, an error occurred: " + e);
            //Return exception
            throw e;
        }
    }

    //Use ResultSet from DB as parameter and set User Object's attributes and return User object.
    private static User getUserFromResultSet(ResultSet rs) throws SQLException {
        User emp = null;
        if (rs.next()) {
            emp = new User(rs.getLong("id"), rs.getString("username"), rs.getString("password"), rs.getBoolean("enabled"));
        }
        return emp;
    }

    private static List<User> getUserListFromResultSet(ResultSet rs) throws SQLException {
        List<User> userList = new ArrayList<>();
        while(rs.next()) {
            User user = new User(rs.getLong("id"), rs.getString("username"), rs.getString("password"), rs.getBoolean("enabled"));
            userList.add(user);
        }
        return userList;
    }


    public static Long insertUser(String userName, String password, boolean enabled) throws SQLException, ClassNotFoundException {
        //Declare a INSERT statement
        String insertStmt =
                "INSERT INTO sys_user(username, password, enabled) VALUES('"+userName+"','"+password+"', "+enabled+")";
        //Execute UPDATE operation
        try {
            return DBUtils.dbExecuteUpdate(insertStmt);

        } catch (SQLException e) {
            System.out.print("Error occurred while insertUser Operation: " + e);
            throw e;
        }
    }

    public static void updateUser(User user) throws SQLException, ClassNotFoundException {
        //Declare a UPDATE statement
        String updateStmt =
                "UPDATE sys_user SET username='"+user.getUserName()+"', enabled = " + user.isEnabled() +", password='"+user.getPassword()+"' WHERE id = " + user.getId();
        //Execute UPDATE operation
        try {
            DBUtils.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while updateUser Operation: " + e);
            throw e;
        }
    }

    public static void deleteUser(Long id) throws SQLException, ClassNotFoundException {
        //Declare a UPDATE statement
        String updateStmt =
                "DELETE from sys_user WHERE id = " + id;
        //Execute UPDATE operation
        try {
            DBUtils.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while deleteUser Operation: " + e);
            throw e;
        }
    }






//    public static int userCount() throws SQLException, ClassNotFoundException {
//        //Declare a SELECT statement
//        String selectStmt = "SELECT count(*) FROM sys_user";
//        //Execute SELECT statement
//        try {
//            //Get ResultSet from dbExecuteQuery method
//            ResultSet rsEmp = DBUtil.dbExecuteQuery(selectStmt);
//            rsEmp.next();
//            //Return User object
//            return rsEmp.getInt(0);
//        } catch (SQLException e) {
////            System.out.println("While searching an User with " + empId + " id, an error occurred: " + e);
//            //Return exception
//            throw e;
//        }
//    }



}
