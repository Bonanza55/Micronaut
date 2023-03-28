package com.vnq.DTO.Response;
/*
select distinct I.ItemID,
                I.ItemPrice,
                I.TaxRate,
                I.Sale,
                I.Discount,
                I.SalesPrice,
                substring(I.ItemDesc,1,40) as ItemDesc
 from Item I
 order by I.ItemID
*/

public class ItemsResponse {
    public String ItemID;
    public String ItemPrice;
    public String TaxRate;
    public String Sale;
    public String Discount;
    public String SalesPrice;
    public String ItemDesc;
}
