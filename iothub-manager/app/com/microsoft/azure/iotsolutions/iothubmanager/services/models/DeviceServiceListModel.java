// Copyright (c) Microsoft. All rights reserved.

package com.microsoft.azure.iotsolutions.iothubmanager.services.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DeviceServiceListModel {

    private String ContinuationToken;
    private List<DeviceServiceModel> items;

    public DeviceServiceListModel(List<DeviceServiceModel> devices, String continuationToken) {
        this.ContinuationToken = continuationToken;
        this.items = devices;
    }

    @JsonProperty("Items")
    public List<DeviceServiceModel> getItems() {
        return this.items;
    }

    public String getContinuationToken() {
        return this.ContinuationToken;
    }
}
