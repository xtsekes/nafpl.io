package dev.nafplio.web;

import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

public final class PageParameters {
    @QueryParam("pagesize")
    @DefaultValue("100")
    @Min(value = 1, message = "pageSize must be greater than 0")
    int pageSize;

    @QueryParam("page")
    @DefaultValue("1")
    @Min(value = 1, message = "pageSize must be greater than 0")
    int page;
}
