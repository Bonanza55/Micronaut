package com.vnq.Delegates.Items;

import com.vnq.Constants.GlobalConstants;
import com.vnq.DTO.Request.AddItemRequest;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;
import com.vnq.Delegates.Orders.ViewOrder;

public class AddItem {
    SqlProperties sqlProperties = new SqlProperties();
    public ViewOrder viewOrder = new ViewOrder();
    public String addItem(AddItemRequest addItemRequest) {

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        String sqlStm = "insert into public.OrderItem (OrderID,ItemID,Quantity) values(";
        sqlStm = sqlStm + addItemRequest.OrderID  + ",";
        sqlStm = sqlStm + addItemRequest.ItemID   + ",";
        sqlStm = sqlStm + addItemRequest.Quantity + ")";

        // GET-RESULTS
        if (db.update(sqlStm) != GlobalConstants.INSERT_FAIL) {
            db.commit();
            db.close();
            return viewOrder.viewOrder(addItemRequest.OrderID);
        } else {
            db.close();
            return GlobalConstants.DB_OPERATION_ERROR;
        }
    }
}