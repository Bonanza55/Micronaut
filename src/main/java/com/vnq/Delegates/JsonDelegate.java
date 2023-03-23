package com.vnq.Delegates;

import com.vnq.Constants.Constants;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@SuppressWarnings("unchecked")
public class JsonDelegate {
    Constants constants = new Constants();
    SqlProperties sqlProperties = new SqlProperties();

    public String view(String sqlText) {

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        //GET-SQL-FILE
        String sqlTDL = db.getSqlCmd(sqlText);
        if (sqlTDL.equals(constants.DBMS_ERROR_GENERAL) || sqlTDL.isEmpty() || sqlText.isEmpty()) {
            return "**** " + sqlText + " Not found ****";
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
                Server.put(sqlText, details);
                rowCount++;
            }
            trailObj.put("Row Count", rowCount);
            trailerA.add(trailObj);
            Server.put("Trailer", trailerA);
            jsonString = Server.toJSONString();
            db.commit();
            db.close();
        } catch (SQLException ex4) {
            return ("\n***** DB-FETCH Error JsonDelegate *****\n");
        }
        return jsonString;
    }
}