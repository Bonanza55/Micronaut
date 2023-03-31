package com.vnq.Delegates.Orders;

import com.vnq.Constants.GlobalConstants;
import com.vnq.DTO.Request.NewOrderRequest;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;

public class NewOrder {
    SqlProperties sqlProperties = new SqlProperties();
    public String newOrder(NewOrderRequest newOrderRequest) {

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        String sqlStm = "insert into public.Order values(";
        sqlStm = sqlStm +       newOrderRequest.OrderID    + ",";
        sqlStm = sqlStm +       newOrderRequest.CustomerID + ",";
        sqlStm = sqlStm +       newOrderRequest.SalesID    + ",";
        sqlStm = sqlStm + "'" + newOrderRequest.SalesDate  + "',";
        sqlStm = sqlStm + "'" + newOrderRequest.SalesTime  + "',";
        sqlStm = sqlStm +       newOrderRequest.Location   + ")";

        // GET-RESULTS
        if (db.update(sqlStm) != GlobalConstants.INSERT_FAIL) {
            db.commit();
            db.close();
            return GlobalConstants.DB_OPERATION_SUCCESS;
        } else {
            db.close();
            return GlobalConstants.DB_OPERATION_ERROR;
        }
    }
}