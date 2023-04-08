package com.vnq.Controllers;

import com.vnq.DTO.Request.*;
import com.vnq.Delegates.Customers.LookupCustomer;
import com.vnq.Delegates.Customers.ViewCustomers;
import com.vnq.Delegates.Items.LookupItem;
import com.vnq.Delegates.Orders.*;
import com.vnq.Delegates.Items.UpdatePrice;
import com.vnq.Delegates.Items.ViewItems;
import com.vnq.Delegates.Reports.FileReportDelegate;
import com.vnq.Delegates.Reports.WebReportDelegate;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Controller("/vnq")
public class ApplicationController {

    private static final String HEADER_X_LOCALE = "en-US";
    public WebReportDelegate webReportDelegate = new WebReportDelegate();
    public FileReportDelegate fileReportDelegate = new FileReportDelegate();
    public ViewCustomers viewCustomers = new ViewCustomers();
    public ViewItems viewItems = new ViewItems();
    public UpdatePrice updatePrice = new UpdatePrice();
    public ViewOrder viewOrder = new ViewOrder();
    public ViewOrderItems viewOrderItems = new ViewOrderItems();
    public NewOrder newOrder = new NewOrder();
    public AddItem addItem = new AddItem();
    public ClearOrder clearOrder = new ClearOrder();
    public DeleteItemOrder deleteItemOrder = new DeleteItemOrder();
    public LookupItem lookupItem = new LookupItem();
    public LookupCustomer lookupCustomer = new LookupCustomer();


    // Create New Order
    @Operation(summary = "Create New Order",
            description = "Call the DB and Create New Order",
            requestBody = @RequestBody(description = "New Order Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = NewOrderRequest.class))),
            responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/newOrder/")
    public String NewOrder(@Body NewOrderRequest newOrderRequest) {
        return newOrder.newOrder(newOrderRequest);
    }

    // Add Item To Order
    @Operation(summary = "Add Item To Order",
            description = "Call the DB and Add New Item",
            requestBody = @RequestBody(description = "New Item Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = AddItem.class))),
            responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/addItem/")
    public String AddItem(@Body AddItemRequest addItemRequest) {
        return addItem.addItem(addItemRequest);
    }

    // Update Item Price
    @Operation(summary = "Update Item Price",
            description = "Call the DB and Update Item Price",
            requestBody = @RequestBody(description = "Update Item Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UpdatePriceRequest.class))),
            responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/updateItemPrice/")
    public String UpdateItemPrice(@Body UpdatePriceRequest updatePriceRequest) {
        return updatePrice.updatePrice(updatePriceRequest);
    }

    // View Order Items
    @Operation(summary = "View Customer Order Items",
            description = "Call the DB and View Order Items",
            requestBody = @RequestBody(description = "View Order Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ViewOrderRequest.class))),
            responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/viewOrderItems/")
    public String ViewOrderItems(@Body ViewOrderRequest viewOrderRequest) {
        return viewOrderItems.viewOrderItems(viewOrderRequest.OrderID);
    }

    // View Order
    @Operation(summary = "View Customer Order",
            description = "Call the DB and View Order",
            requestBody = @RequestBody(description = "View Order Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ViewOrderRequest.class))),
            responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/viewOrder/")
    public String ViewOrder(@Body ViewOrderRequest viewOrderRequest) {
        return viewOrder.viewOrder(viewOrderRequest.OrderID);
    }

    // Delete Order Item
    @Operation(summary = "Delete Customer Order Item",
            description = "Call the DB and Delete Order Item",
            requestBody = @RequestBody(description = "Delete Order Item Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = DeleteOrderItemRequest.class))),
            responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/deleteOrderItem/")
    public String ClearOrder(@Body DeleteOrderItemRequest deleteOrderItemRequest) {
        return deleteItemOrder.deleteOrderItem(deleteOrderItemRequest.OrderID,deleteOrderItemRequest.Identity);
    }

    // Lookup Customer
    @Operation(summary = "View Customer",
            description = "Call the DB and View Customer",
            requestBody = @RequestBody(description = "View Customer Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = LookupCustomerRequest.class))),
            responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/lookupCustomer/")
    public String LookupCustomer(@Body LookupCustomerRequest lookupCustomerRequest) {
        return lookupCustomer.lookupCustomer(lookupCustomerRequest.CustomerID);
    }

    // Lookup Item
    @Operation(summary = "View Item",
            description = "Call the DB and View Item",
            requestBody = @RequestBody(description = "View Item Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ViewItemRequest.class))),
            responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/lookupItem/")
    public String ViewItem(@Body ViewItemRequest viewItemRequest) {
        return lookupItem.viewItem(viewItemRequest.ItemID);
    }

    // Clear Order
    @Operation(summary = "Clear Customer Order",
            description = "Call the DB and Clear Order",
            requestBody = @RequestBody(description = "Clear Order Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ClearOrderRequest.class))),
            responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/clearOrder/")
    public String ClearOrder(@Body ClearOrderRequest clearOrderRequest) {
        return clearOrder.clearOrder(clearOrderRequest.OrderID);
    }

    // JSON report API.
    @Operation(summary = "Run Dynamic JSON Report",
            description = "Take in a report name and return json",
            parameters = {
                    @Parameter(name = HEADER_X_LOCALE,
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string"),
                            example = "en-US")
            },
            requestBody = @RequestBody(description = "Report request name",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ReportRequest.class))),
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/RunWebReport/")
    public String RunWebReport(@Body ReportRequest reportRequest) {
        return webReportDelegate.view(reportRequest);
    }

    // Customers Report API
    @Operation(summary = "View Customers Report",
            description = "Call the DB and return all customers",
            responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/viewCustomers/")
    public String ViewCustomers() {
        return viewCustomers.viewCustomers();
    }

    // Items Report API
    @Operation(summary = "View Items Report",
            description = "Call the DB and return all items",
            responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/viewItems/")
    public String ViewItems() {
        return viewItems.viewItems();
    }

    // Run table report
    @Operation(summary = "Run DB Report",
            description = "Take in a report name and return rows",
            parameters = {
                    @Parameter(name = HEADER_X_LOCALE,
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string"),
                            example = "en-US")
            },
            requestBody = @RequestBody(description = "Report request name",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ReportRequest.class))),
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/RunFileReport/")
    public String RunFileReport(@Body ReportRequest reportRequest) {
        return fileReportDelegate.view(reportRequest);
    }
}