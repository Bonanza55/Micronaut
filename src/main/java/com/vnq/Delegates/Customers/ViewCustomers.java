package com.vnq.Delegates.Customers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnq.Constants.GlobalConstants;
import com.vnq.DTO.Response.CustomerResponse;
import com.vnq.DTO.Response.ReportHeader;
import com.vnq.DTO.Response.ReportTrailer;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ViewCustomers {

    String CUSTOMERS = "Customers";
    SqlProperties sqlProperties = new SqlProperties();

    public String viewCustomers() {

        // GET-TIMESTAMP
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        // JSON-OBJECT
        ObjectMapper objectMapper = new ObjectMapper();

        // DELEGATE-RESPONSE
        CustomerResponse customerResponse = new CustomerResponse();
        ArrayList<CustomerResponse> customerResponseList = new ArrayList<>();

        // THE_RESPONSE-FORMATTED
        ArrayList<String> response = new ArrayList<>();

        // GET-REPORT-HEADER
        ReportHeader reportHeader = new ReportHeader();
        reportHeader.ReportName = CUSTOMERS;
        reportHeader.ReportDate = timeStamp;

        // GET-REPORT-TRAILER
        ReportTrailer reportTrailer = new ReportTrailer();

        // OPEN-DB-CONNECTION
        sqlProperties.getSqlProperties();
        Sql db = new Sql(sqlProperties.driver, sqlProperties.uid, sqlProperties.server);

        // GET-RESULTS
        try {
            int rowCount = 0;
            ResultSet sqlRs = db.query(db.getSqlCmd(CUSTOMERS));
            while (sqlRs.next()) {
                customerResponse.CustomerID = sqlRs.getString(1).trim();
                customerResponse.LastName = sqlRs.getString(2).trim();
                customerResponse.FirstName = sqlRs.getString(3).trim();
                customerResponse.Address = sqlRs.getString(4).trim();
                customerResponse.City = sqlRs.getString(5).trim();
                customerResponseList.add(customerResponse);
                customerResponse = new CustomerResponse();
                rowCount++;
            }
            db.commit();
            db.close();
            reportTrailer.RowCount = String.valueOf(rowCount);
        } catch (SQLException ex4) {
            return GlobalConstants.DB_OPERATION_ERROR;
        }
        try {
            response.add(objectMapper.writeValueAsString(reportHeader));
            response.add(objectMapper.writeValueAsString(customerResponseList));
            response.add(objectMapper.writeValueAsString(reportTrailer));
            return response.toString();
        } catch (JsonProcessingException ex4) {
            return GlobalConstants.JSON_PARSE_ERROR;
        }
    }
}