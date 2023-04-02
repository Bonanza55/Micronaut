package com.vnq.Delegates.Orders;

import com.vnq.Constants.GlobalConstants;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;
import com.vnq.DTO.Request.SqlStatementBuilder;

import java.math.BigDecimal;

public class DeleteItemOrder {
    SqlProperties sqlProperties = new SqlProperties();
    public ViewOrder viewOrder = new ViewOrder();

    public String deleteOrderItem(BigDecimal OrderID, Integer Identity) {

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        // BUILD-SQL
        SqlStatementBuilder sqlStatementBuilder = new SqlStatementBuilder();
        // GET-RESULTS
        if (db.update(sqlStatementBuilder.deleteOrderItem(OrderID,Identity)) != GlobalConstants.INSERT_FAIL) {
            db.commit();
            db.close();
            return viewOrder.viewOrder(OrderID);
        } else {
            db.close();
            return GlobalConstants.DB_OPERATION_ERROR;
        }
    }
}