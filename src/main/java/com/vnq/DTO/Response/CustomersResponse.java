package com.vnq.DTO.Response;
/*
    select distinct P.PersonID,
        substring(P.LastName,1,30) as LastName,
        substring(P.FirstName,1,30) as FirstName,
        P.Address,
        P.City
        from Persons P
        order by LastName
 */

public class CustomersResponse {
    public String CustomerID;
    public String LastName;
    public String FirstName;
    public String Address;
    public String City;
}
