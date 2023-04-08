package com.vnq.Delegates.Items;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnq.Constants.GlobalConstants;
import com.vnq.DTO.Request.SqlStatementBuilder;
import com.vnq.DTO.Response.ItemResponse;
import com.vnq.DTO.Response.ReportHeader;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LookupItem {

    String ITEM = "Item Detail";
    SqlProperties sqlProperties = new SqlProperties();

    public String viewItem(BigDecimal ItemID) {

        // GET-TIMESTAMP
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        // JSON-OBJECT
        ObjectMapper objectMapper = new ObjectMapper();

        // DELEGATE-RESPONSE
        ItemResponse itemResponse = new ItemResponse();
        ArrayList<ItemResponse> itemResponseArrayList = new ArrayList<>();

        // THE_RESPONSE-FORMATTED
        ArrayList<String> response = new ArrayList<>();

        // GET-REPORT-HEADER
        ReportHeader reportHeader = new ReportHeader();
        reportHeader.ReportName = ITEM;
        reportHeader.ReportDate = timeStamp;

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        // BUILD-SQL
        SqlStatementBuilder sqlStatementBuilder = new SqlStatementBuilder();

        // GET-RESULTS
        try {
            ResultSet sqlRs = db.query(sqlStatementBuilder.viewItem(ItemID));
            while (sqlRs.next()) {
                itemResponse.ItemID = sqlRs.getString(1).trim();
                itemResponse.ItemDesc = sqlRs.getString(2).trim();
                itemResponse.ItemPrice = sqlRs.getString(3).trim();
                itemResponse.TaxRate = sqlRs.getString(4).trim();
                itemResponse.Sale = sqlRs.getString(5).trim();
                itemResponse.Discount = sqlRs.getString(6).trim();
                itemResponse.SalesPrice = sqlRs.getString(7).trim();
                itemResponseArrayList.add(itemResponse);
                itemResponse = new ItemResponse();
            }
            db.commit();
            db.close();
        } catch (SQLException ex4) {
            return GlobalConstants.DB_OPERATION_ERROR;
        }
        try {
            response.add(objectMapper.writeValueAsString(reportHeader));
            response.add(objectMapper.writeValueAsString(itemResponseArrayList));
            return response.toString();
        } catch (JsonProcessingException ex4) {
            return GlobalConstants.JSON_PARSE_ERROR;
        }
    }
}