package com.vnq.Controllers;

import com.vnq.DTO.Request.*;
import com.vnq.Delegates.Customers.ViewCustomers;
import com.vnq.Delegates.Items.AddItem;
import com.vnq.Delegates.Items.UpdatePrice;
import com.vnq.Delegates.Items.ViewItems;
import com.vnq.Delegates.Orders.NewOrder;
import com.vnq.Delegates.Orders.ViewOrder;
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
    public NewOrder newOrder = new NewOrder();
    public AddItem addItem = new AddItem();

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
    public String NewOrder(@Body AddItemRequest addItemRequest) {
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

    // View Order
    @Operation(summary = "View Customer Order",
            description = "Call the DB and View Order",
            requestBody = @RequestBody(description = "View Order Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ViewOrderRequest.class))),
            responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/viewOrder/")
    public String UpdateItemPrice(@Body ViewOrderRequest viewOrderRequest) {
        return viewOrder.viewOrder(viewOrderRequest.OrderID);
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