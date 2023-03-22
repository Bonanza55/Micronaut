package com.vnq;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@Controller("/query")
public class ApplicationController {
    GetSqlProperties getSqlProperties = new GetSqlProperties();

    @Get("/json/{sqlFileName}")
    @Produces(MediaType.TEXT_PLAIN)
    public String json(@PathVariable("sqlFileName") String sqlText) {

        // OPEN-DB-CONNECTION
        getSqlProperties.getSqlProperties();
        Sql sql = new Sql(getSqlProperties.driver, getSqlProperties.uid, getSqlProperties.server);

        //GET-SQL-FILE
        String sqlTDL = sql.getSqlCmd(sqlText);

        JSONObject Server = new JSONObject();
        JSONObject trailObj = new JSONObject();
        JSONArray trailerA = new JSONArray();
        String jsonString;

        try {
            ResultSet sqlRs = sql.query(sqlTDL);
            ResultSetMetaData rsmd = sqlRs.getMetaData();
            int cc = rsmd.getColumnCount();
            int j = 0;
            int rowCount = 0;
            String dataElement;
            JSONArray details = new JSONArray();
            while (sqlRs.next()) {
                JSONObject fieldObj = new JSONObject();
                JSONArray fieldA = new JSONArray();
                for (int i = 1; i <= cc; i++) {
                    dataElement = sqlRs.getString(i).trim();
                    fieldObj.put(rsmd.getColumnName(i), dataElement);
                }
                j++;
                if (j == 5000) {
                    sql.commit();
                    j = 0;
                }
                fieldA.add(fieldObj);
                details.add(fieldA);
                Server.put(sqlText, details);
                rowCount++;
            }
            trailObj.put("Row Count", rowCount);
            trailerA.add(trailObj);
            Server.put("Trailer", trailerA);
            jsonString = Server.toJSONString();
            sql.commit();
            sql.close();
            j = 0;
        } catch (SQLException ex4) {
            return "\n***** DB-FETCH Error  *****\n";
        }
        return jsonString;
    }

    @Get("/report/{sqlFileName}")
    @Produces(MediaType.TEXT_PLAIN)
    public String report(@PathVariable("sqlFileName") String sqlText) {

        String dataElement = "0";
        int rowCount = 0;
        int totalRows = 0;

        // OPEN-DB-CONNECTION
        getSqlProperties.getSqlProperties();
        Sql sql = new Sql(getSqlProperties.driver, getSqlProperties.uid, getSqlProperties.server);

        //GET-SQL-FILE
        String sqlTDL = sql.getSqlCmd(sqlText);

        try {
            FileWriter fw;
            if (0 == getSqlProperties.outFile.compareTo("stdout")) {
                fw = new FileWriter(FileDescriptor.out);
            } else {
                fw = new FileWriter(getSqlProperties.outFile);
            }
            fw.write("***** Result Set *****\n\n");
            try {
                ResultSet sqlRs = sql.query(sqlTDL);
                ResultSetMetaData rsmd = sqlRs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    if (i == columnCount) {
                        fw.write(String.format("%-20s\n", rsmd.getColumnName(i)));
                    } else {
                        fw.write(String.format("%-20s", rsmd.getColumnName(i)));
                    }
                }
                fw.flush();
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
                            sql.commit();
                            rowCount = 0;
                        }
                    }
                }
                sql.commit();
                sql.close();
                fw.write("\n***** Returned: " + totalRows + " Rows *****\n");
            } catch (SQLException ex4) {
                return "\n***** DB-FETCH Error  *****\n";
            }
            fw.flush();
            totalRows = 0;
            rowCount = 0;
        } catch (IOException e) {
            return "\n***** OPEN OUTPUT FILE Error  *****\n";
        }
        return getSqlProperties.errTxt;
    }
}
