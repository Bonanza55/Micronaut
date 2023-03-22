package com.vnq;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetSqlProperties {
    String sqlTDL = "";
    String driver = "";
    String uid = "";
    String server = "";
    String outFile = "";
    String errTxt = "";

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
