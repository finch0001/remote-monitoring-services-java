// Copyright (c) Microsoft. All rights reserved.

package com.microsoft.azure.iotsolutions.devicetelemetry.webservice.v1.controllers;

import com.google.inject.Inject;
import com.microsoft.azure.iotsolutions.devicetelemetry.services.IAlarmsByRule;
import com.microsoft.azure.iotsolutions.devicetelemetry.webservice.v1.controllers.helpers.DateHelper;
import com.microsoft.azure.iotsolutions.devicetelemetry.webservice.v1.models.AlarmApiModel;
import com.microsoft.azure.iotsolutions.devicetelemetry.webservice.v1.models.AlarmListApiModel;
import play.Logger;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

import static play.libs.Json.toJson;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

// TODO: Review and see if we can either extend the Alarm API or the Rules API.
public class AlarmsByRuleController {
    private static final Logger.ALogger log = Logger.of(AlarmsByRuleController.class);

    private final IAlarmsByRule alarmsByRule;

    @Inject
    public AlarmsByRuleController(IAlarmsByRule alarmsByRule) {
        this.alarmsByRule = alarmsByRule;
    }

    /**
     * Return a list of alarms grouped by the rule from which the alarm is
     * created. The list can be paginated, and filtered by device, period of
     * time, status. The list is sorted chronologically, by default starting
     * from the oldest alarm, and optionally from the most recent.
     *
     * The list can also contain zero alarms and only a count of occurrences,
     * for instance to know how many alarms are generated for each rule.
     *
     * @return List of alarms.
     */
    public Result list(String from, String to, String order, int skip,
                       int limit, String devices) throws Exception {
        // TODO: move this logic to the storage engine, depending on the
        // storage type the limit will be different. 200 is DocumentDb
        // limit for the IN clause.
        String[] deviceIds = new String[0];
        if (devices != null) {
            deviceIds = devices.split(",");
        }
        if (deviceIds.length > 200) {
            log.warn("The client requested too many devices: {}", deviceIds.length);
            return badRequest("The number of devices cannot exceed 200");
        }

        return ok(toJson(new AlarmListApiModel(this.alarmsByRule.getList(DateHelper.parseDate(from), DateHelper.parseDate(to), order, skip, limit, deviceIds))));
    }

    /**
     * @return A list of alarms generated from a specific rule.
     */
    public Result get(String id, String from, String to, String order, int skip,
                      int limit, String devices) throws Exception {
        // TODO: move this logic to the storage engine, depending on the
        // storage type the limit will be different. 200 is DocumentDb
        // limit for the IN clause.
        String[] deviceIds = new String[0];
        if (devices != null) {
            deviceIds = devices.split(",");
        }
        if (deviceIds.length > 200) {
            log.warn("The client requested too many devices: {}", deviceIds.length);
            return badRequest("The number of devices cannot exceed 200");
        }

        return ok(toJson(new AlarmApiModel(this.alarmsByRule.get(id, DateHelper.parseDate(from), DateHelper.parseDate(to), order, skip, limit, deviceIds))));
    }
}