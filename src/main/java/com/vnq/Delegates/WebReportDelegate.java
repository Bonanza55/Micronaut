package com.vnq.Delegates;

import com.vnq.DataTransferObject.ReportRequest;
import com.vnq.Constants.GlobalConstants;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@SuppressWarnings("unchecked")
public class WebReportDelegate {
    SqlProperties sqlProperties = new SqlProperties();

    // Read from DB table and produce JSON response.
    // Input is SQL statement to execute.
    public String view(ReportRequest reportRequest) {

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        //GET-SQL-FILE
        String sqlTDL = db.getSqlCmd(reportRequest.reportName);
        if (sqlTDL.equals(db.DBMS_ERROR_GENERAL) || sqlTDL.isEmpty() || reportRequest.reportName.isEmpty()) {
            return "**** " + reportRequest.reportName + " Not found ****";
        }

        JSONObject Server = new JSONObject();
        JSONObject trailObj = new JSONObject();
        JSONArray trailerA = new JSONArray();
        String jsonString;

        // GET-RESULTS
        try {
            ResultSet sqlRs = db.query(sqlTDL);
            ResultSetMetaData rsmd = sqlRs.getMetaData();
            int cc = rsmd.getColumnCount();
            int j = 0;
            int rowCount = 0;
            String dataElement;
            JSONArray details = new JSONArray();
            while (sqlRs.next()) {
                JSONObject fieldObj = new JSONObject();
                JSONArray fieldA = new JSONArray();
                // Get Column names and data
                for (int i = 1; i <= cc; i++) {
                    dataElement = sqlRs.getString(i).trim();
                    fieldObj.put(rsmd.getColumnName(i), dataElement);
                }
                j++;
                if (j == 5000) {
                    db.commit();
                    j = 0;
                }
                fieldA.add(fieldObj);
                details.add(fieldA);
                Server.put(reportRequest.reportName, details);
                rowCount++;
            }
            trailObj.put("Row Count", rowCount);
            trailerA.add(trailObj);
            Server.put("Trailer", trailerA);
            jsonString = Server.toJSONString();
            db.commit();
            db.close();
        } catch (SQLException ex4) {
            return GlobalConstants.DB_FETCH_ERROR;
        }
        return jsonString;
    }
}