package com.simeon.bing.dao;

import com.simeon.bing.model.Base;
import com.simeon.bing.utils.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDAO {

    public static ObservableList<Base> searchBaseList(String id) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM base";
        if(id != null) {
            selectStmt = "SELECT * FROM base where pid = '"+id+"'";
        }
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmps = DBUtils.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEmployeeList method and get employee object
            ObservableList<Base> empList = getBaseList(rsEmps);
            //Return employee object
            return empList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

    private static ObservableList<Base> getBaseList(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of Employee objects
        ObservableList<Base> empList = FXCollections.observableArrayList();
        while (rs.next()) {
            Base emp = new Base();
            emp.setId(rs.getString("id"));
            emp.setName(rs.getString("name"));
            emp.setNo(rs.getString("no"));
            emp.setLevel(rs.getInt("level"));
            emp.setPid(rs.getString("pid"));
            emp.setSelected(rs.getInt("selected"));
            //Add employee to the ObservableList
            empList.add(emp);
        }
        //return empList (ObservableList of Employees)
        return empList;
    }

    public static int searchCount(String id, String code) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT count(*) FROM base where no = '"+code+"' and id <> '"+id+"'";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmps = DBUtils.dbExecuteQuery(selectStmt);
            rsEmps.next();
            //Send ResultSet to the getEmployeeList method and get employee object
            return rsEmps.getInt(1);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

    public static void insert(Base base) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt =
                 "INSERT INTO base\n" +
                        "(id, name, no, level, pid)\n" +
                        "VALUES\n" +
                        "('"+base.getId()+"', '"+base.getName()+"', '"+base.getNo()+"','"+base.getLevel()+"' ,'"+base.getPid()+"')";
        //Execute DELETE operation
        try {
            DBUtils.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while Insert Operation: " + e);
            throw e;
        }
    }

    public static void update(Base base) throws SQLException, ClassNotFoundException {
        //Declare a UPDATE statement
        String updateStmt =
               "   UPDATE base\n" +
                        "  SET name = '" + base.getName() + "'\n" +
                        "  ,no = '" + base.getNo() + "'\n" +
                        "  ,level = '" + base.getLevel() + "'\n" +
                        "  ,pid = '" + base.getPid() + "'\n" +
                        "  ,selected = " + base.getSelected() + "\n" +
                        " WHERE id = '" + base.getId() +"'";
        //Execute UPDATE operation
        try {
            DBUtils.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void deleteBaseById(String id) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt =
               "   DELETE FROM base\n" +
                        "         WHERE id ='"+ id +"'";
        //Execute UPDATE operation
        try {
            DBUtils.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
        deleteChildren(id);
    }

    private static void deleteChildren(String id) throws SQLException, ClassNotFoundException {
//        ObservableList<Base> children = searchBaseList(id);
//        children.forEach(e -> {
//            try {
//
//                //Declare a DELETE statement
//                String updateStmt =
//                        "   DELETE FROM base\n" +
//                                "         WHERE id ='"+ e.getId() +"'";
//                //Execute UPDATE operation
//                DBUtil.dbExecuteUpdate(updateStmt);
//
//                deleteChildren(e.getId());
//            } catch (SQLException ex) {
//                throw new RuntimeException(ex);
//            } catch (ClassNotFoundException ex) {
//                throw new RuntimeException(ex);
//            }
//        });
    }
}
