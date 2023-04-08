package com.vnq.Delegates.Items;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnq.Constants.GlobalConstants;
import com.vnq.DTO.Response.ItemResponse;
import com.vnq.DTO.Response.ReportHeader;
import com.vnq.DTO.Response.ReportTrailer;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ViewItems {

    String ITEMS = "Items";
    SqlProperties sqlProperties = new SqlProperties();

    public String viewItems() {

        // GET-TIMESTAMP
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        // JSON-OBJECT
        ObjectMapper objectMapper = new ObjectMapper();

        // DELEGATE-RESPONSE
        ItemResponse itemResponse = new ItemResponse();
        ArrayList<ItemResponse> itemResponseList = new ArrayList<>();

        // THE_RESPONSE-FORMATTED
        ArrayList<String> response = new ArrayList<>();

        // GET-REPORT-HEADER
        ReportHeader reportHeader = new ReportHeader();
        reportHeader.ReportName = ITEMS;
        reportHeader.ReportDate = timeStamp;

        // GET-REPORT-TRAILER
        ReportTrailer reportTrailer = new ReportTrailer();

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        // GET-RESULTS
        try {
            int rowCount = 0;
            ResultSet sqlRs = db.query(db.getSqlCmd(ITEMS));
            while (sqlRs.next()) {
                itemResponse.ItemID = sqlRs.getString(1).trim();
                itemResponse.ItemPrice = sqlRs.getString(2).trim();
                itemResponse.TaxRate = sqlRs.getString(3).trim();
                itemResponse.Sale = sqlRs.getString(4).trim();
                itemResponse.Discount = sqlRs.getString(5).trim();
                itemResponse.SalesPrice = sqlRs.getString(6).trim();
                itemResponse.ItemDesc = sqlRs.getString(7).trim();
                itemResponseList.add(itemResponse);
                itemResponse = new ItemResponse();
                rowCount++;
            }
            db.commit();
            db.close();
            reportTrailer.RowCount = String.valueOf(rowCount);
        } catch (SQLException ex4) {
            return GlobalConstants.DB_OPERATION_ERROR;
        }
        try {
            response.add(objectMapper.writeValueAsString(reportHeader));
            response.add(objectMapper.writeValueAsString(itemResponseList));
            response.add(objectMapper.writeValueAsString(reportTrailer));
            return response.toString();
        } catch (JsonProcessingException ex4) {
            return GlobalConstants.JSON_PARSE_ERROR;
        }
    }
}