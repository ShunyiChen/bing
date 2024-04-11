package com.simeon.bing.dao;

import com.simeon.bing.model.Reg;
import com.simeon.bing.utils.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RegDAO {

    public static ObservableList<Reg> searchRegList(String keyword) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM registry where (xm like '%"+keyword+"%' or zjhm like '%"+keyword+"%' or dkbm like '%"+keyword+"%')";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmps = DBUtils.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEmployeeList method and get employee object
            ObservableList<Reg> empList = getRegList(rsEmps);
            //Return employee object
            return empList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

    private static ObservableList<Reg> getRegList(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of Employee objects
        ObservableList<Reg> empList = FXCollections.observableArrayList();
        while (rs.next()) {
            Reg emp = new Reg().id(rs.getString("id"))
                    .dw(rs.getString("dw"))
                    .rq(rs.getString("rq"))
                    .cbfbh(rs.getString("cbfbh"))
                    .cbfmc(rs.getString("cbfmc"))
                    .cbflx(rs.getString("cbflx"))
                    .xm(rs.getString("xm"))
                    .zjhm(rs.getString("zjhm"))
                    .jtgx(rs.getString("jtgx"))
                    .dkmc(rs.getString("dkmc"))
                    .dkbm(rs.getString("dkbm"));
            //Add employee to the ObservableList
            empList.add(emp);
        }
        //return empList (ObservableList of Employees)
        return empList;
    }

    public static Integer searchRegCount() throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT count(id) as c FROM registry";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmps = DBUtils.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEmployeeList method and get employee object
            //Return employee object
            rsEmps.next();
            return rsEmps.getInt("c");
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

    public static boolean exist(String dw) {
        //Declare a SELECT statement
        String selectStmt = "SELECT count(id) as c FROM registry where dw = '"+dw+"'";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rs = DBUtils.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEmployeeList method and get employee object
            //Return employee object
            rs.next();
            return rs.getInt("c") > 0;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("SQL select operation has been failed: " + e);
        }
        return false;
    }

    public static void insert(Reg reg) {
        //Declare a DELETE statement
        String updateStmt =
                "INSERT INTO registry" +
                        "(id,dw,rq,cbfbh,cbfmc,jtcy,xm,zjhm,jtgx,sfgyr,bz,dkmc,dkbm,tfbh,elhtmj,elhtzmj,scmj,sczmj,qqmj,qqzmj," +
                        "east,south,west,north,tdyt,dj,tdlylx,sfjbnt,dklb,zzlx,jyfs,gblx,bzz,cbflx)\n" +
                        "VALUES" +
                        "('"+reg.id()+"','"+reg.dw()+"','"+reg.rq()+"','"+reg.cbfbh()+"','"+reg.cbfmc()+"','"+reg.jtcy()+"'," +
                        "'"+reg.xm()+"','"+reg.zjhm()+"','"+reg.jtgx()+"','"+reg.sfgyr()+"','"+reg.bz()+"','"+reg.dkmc()+"'," +
                        "'"+reg.dkbm()+"','"+reg.tfbh()+"','"+reg.elhtmj()+"','"+reg.elhtzmj()+"','"+reg.scmj()+"','"+reg.sczmj()+"'," +
                        "'"+reg.qqmj()+"','"+reg.qqzmj()+"','"+reg.east()+"','"+reg.south()+"','"+reg.west()+"','"+reg.north()+"'," +
                        "'"+reg.tdyt()+"','"+reg.dj()+"','"+reg.tdlylx()+"','"+reg.sfjbnt()+"','"+reg.dklb()+"','"+reg.zzlx()+"'," +
                        "'"+reg.jyfs()+"','"+reg.gblx()+"','"+reg.bzz()+"','"+reg.cbflx()+"')";
        //Execute DELETE operation
        try {
            DBUtils.dbExecuteUpdate(updateStmt);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.print("Error occurred while Insert Operation: " + e);
        }
    }

    public static void deleteByDw(String dw) {
        //Declare a DELETE statement
        String updateStmt =
                "DELETE from registry where dw = '"+dw+"'";
        //Execute DELETE operation
        try {
            DBUtils.dbExecuteUpdate(updateStmt);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.print("Error occurred while Delete Operation: " + e);
        }
    }
}
