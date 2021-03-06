// Copyright (c) Microsoft. All rights reserved.

package com.microsoft.azure.iotsolutions.devicetelemetry.services.storage.storageAdapter;

import com.google.inject.Inject;
import com.microsoft.azure.iotsolutions.devicetelemetry.services.runtime.IServicesConfig;
import play.libs.ws.WSClient;

public class StorageAdapterClient implements IStorageAdapterClient {

    private final IServicesConfig servicesConfig;
    private String storageAdapterWebserviceUrl;

    private WSClient wsClient;

    @Inject
    public StorageAdapterClient(final IServicesConfig config, WSClient wsClient) {
        this.servicesConfig = config;
        this.storageAdapterWebserviceUrl = servicesConfig.getKeyValueStorageUrl();
        this.wsClient = wsClient;
    }

    // TODO: Move CRUD methods from Rules.java to this class
}
