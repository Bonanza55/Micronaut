package com.vnq.DTO.Request;

import java.math.BigDecimal;

public class SqlStatementBuilder {
    public String newOrder(NewOrderRequest newOrderRequest) {

        return "insert into public.Order values("
                + newOrderRequest.OrderID + ","
                + newOrderRequest.CustomerID + ","
                + newOrderRequest.SalesID + ","
                + "'" + newOrderRequest.SalesDate + "',"
                + "'" + newOrderRequest.SalesTime + "',"
                + newOrderRequest.Location + ")";
    }

    public String viewCustomer(BigDecimal CustomerID) {

        return "select distinct C.CustomerID,"
                + "        C.LastName,"
                + "        C.FirstName,"
                + "        C.Address,"
                + "        C.City"
                + "  from public.Customer C"
                + " where C.CustomerID = " + CustomerID;
    }


    public String viewItem(BigDecimal ItemID) {

        return "select distinct I.ItemID,"
                + "        I.ItemDesc,"
                + "        I.ItemPrice,"
                + "        I.TaxRate,"
                + "        I.Sale,"
                + "        I.Discount,"
                + "        I.SalesPrice"
                + "  from public.Item I"
                + " where I.ItemID = " + ItemID;
    }

    public String viewOrderItems(BigDecimal OrderID) {

        return "select distinct O.OrderID,"
                + "        I.ItemDesc,"
                + "        OI.Identity,"
                + "        OI.Quantity"
                + "  from public.Order O, public.OrderItem OI, public.Item I"
                + " where O.OrderID = " + OrderID
                + "   and O.OrderID = OI.OrderID"
                + "   and I.ItemID  = OI.ItemID"
                + " group by O.OrderID,I.ItemDesc,OI.Identity,OI.Quantity";
    }

    public String viewOrder(BigDecimal OrderID) {

        return "select distinct O.OrderID,"
                + "        OI.ItemID,"
                + "        I.ItemDesc,"
                + "        I.ItemPrice,"
                + "        sum(OI.Quantity),"
                + "        C.CustomerID,"
                + "        C.LastName,"
                + "        C.FirstName,"
                + "        O.SalesDate,"
                + "        O.SalesTime,"
                + "        sum(I.ItemPrice*OI.Quantity),"
                + "        sum(I.ItemPrice*OI.Quantity)*I.TaxRate,"
                + "        sum(I.ItemPrice*OI.Quantity)*I.TaxRate+sum(I.ItemPrice*OI.quantity)"
                + "  from public.Order O, public.OrderItem OI, public.Customer C, public.Item I"
                + " where O.OrderID = " + OrderID
                + "   and O.OrderID = OI.OrderID"
                + "   and I.ItemID  = OI.ItemID"
                + "   and C.CustomerID  = O.CustomerID"
                + " group by O.OrderID,OI.OrderID,OI.ItemID,I.ItemDesc,I.ItemPrice,OI.Quantity,C.CustomerID,C.LastName,C.FirstName,O.SalesDate,O.SalesTime,I.TaxRate";
    }

    public String addItem(BigDecimal OrderID, BigDecimal ItemID, BigDecimal Quantity) {

        return "insert into public.OrderItem (OrderID,ItemID,Quantity) values("
                + OrderID + ","
                + ItemID + ","
                + Quantity + ")";
    }

    public String deleteOrderItem(BigDecimal OrderID, Integer Identity) {

        return "delete from public.OrderItem where OrderID = " + OrderID + " and Identity = " + Identity;
    }

    public String clearOrder_1(BigDecimal OrderID) {

        return "delete from public.OrderItem where OrderID = " + OrderID;
    }

    public String clearOrder_2(BigDecimal OrderID) {

        return "delete from public.Order where OrderID = " + OrderID;
    }

    public String updatePrice(BigDecimal NewPrice, Integer ItemID) {

        return "update public.Item" + " set ItemPrice = " + NewPrice + " where ItemID = " + ItemID;
    }
}

