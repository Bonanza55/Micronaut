package com.vnq.Controllers;

import com.vnq.DataTransferObject.ReportRequest;
import com.vnq.Delegates.WebReportDelegate;
import com.vnq.Delegates.FileReportDelegate;
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

    // JSON report API.
    @Operation(summary = "Run JSON Report",
            description = "Take in a report name and return json",
            parameters = {
                    @Parameter(name = HEADER_X_LOCALE,
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string"),
                            example = "en-US")
            },
            requestBody = @RequestBody(description = "Report request body",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ReportRequest.class))),
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/RunWebReport/")
    public String RunWebReport(@Body ReportRequest reportRequest) {
        return webReportDelegate.view(reportRequest);
    }

    @Operation(summary = "Run DB Report",
            description = "Take in a report name and return rows",
            parameters = {
                    @Parameter(name = HEADER_X_LOCALE,
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string"),
                            example = "en-US")
            },
            requestBody = @RequestBody(description = "Report request body",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ReportRequest.class))),
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/RunFileReport/")
    public String RunFileReport(@Body ReportRequest reportRequest) {
        return fileReportDelegate.view(reportRequest);
    }
}