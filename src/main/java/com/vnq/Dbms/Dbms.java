package com.vnq.Dbms;

import com.vnq.Constants.GlobalConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;


abstract class Dbms {

    public String DBMS_ERROR_GENERAL = "DBMS_ERROR_GENERAL";
    GlobalConstants constants = new GlobalConstants();
    String dbmsDriver = null;
    int level = 0;
    Connection con;
    Statement stm;
    int go = 0;
    int stop = 0;

    void dbmsError(SQLException ex) {
        if (ex != null) {
          System.err.println("Message:  " + ex.getMessage());
          System.err.println("SQLState: " + ex.getSQLState());
          System.err.println("ErrorCode:" + ex.getErrorCode());
        }
    }

    public void register(String driver) {
        dbmsDriver = driver;
        try {
            Class.forName(dbmsDriver);
        } catch (Exception ex) {
            ex.printStackTrace(); // Got some other type of exception.  Dump it.
        }
    }

    public void connect(String url, String login, String server) {
        try {
            con = DriverManager.getConnection(url, login, server);
            autoCommitOFF();
            level = con.getTransactionIsolation();
            stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stm.setFetchSize(5000);
        } catch (SQLException ex) {
            dbmsError(ex);
        }
    }

    Connection getCon() {
        return con;
    }

    public ResultSet query(String query) {
        ResultSet rs;
        rs = null;
        try {
            rs = stm.executeQuery(query);
        } catch (SQLException ex3) {
            dbmsError(ex3);
        }
        return rs;
    }

    public int update(String statement) {
        int retVal = 0;
        try {
            retVal = stm.executeUpdate(statement);
        } catch(SQLException ex4){
            retVal = GlobalConstants.UPDATE_FAIL;
        }
        return(retVal);
    }


    public void commit() {
        try {
            con.commit();
        } catch (SQLException ex8) {
            dbmsError(ex8);
        }
    }

    public void autoCommitOFF() {
        try {
            con.setAutoCommit(false);
        } catch (SQLException ex6) {
            dbmsError(ex6);
        }
    }

    public void close() {
        try {
            stm.close();
            con.close();
        } catch (SQLException ex2) {
            dbmsError(ex2);
        }
    }

    public String getSqlCmd(String in_fname) {
        String sqlCmd = null;
        try {
            File file = new File(in_fname);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            fileReader.close();
            sqlCmd = stringBuffer.toString();
        } catch (IOException e) {
            return "File " + in_fname + " IO Exception";
        }
        return sqlCmd;
    }
}