package com.vnq.Delegates.Orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnq.Constants.GlobalConstants;
import com.vnq.DTO.Response.OrderItemSummary;
import com.vnq.DTO.Response.OrderSummary;
import com.vnq.DTO.Response.ReportHeader;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;
import com.vnq.DTO.Request.SqlStatementBuilder;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ViewOrder {

    String ORDER = "Order Summary";
    SqlProperties sqlProperties = new SqlProperties();

    public String viewOrder(BigDecimal OrderID) {

        // GET-TIMESTAMP
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        // JSON-OBJECT
        ObjectMapper objectMapper = new ObjectMapper();

        // DELEGATE-RESPONSE
        OrderItemSummary orderItemSummary = new OrderItemSummary();
        ArrayList<OrderItemSummary> orderItemSummaryList  = new ArrayList<>();

        // THE_RESPONSE-FORMATTED
        ArrayList<String> response = new ArrayList<>();

        // GET-REPORT-HEADER
        ReportHeader reportHeader = new ReportHeader();
        reportHeader.ReportName = ORDER;
        reportHeader.ReportDate = timeStamp;

        // ORDER-SUMMARY
        OrderSummary orderSummary = new OrderSummary();
        double OrderTotal = 0.0;

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        // BUILD-SQL
        SqlStatementBuilder sqlStatementBuilder = new SqlStatementBuilder();

        // GET-RESULTS
        try {
            ResultSet sqlRs = db.query(sqlStatementBuilder.viewOrder(OrderID));
            while (sqlRs.next()) {
                orderSummary.OrderID = sqlRs.getString(1).trim();
                orderItemSummary.ItemID = sqlRs.getString(2).trim();
                orderItemSummary.ItemDesc = sqlRs.getString(3).trim();
                orderItemSummary.ItemPrice = sqlRs.getString(4).trim();
                orderItemSummary.Quantity = sqlRs.getString(5).trim();
                orderSummary.CustomerID = sqlRs.getString(6).trim();
                orderSummary.LastName = sqlRs.getString(7).trim();
                orderSummary.FirstName = sqlRs.getString(8).trim();
                orderSummary.SalesDate = sqlRs.getString(9).trim();
                orderSummary.SalesTime = sqlRs.getString(10).trim();
                orderItemSummary.ItemSubTotal = sqlRs.getString(11).trim();
                orderItemSummary.ItemTotalTax = sqlRs.getString(12).trim();
                orderItemSummary.ItemTotal = sqlRs.getString(13).trim();
                OrderTotal += Double.parseDouble(orderItemSummary.ItemTotal);
                orderItemSummaryList.add(orderItemSummary);
                orderItemSummary = new OrderItemSummary();
            }
            db.commit();
            db.close();
            orderSummary.OrderTotal = String.valueOf(OrderTotal);
        } catch (SQLException ex4) {
            return GlobalConstants.DB_OPERATION_ERROR;
        }
        try {
            response.add(objectMapper.writeValueAsString(reportHeader));
            response.add(objectMapper.writeValueAsString(orderItemSummaryList));
            response.add(objectMapper.writeValueAsString(orderSummary));
            return response.toString();
        } catch (JsonProcessingException ex4) {
            return GlobalConstants.JSON_PARSE_ERROR;
        }
    }
}