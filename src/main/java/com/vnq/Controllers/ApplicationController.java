package com.vnq.Controllers;

import com.vnq.Delegates.JsonDelegate;
import com.vnq.Delegates.ReportDelegate;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;

@SuppressWarnings("unchecked")
@Controller("/query")
public class ApplicationController {

    public JsonDelegate jsonDelegate = new JsonDelegate();
    public ReportDelegate reportDelegate = new ReportDelegate();

    @Get("/json/{ReportName}")
    @Produces(MediaType.TEXT_PLAIN)
    public String json(@PathVariable("ReportName") String sqlText) {
        return jsonDelegate.view(sqlText);
    }

    @Get("/report/{ReportName}")
    @Produces(MediaType.APPLICATION_JSON)
    public String report(@PathVariable("ReportName") String sqlText) {
        return reportDelegate.view(sqlText);
    }
}
