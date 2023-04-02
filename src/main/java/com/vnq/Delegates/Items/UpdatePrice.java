package com.vnq.Delegates.Items;

import com.vnq.Constants.GlobalConstants;
import com.vnq.DTO.Request.SqlStatementBuilder;
import com.vnq.DTO.Request.UpdatePriceRequest;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;

public class UpdatePrice {

    String ITEMS = "Items";
    SqlProperties sqlProperties = new SqlProperties();

    public String updatePrice(UpdatePriceRequest updatePriceRequest) {

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        // BUILD-SQL
        SqlStatementBuilder sqlStatementBuilder = new SqlStatementBuilder();

        // GET-RESULTS
        if (db.update(sqlStatementBuilder.updatePrice(updatePriceRequest.NewPrice,updatePriceRequest.ItemID)) != GlobalConstants.UPDATE_FAIL) {
            db.commit();
            db.close();
            return GlobalConstants.DB_OPERATION_SUCCESS;
        } else {
            db.close();
            return GlobalConstants.DB_OPERATION_ERROR;
        }
    }
}
