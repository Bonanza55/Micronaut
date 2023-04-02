package com.vnq.Delegates.Orders;

import com.vnq.Constants.GlobalConstants;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;
import com.vnq.DTO.Request.SqlStatementBuilder;

import java.math.BigDecimal;

public class ClearOrder {
    SqlProperties sqlProperties = new SqlProperties();
    public String clearOrder(BigDecimal OrderID) {

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        // BUILD-SQL
        SqlStatementBuilder sqlStatementBuilder = new SqlStatementBuilder();
        // GET-RESULTS
        if (db.update(sqlStatementBuilder.clearOrder_1(OrderID)) != GlobalConstants.INSERT_FAIL) {
            db.commit();
            if (db.update(sqlStatementBuilder.clearOrder_2(OrderID)) != GlobalConstants.INSERT_FAIL) {
                db.commit();
                db.close();
                return GlobalConstants.DB_OPERATION_SUCCESS;
            } else {
                db.close();
                return GlobalConstants.DB_OPERATION_ERROR;
            }
        } else {
            db.close();
            return GlobalConstants.DB_OPERATION_ERROR;
        }
    }
}