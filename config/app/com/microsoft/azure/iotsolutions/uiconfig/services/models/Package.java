// Copyright (c) Microsoft. All rights reserved.

package com.microsoft.azure.iotsolutions.uiconfig.services.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

public class Package {
    private String id;
    private String name;
    private PackageType type;
    private PackageConfigType configType;
    private String content;
    private String dateCreated;

    public Package() {
    }

    public Package(String id, String name, PackageType type, PackageConfigType configType, String content) {
        this(id, name, type, configType, content, StringUtils.EMPTY);
    }

    public Package(String id, String name, PackageType type, PackageConfigType configType,
                   String content, String dateCreated) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.configType = configType
        this.content = content;
        this.dateCreated = dateCreated;
    }

    @JsonProperty("Id")
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("Name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Type")
    public PackageType getType() {
        return this.type;
    }

    public void setType(PackageType type) {
        this.type = type;
    }

    @JsonProperty("Config")
    public PackageConfigType getPackageConfig() { return this.configType; }

    public void setPackageConfig(PackageConfigType configType) { this.configType = configType; }

    @JsonProperty("Content")
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty("DateCreated")
    public String getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
