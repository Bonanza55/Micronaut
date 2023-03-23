package com.vnq.Delegates;

import com.vnq.Constants.Constants;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;

import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ReportDelegate {
    SqlProperties sqlProperties = new SqlProperties();
    Constants constants = new Constants();

    public String view(String sqlText) {

        String dataElement;
        int rowCount = 0;
        int totalRows = 0;

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        //GET-SQL-FILE
        String sqlTDL = db.getSqlCmd(sqlText);
        if (sqlTDL.equals(constants.DBMS_ERROR_GENERAL) || sqlTDL.isEmpty() || sqlText.isEmpty()) {
            return "**** " + sqlText + " Not found ****";
        }

        // GET-RESULTS
        try {
            FileWriter fw;
            if (0 == sqlProperties.outFile.compareTo("stdout")) {
                fw = new FileWriter(FileDescriptor.out);
            } else {
                fw = new FileWriter(sqlProperties.outFile);
            }
            fw.write("***** Result Set *****\n\n");
            try {
                ResultSet sqlRs = db.query(sqlTDL);
                ResultSetMetaData rsmd = sqlRs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                // Write column names
                for (int i = 1; i <= columnCount; i++) {
                    if (i == columnCount) {
                        fw.write(String.format("%-20s\n", rsmd.getColumnName(i)));
                    } else {
                        fw.write(String.format("%-20s", rsmd.getColumnName(i)));
                    }
                }
                fw.flush();
                // Write data rows
                while (sqlRs.next()) {
                    totalRows++;
                    for (int i = 1; i <= columnCount; i++) {
                        dataElement = sqlRs.getString(i);
                        if (i == columnCount) {
                            fw.write(String.format("%-20s\n", dataElement));
                        } else {
                            fw.write(String.format("%-20s", dataElement));
                        }
                        rowCount++;
                        if (rowCount == 5000) {
                            db.commit();
                            rowCount = 0;
                        }
                    }
                }
                db.commit();
                db.close();
                fw.write("\n***** Returned: " + totalRows + " Rows *****\n");
            } catch (SQLException ex4) {
                fw.write("\n***** DB-FETCH Error ReportDelegate  *****\n");
            }
            fw.flush();
        } catch (IOException e) {
            return "\n***** OPEN OUTPUT FILE Error ReportDelegate  *****\n";
        }
        return sqlProperties.errTxt;
    }
}
