package com.vnq.Delegates.Orders;

import com.vnq.Constants.GlobalConstants;
import com.vnq.DTO.Request.AddItemRequest;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;
import com.vnq.DTO.Request.SqlStatementBuilder;

public class AddItem {
    SqlProperties sqlProperties = new SqlProperties();
    public ViewOrder viewOrder = new ViewOrder();
    public String addItem(AddItemRequest addItemRequest) {

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        // BUILD SQL
        SqlStatementBuilder sqlStatementBuilder = new SqlStatementBuilder();

        // GET-RESULTS
        if (db.update(sqlStatementBuilder.addItem(addItemRequest.OrderID,
                addItemRequest.ItemID,
                addItemRequest.Quantity)) != GlobalConstants.INSERT_FAIL) {
            db.commit();
            db.close();
            return viewOrder.viewOrder(addItemRequest.OrderID);
        } else {
            db.close();
            return GlobalConstants.DB_OPERATION_ERROR;
        }
    }
}