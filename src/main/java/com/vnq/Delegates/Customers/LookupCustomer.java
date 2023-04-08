package com.vnq.Delegates.Customers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnq.Constants.GlobalConstants;
import com.vnq.DTO.Request.SqlStatementBuilder;
import com.vnq.DTO.Response.CustomerResponse;
import com.vnq.DTO.Response.ReportHeader;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LookupCustomer {

    String CUSTOMER = "Customer Detail";
    SqlProperties sqlProperties = new SqlProperties();

    public String lookupCustomer(BigDecimal CustomerID) {

        // GET-TIMESTAMP
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        // JSON-OBJECT
        ObjectMapper objectMapper = new ObjectMapper();

        // DELEGATE-RESPONSE
        CustomerResponse customerResponse = new CustomerResponse();
        ArrayList<CustomerResponse> customerResponseArrayList = new ArrayList<>();

        // THE_RESPONSE-FORMATTED
        ArrayList<String> response = new ArrayList<>();

        // GET-REPORT-HEADER
        ReportHeader reportHeader = new ReportHeader();
        reportHeader.ReportName = CUSTOMER;
        reportHeader.ReportDate = timeStamp;

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        // BUILD-SQL
        SqlStatementBuilder sqlStatementBuilder = new SqlStatementBuilder();

        // GET-RESULTS
        try {
            ResultSet sqlRs = db.query(sqlStatementBuilder.viewCustomer(CustomerID));
            while (sqlRs.next()) {
                customerResponse.CustomerID = sqlRs.getString(1).trim();
                customerResponse.LastName = sqlRs.getString(2).trim();
                customerResponse.FirstName = sqlRs.getString(3).trim();
                customerResponse.Address = sqlRs.getString(4).trim();
                customerResponse.City = sqlRs.getString(5).trim();
                customerResponseArrayList.add(customerResponse);
                customerResponse = new CustomerResponse();
            }
            db.commit();
            db.close();
        } catch (SQLException ex4) {
            return GlobalConstants.DB_OPERATION_ERROR;
        }
        try {
            response.add(objectMapper.writeValueAsString(reportHeader));
            response.add(objectMapper.writeValueAsString(customerResponseArrayList));
            return response.toString();
        } catch (JsonProcessingException ex4) {
            return GlobalConstants.JSON_PARSE_ERROR;
        }
    }
}