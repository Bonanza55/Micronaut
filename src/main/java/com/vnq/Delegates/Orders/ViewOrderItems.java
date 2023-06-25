package com.vnq.Delegates.Orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnq.Constants.GlobalConstants;
import com.vnq.DTO.Request.SqlStatementBuilder;
import com.vnq.DTO.Response.OrderItemBasic;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ViewOrderItems {

    SqlProperties sqlProperties = new SqlProperties();

    public String viewOrderItems(BigDecimal OrderID) {

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
                orderItemBasic.ItemID   = sqlRs.getString(2).trim();
                orderItemBasic.ItemDesc = sqlRs.getString(3).trim();
                orderItemBasic.Identity = sqlRs.getString(4).trim();
                orderItemBasic.Quantity = sqlRs.getString(5).trim();
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