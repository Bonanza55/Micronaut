package com.vnq.Delegates.Items;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnq.Constants.GlobalConstants;
import com.vnq.DTO.Request.UpdatePriceRequest;
import com.vnq.DTO.Response.ItemsResponse;
import com.vnq.DTO.Response.ReportHeader;
import com.vnq.DTO.Response.ReportTrailer;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ItemsDelegate {

    String ITEMS = "Items";
    SqlProperties sqlProperties = new SqlProperties();

    public String updatePrice(UpdatePriceRequest updatePriceRequest) {

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        String   sqlStm = "update public.Item";
        sqlStm = sqlStm + " set ItemPrice = " + updatePriceRequest.NewPrice;
        sqlStm = sqlStm + " where ItemID = "  + updatePriceRequest.ItemID;

        // GET-RESULTS
        if (db.update(sqlStm) != GlobalConstants.UPDATE_FAIL) {
            db.commit();
            db.close();
            return GlobalConstants.DB_OPERATION_SUCCESS;
        } else {
            db.close();
            return GlobalConstants.DB_OPERATION_ERROR;
        }
    }

    public String viewItems() {

        // GET-TIMESTAMP
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        // JSON-OBJECT
        ObjectMapper objectMapper = new ObjectMapper();

        // DELEGATE-RESPONSE
        ItemsResponse itemsResponse = new ItemsResponse();
        ArrayList<ItemsResponse> itemsResponseList = new ArrayList<>();

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
                itemsResponse.ItemID = sqlRs.getString(1).trim();
                itemsResponse.ItemPrice = sqlRs.getString(2).trim();
                itemsResponse.TaxRate = sqlRs.getString(3).trim();
                itemsResponse.Sale = sqlRs.getString(4).trim();
                itemsResponse.Discount = sqlRs.getString(5).trim();
                itemsResponse.SalesPrice = sqlRs.getString(6).trim();
                itemsResponse.ItemDesc = sqlRs.getString(7).trim();
                itemsResponseList.add(itemsResponse);
                itemsResponse = new ItemsResponse();
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
            response.add(objectMapper.writeValueAsString(itemsResponseList));
            response.add(objectMapper.writeValueAsString(reportTrailer));
            return response.toString();
        } catch (JsonProcessingException ex4) {
            return GlobalConstants.JSON_PARSE_ERROR;
        }
    }
}