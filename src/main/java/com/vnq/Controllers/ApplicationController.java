package com.vnq.Controllers;

import com.vnq.Delegates.JsonDelegate;
import com.vnq.Delegates.ReportDelegate;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Controller("/query")
public class ApplicationController {

    private static final String HEADER_X_LOCALE = "en-US";
    public JsonDelegate jsonDelegate = new JsonDelegate();
    public ReportDelegate reportDelegate = new ReportDelegate();

    // JSON report.
    @Operation(summary = "Run JSON Report",
            description = "Take in a report name and return json",
            parameters = {
                    @Parameter(name = HEADER_X_LOCALE,
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string"),
                            example = "en-US")
            },
            requestBody = @RequestBody(description = "JSON report request body",
                content = @Content(mediaType = MediaType.APPLICATION_JSON,
                 schema = @Schema(implementation = JSONRequest.class))),
              responses = @ApiResponse(responseCode = "200",
                content = @Content(mediaType = MediaType.ALL)))
    @Post(uri = "/json/{ReportName}")
    public String json(@PathVariable("ReportName") String sqlText,@Body JSONRequest jsonRequest) {
        return jsonDelegate.view(sqlText,jsonRequest);
    }

    // Table report.
    @Post("/report/{ReportName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String report(@Body JSONRequest jsonRequest, @PathVariable("ReportName") String sqlText) {
        return reportDelegate.view(sqlText);
    }
}
