package com.vnq.Delegates.Customers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnq.Constants.GlobalConstants;
import com.vnq.DTO.Response.CustomersResponse;
import com.vnq.DTO.Response.ReportHeader;
import com.vnq.DTO.Response.ReportTrailer;
import com.vnq.Dbms.Sql;
import com.vnq.Dbms.SqlProperties;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CustomersDelegate {

    String CUSTOMERS = "Customers";
    SqlProperties sqlProperties = new SqlProperties();

    public String viewCustomers() {

        // GET-TIMESTAMP
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        // JSON-OBJECT
        ObjectMapper objectMapper = new ObjectMapper();

        // DELEGATE-RESPONSE
        CustomersResponse customersResponse = new CustomersResponse();
        ArrayList<CustomersResponse> customersResponseList = new ArrayList<>();

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
                customersResponse.CustomerID = sqlRs.getString(1).trim();
                customersResponse.LastName = sqlRs.getString(2).trim();
                customersResponse.FirstName = sqlRs.getString(3).trim();
                customersResponse.Address = sqlRs.getString(4).trim();
                customersResponse.City = sqlRs.getString(5).trim();
                customersResponseList.add(customersResponse);
                customersResponse = new CustomersResponse();
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
            response.add(objectMapper.writeValueAsString(customersResponseList));
            response.add(objectMapper.writeValueAsString(reportTrailer));
            return response.toString();
        } catch (JsonProcessingException ex4) {
            return GlobalConstants.JSON_PARSE_ERROR;
        }
    }
}