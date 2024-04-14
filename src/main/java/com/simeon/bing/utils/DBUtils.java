package com.simeon.bing.utils;


import lombok.Getter;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class DBUtils {
    //Declare JDBC Driver
//    private static final String JDBC_DRIVER = "org.sqlite.JDBC";

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    //Connection
    @Getter
    private static Connection conn = null;

    //Connection String
    //String connStr = "jdbc:oracle:thin:Username/Password@IP:Port/SID";
    //Username=HR, Password=HR, IP=localhost, IP=1521, SID=xe
//    private static final String connStr = "jdbc:sqlite:"+ Constants.DB_DATA;
    private static final String connStr = "jdbc:mysql://rm-cn-o493o4i3h00045qo.rwlb.rds.aliyuncs.com/bing?user=maxtreesoftware&password=cDe3@wsx";

    //Connect to DB
    public static void dbConnect() throws SQLException, ClassNotFoundException {
        //Settings Oracle JDBC Driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Sqlite3 JDBC Driver?");
            e.printStackTrace();
            throw e;
        }

//        System.out.println("Sqlite3 JDBC Driver Registered!");

        System.out.println("MySQL8 JDBC Driver Registered!");

        //Establish the Oracle Connection using Connection String
        try {
            conn = DriverManager.getConnection(connStr);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
            throw e;
        }
    }

    //Close Connection
    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    //DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        CachedRowSet crs;
        ResultSet resultSet = null;
        PreparedStatement pstmt = null;
        try {
            dbConnect();
            pstmt = conn.prepareStatement(queryStmt);
            resultSet = pstmt.executeQuery();
            RowSetFactory factory = RowSetProvider.newFactory();
            crs = factory.createCachedRowSet();
            crs.populate(resultSet);
            return crs;
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (pstmt != null) {
                //Close Statement
                pstmt.close();
            }
            //Close connection
            dbDisconnect();
        }
//        //Declare statement, resultSet and CachedResultSet as null
//        Statement stmt = null;
//        ResultSet resultSet = null;
//        CachedRowSet crs = null;
//        try {
//            //Connect to DB (Establish Oracle Connection)
//            dbConnect();
//            System.out.println("Select statement: " + queryStmt + "\n");
//
//            //Create statement
//            stmt = conn.createStatement();
//
//            //Execute select (query) operation
//            resultSet = stmt.executeQuery(queryStmt);
//
//            //CachedRowSet Implementation
//            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
//            //We are using CachedRowSet
//            RowSetFactory factory = RowSetProvider.newFactory();
//            crs = factory.createCachedRowSet();
//            crs.populate(resultSet);
//        } catch (SQLException e) {
//            System.out.println("Problem occurred at executeQuery operation : " + e);
//            throw e;
//        } finally {
//            if (resultSet != null) {
//                //Close resultSet
//                resultSet.close();
//            }
//            if (stmt != null) {
//                //Close Statement
//                stmt.close();
//            }
//            //Close connection
//            dbDisconnect();
//        }
//        //Return CachedRowSet
//        return crs;
    }

    //DB Execute Update (For Update/Insert/Delete) Operation
    public static Long dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        PreparedStatement stmt = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            dbConnect();
            //Create Statement
            stmt = conn.prepareStatement(sqlStmt, PreparedStatement.RETURN_GENERATED_KEYS);
            //Run executeUpdate operation with given sql statement
            stmt.executeUpdate();

            // 获取生成的ID
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long generatedId = generatedKeys.getLong(1);
                System.out.println("Generated ID: " + generatedId);
                return generatedId;
            }
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
        return 0L;
    }

}