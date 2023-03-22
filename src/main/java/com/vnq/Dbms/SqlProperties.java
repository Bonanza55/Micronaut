package com.vnq.Dbms;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SqlProperties {
    String sqlTDL = "";
    public String driver = "";
    public String uid = "";
    public String server = "";
    public String outFile = "";
    public String errTxt = "";

    // GET-LOGIN-DETAILS
    public void getSqlProperties() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("Login.properties");
            prop.load(input);
            driver = prop.getProperty("driver");
            uid = prop.getProperty("uid");
            server = prop.getProperty("server");
            outFile = prop.getProperty("outFile");
            input.close();
        } catch (IOException ignored) {
        }
    }
}
