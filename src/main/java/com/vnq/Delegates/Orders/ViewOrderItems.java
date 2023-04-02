package com.vnq.Delegates.Orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnq.Constants.GlobalConstants;
import com.vnq.DTO.Response.OrderItemBasic;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;
import com.vnq.DTO.Request.SqlStatementBuilder;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ViewOrderItems {

    String ORDER = "Order Items";
    SqlProperties sqlProperties = new SqlProperties();

    public String viewOrderItems(BigDecimal OrderID) {

        // GET-TIMESTAMP
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        // JSON-OBJECT
        ObjectMapper objectMapper = new ObjectMapper();

        // DELEGATE-RESPONSE
        OrderItemBasic orderItemBasic = new OrderItemBasic();
        ArrayList<OrderItemBasic> orderItemSummaryList  = new ArrayList<>();

        // THE_RESPONSE-FORMATTED
        ArrayList<String> response = new ArrayList<>();

        // ORDER-SUMMARY
        SqlStatementBuilder sqlStatementBuilder = new SqlStatementBuilder();

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        // GET-RESULTS
        try {
            ResultSet sqlRs = db.query(sqlStatementBuilder.viewOrderItems(OrderID));
            while (sqlRs.next()) {
                orderItemBasic.ItemDesc = sqlRs.getString(2).trim();
                orderItemBasic.Identity = sqlRs.getString(3).trim();
                orderItemBasic.Quantity = sqlRs.getString(4).trim();
                orderItemSummaryList.add(orderItemBasic);
                orderItemBasic = new OrderItemBasic();
            }
            db.commit();
            db.close();
        } catch (SQLException ex4) {
            return GlobalConstants.DB_OPERATION_ERROR;
        }
        try {
            response.add(objectMapper.writeValueAsString(orderItemSummaryList));
            return response.toString();
        } catch (JsonProcessingException ex4) {
            return GlobalConstants.JSON_PARSE_ERROR;
        }
    }
}