package com.microsoft.azure.iotsolutions.devicetelemetry.webservice.v1.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class QueryApiModel {
    private String from;
    private String to;
    private String order;
    private Integer skip;
    private Integer limit;
    private List<String> devices;

    public QueryApiModel() {
        this.from = "";
        this.to = "";
        this.order = null;
        this.skip = null;
        this.limit = null;
        this.devices = new ArrayList<>();
    }

    public QueryApiModel(String from, String to, String order, Integer skip, Integer limit, List<String> devices) {
        this.from = from;
        this.to = to;
        this.order = order;
        this.skip = skip;
        this.limit = limit;
        this.devices = devices;
    }

    @JsonProperty("From")
    public String getFrom() { return this.from; }

    public void setFrom(String from) { this.from = from; }

    @JsonProperty("To")
    public String getTo() { return this.to; }

    public void setTo(String to) { this.to = to; }

    @JsonProperty("Order")
    public String getOrder() { return this.order; }

    public void setOrder(String order) { this.order = order; }

    @JsonProperty("Skip")
    public Integer getSkip() { return this.skip; }

    public void setSkip(Integer skip) { this.skip = skip; }

    @JsonProperty("Limit")
    public Integer getLimit() { return this.limit; }

    public void setLimit(Integer limit) { this.limit = limit; }

    @JsonProperty("Devices")
    public List<String> getDevices() { return this.devices; }

    public void setDevices(List<String> devices) { this.devices = devices; }

}
