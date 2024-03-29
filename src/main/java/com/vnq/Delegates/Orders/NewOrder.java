package com.vnq.Delegates.Orders;

import com.vnq.Constants.GlobalConstants;
import com.vnq.DTO.Request.NewOrderRequest;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;
import com.vnq.DTO.Request.SqlStatementBuilder;

public class NewOrder {
    SqlProperties sqlProperties = new SqlProperties();
    public String newOrder(NewOrderRequest newOrderRequest) {

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        // BUILD-SQL
        SqlStatementBuilder sqlStatementBuilder = new SqlStatementBuilder();

        // GET-RESULTS
        if (db.update(sqlStatementBuilder.newOrder(newOrderRequest)) != GlobalConstants.INSERT_FAIL) {
            db.commit();
            db.close();
            return GlobalConstants.DB_OPERATION_SUCCESS;
        } else {
            db.close();
            return GlobalConstants.DB_OPERATION_ERROR;
        }
    }
}