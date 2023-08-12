package com.vnq.Delegates.Orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnq.Constants.GlobalConstants;
import com.vnq.DTO.Request.SqlStatementBuilder;
import com.vnq.DTO.Response.ItemResponse;
import com.vnq.DTO.Response.OrderList;
import com.vnq.DTO.Response.OrderSummary;
import com.vnq.DTO.Response.ReportHeader;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GetOrders {

    String ORDER = "List Of Orders";
    SqlProperties sqlProperties = new SqlProperties();

    public String getOrders() {

        // GET-TIMESTAMP
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        // JSON-OBJECT
        ObjectMapper objectMapper = new ObjectMapper();

        // DELEGATE-RESPONSE
        OrderList orderList = new OrderList();
        ArrayList<OrderList> orderSummaryList  = new ArrayList<>();

        // THE_RESPONSE-FORMATTED
        ArrayList<String> response = new ArrayList<>();

        // GET-REPORT-HEADER
        ReportHeader reportHeader = new ReportHeader();
        reportHeader.ReportName = ORDER;
        reportHeader.ReportDate = timeStamp;

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        // BUILD-SQL
        SqlStatementBuilder sqlStatementBuilder = new SqlStatementBuilder();

        // GET-RESULTS
        try {
            ResultSet sqlRs = db.query(sqlStatementBuilder.getOrders());
            while (sqlRs.next()) {
                orderList.OrderID = sqlRs.getString(1).trim();
                orderSummaryList.add(orderList);
                orderList = new OrderList();
            }
            db.commit();
            db.close();
        } catch (SQLException ex4) {
            return GlobalConstants.DB_OPERATION_ERROR;
        }
        try {
            response.add(objectMapper.writeValueAsString(reportHeader));
            response.add(objectMapper.writeValueAsString(orderSummaryList));
            return response.toString();
        } catch (JsonProcessingException ex4) {
            return GlobalConstants.JSON_PARSE_ERROR;
        }
    }
}