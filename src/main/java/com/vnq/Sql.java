package com.vnq;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class Sql extends com.vnq.Dbms {
    Properties prop   = new Properties();
    String     driver = null;
    String     url    = null;
    String     pwd    = null;

    InputStream input = null;
    Sql(String propFile, String uid, String server) {
        try {
            input = new FileInputStream(propFile);
            prop.load(input);
            driver = prop.getProperty("driver");
            url    = prop.getProperty(server);
            pwd    = prop.getProperty(uid);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } // if
        } // finally
        register(driver);
        connect(url,uid,pwd);
    }
}