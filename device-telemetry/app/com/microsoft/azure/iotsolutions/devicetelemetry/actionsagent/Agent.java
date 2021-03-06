// Copyright (c) Microsoft. All rights reserved.

package com.microsoft.azure.iotsolutions.devicetelemetry.actionsagent;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import com.microsoft.azure.eventprocessorhost.IEventProcessorFactory;
import com.microsoft.azure.iotsolutions.devicetelemetry.actionsagent.eventhub.IEventProcessorHostWrapper;
import com.microsoft.azure.iotsolutions.devicetelemetry.services.runtime.ActionsConfig;
import com.microsoft.azure.iotsolutions.devicetelemetry.services.runtime.IServicesConfig;
import play.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@Singleton
public class Agent implements IAgent {

    private static final Logger.ALogger logger = Logger.of(Agent.class);
    private IServicesConfig serviceConfig;
    private IEventProcessorFactory actionsEventProcessorFactory;
    private IEventProcessorHostWrapper eventProcessorHostWrapper;
    private static final String DEFAULT_CONSUMER_GROUP_NAME = "$Default";

    @Inject
    public Agent(
        IServicesConfig serviceConfig,
        IEventProcessorHostWrapper eventProcessorHostWrapper,
        IEventProcessorFactory actionEventProcessorFactory) throws ExecutionException, InterruptedException {
        this.serviceConfig = serviceConfig;
        this.eventProcessorHostWrapper = eventProcessorHostWrapper;
        this.actionsEventProcessorFactory = actionEventProcessorFactory;
        CompletableFuture.runAsync(() -> this.runAsync());
    }

    public CompletionStage runAsync() {
        this.logger.info("Actions Agent started");
        return setUpEventHubAsync();
    }

    private CompletionStage setUpEventHubAsync() {
        try {
            ActionsConfig actionsConfig = this.serviceConfig.getActionsConfig();
            EventProcessorHost host = this.eventProcessorHostWrapper.createEventProcessorHost(
                actionsConfig.getEventHubName(),
                DEFAULT_CONSUMER_GROUP_NAME,
                actionsConfig.getEventHubConnectionString(),
                actionsConfig.getBlobStorageConnectionString(),
                actionsConfig.getEventHubCheckpointContainerName());
            return this.eventProcessorHostWrapper.registerEventProcessorFactoryAsync(host, this.actionsEventProcessorFactory);
        } catch (Exception e) {
            this.logger.error("Received error setting up event hub. Will not receive information from the eventhub", e);
            throw new CompletionException(e);
        }
    }
}
