[![Build][build-badge]][build-url]
[![Issues][issues-badge]][issues-url]
[![Gitter][gitter-badge]][gitter-url]

# Device Telemetry Overview

This service provides a RESTful endpoint for read access to device telemetry,
full CRUD for rules, and read/write for alarms from storage.

## Why?

This microservice was built as part of the
[Azure IoT Remote Monitoring](https://github.com/Azure/azure-iot-pcs-remote-monitoring-java)
project to provide a generic implementation for an end-to-end IoT solution.
More information [here][rm-arch-url].

## Features
* Gets a list of telemetry messages
* Gets a list of alarms
* Gets a single alarm
* Modifies alarm status
* Create/Read/Update/Delete Rules
* Supports alert triggered notifications

## Documentation

* View the API documentation in the [Wiki](https://github.com/Azure/remote-monitoring-services-java/wiki/Telemetry-Api).

# How to use

## Running the service with Docker

You can run the microservice and its dependencies using
[Docker](https://www.docker.com/) with the instructions
[here][run-with-docker-url].

## Running the service locally

## Prerequisites
### 1. Deploy Azure Services
This service has a dependency on the following Azure resources. Follow the instructions for
[Deploy the Azure services](https://docs.microsoft.com/azure/iot-suite/iot-suite-remote-monitoring-deploy-local#deploy-the-azure-services).
* Cosmos DB

### 2. Setup Dependencies

This service depends on the following repository.
1. [Storage Adapter Microservice](https://github.com/Azure/remote-monitoring-services-java/tree/master/storage-adapter)
2. [Auth Microservice](https://github.com/Azure/remote-monitoring-services-java/tree/master/auth)

### 3. In order to run the service, some environment variables need to be created 
at least once. See specific instructions for IDE or command line setup below
for more information. More information on environment variables
[here](#configuration-and-environment-variables).

* `PCS_AAD_APPID` = { Azure service principal id }
* `PCS_AAD_APPSECRET` = { Azure service principal secret }
* `PCS_KEYVAULT_NAME` = { Name of Key Vault resource that stores settings and configuration }

### 3.1 Configurations values used from Key Vault
Some of the configuration needed by the microservice is stored in an instance of Key Vault that was created on initial deployment. The telemetry microservice uses:

  * `authWebServiceUrl` = http://localhost:9001/v1
  * `aadTenantId` = {Azure Active Directory Tenant ID}
    * see: Azure Portal => Azure Active Directory => Properties => Directory ID
  * `aadAppId` = {Azure Active Directory application ID}
    * see: Azure Portal => Azure Active Directory => App Registrations => Your App => Application ID
  * `aadAppSecret` = {application secret}
    * see: Azure Portal => Azure Active Directory => App Registrations => Your App => Settings => Passwords
  * `actionsEventHubName` = {Event hub name}
  * `actionsEventHubConnectionString` = {Endpoint=sb://....servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=...}
    * see: Azure Portal => Your resource group => your event hub namespace => Shared access policies
  * `corsWhitelist` = { CORS whitelisted urls }
  * `documentDBConnectionString ` = {your Azure Cosmos DB connection string}
  * `diagnosticsWebServiceUrl` (optional) = http://localhost:9006/v1
  * `logicAppEndpointUrl` = {Logic App Endpoint}
    * see: Azure Portal => Your resource group => Your Logic App => Logic App Designer => When a Http Request is received => HTTP POST URL
  * `storageAdapterWebServiceUrl ` = http://localhost:9022/v1
  * `storageConnectionString` = {connection string}
    * see: Azure Portal => Your resource group => Your Storage Account => Access keys => Connection String
  * `solutionWebsiteUrl` = {Solution Url}
  * `telemetryStorageType` = "tsi"
    * Allowed values: ["cosmosdb", "tsi"]. Default is "tsi"
  * `tsiDataAccessFQDN` = {Time Series FQDN}
    * see: Azure Portal => Your Resource Group => Time Series Insights Environment => Data Access FQDN
# Running the service in an IDE

## Prerequisites
- Install [IntelliJ IDEA](https://www.jetbrains.com/idea/download)
- Install [SBT](http://www.scala-sbt.org/download.html)
- Install the latest
[Java 8 SDK](http://www.oracle.com/technetwork/java/javase/downloads)

## Running the service with IntelliJ IDEA
IntelliJ IDEA lets you open the application without using a command
prompt, without configuring anything outside of the IDE. The SBT build tool
takes care of downloading appropriate libraries, resolving dependencies and
building the project (more info [here](https://www.playframework.com/documentation/2.6.x/IDE)).

Steps using IntelliJ IDEA, with SBT plugin enabled:

* Make sure the [prerequisites](#prerequisites) are set up.
* "Open" the project with IntelliJ, the IDE should automatically recognize
  the SBT structure. Wait for the IDE to download some dependencies
  (see IntelliJ status bar). This may take a while, hang in there!
* Create a new Run Configuration, of type "SBT Task", with any name.
  * Enter `"run 9004"` (including the double quotes) in Tasks. This ensures that
   the service starts using the TCP port 9004.  If you desire to use a
    different port, feel free to change it.
  * Define the following environment variables:
      * `PCS_AAD_APPID` = { Azure service principal id }
      * `PCS_AAD_APPSECRET` = { Azure service principal secret }
      * `PCS_KEYVAULT_NAME` = { Name of Key Vault resource that stores settings and configuration }
* Either from the toolbar or the Run menu, execute the configuration just
  created, using the Debug command/button
* Test that the service is up and running pointing your browser to
  http://127.0.0.1:9004/v1/status

## Running the service with Eclipse
The integration with Eclipse requires the
[sbteclipse plugin](https://github.com/typesafehub/sbteclipse), already
included, and an initial setup via command line (more info
[here](https://www.playframework.com/documentation/2.6.x/IDE)).

Steps using Eclipse Oxygen ("Eclipse for Java Developers" package):

* Open a console (either Bash or Windows CMD), move into the project folder,
  execute `sbt compile` and then `sbt eclipse`. This generates some files
  required by Eclipse to recognize the project.
* Open Eclipse, and from the Welcome screen "Import" an existing project,
  navigating to the root folder of the project.
* From the console run `sbt -jvm-debug 9999 "run 9004"` to start the project
* Test that the service is up and running pointing your browser to
  http://127.0.0.1:9004/v1/status
* In Eclipse, select "Run -> Debug Configurations" and add a "Remote Java
  Application", using "localhost" and port "9999".
* After saving this configuration, you can click "Debug" to connect to the
  running application.

# Running the service from the command line

1. Make sure the [prerequisites](#prerequisites) are set up.
1. Set the following environment variables in your system.
More information on environment variables [here](#configuration-and-environment-variables).
   * `PCS_AAD_APPID` = { Azure service principal id }
   * `PCS_AAD_APPSECRET` = { Azure service principal secret }
   * `PCS_KEYVAULT_NAME` = { Name of Key Vault resource that stores settings and configuration }

1. Use the scripts in the [scripts](scripts) folder for many frequent tasks:
   * `build`: compile all the projects and run the tests.
   * `compile`: compile all the projects.
   * `run`: compile the projects and run the service. This will prompt for
  elevated privileges in Windows to run the web service.

If you are familiar with [SBT](http://www.scala-sbt.org), you can also use SBT
directly. A copy of SBT is included in the root of the project.

# Project Structure

* **Code** for the application is in **app/com.microsoft.azure.iotsolutions.devicetelemetry/**
    * **WebService** - Java web service exposing REST interface for managing Ruels,
    Alarms, and Messages
    * **Services** - Java project containing business logic for interacting with
    storage service
* **Tests** are in the **test** folder
    * **WebService** - Tests for web services functionality
    * **Service** - Tests for services functionality
* Configuration files and routes are in the **conf** folder
* The **scripts** folder contains build scripts, docker container creation scripts,
   and scripts for running the microservice from the command line


## Updating the Docker image

The `scripts` folder includes a [docker](scripts/docker) subfolder with the files
required to package the service into a Docker image:

* `build`: build a Docker container and store the image in the local registry.
* `run`: run the Docker container from the image stored in the local registry.

You might notice that there is no `Dockerfile`. All Docker settings are
defined in [build.sbt](build.sbt).

```scala
dockerRepository := Some("azureiotpcs")
dockerAlias := DockerAlias(dockerRepository.value, None, packageName.value + "-java", Some((version in Docker).value))
dockerBaseImage := "toketi/openjdk-8-jre-alpine-bash"
dockerUpdateLatest := false
dockerBuildOptions ++= Seq("--compress", "--label", "Tags=azure,iot,pcs,telemetry,Java")
dockerEntrypoint := Seq("bin/telemetry")
```

The package logic is executed via
[sbt-native-packager](https://github.com/sbt/sbt-native-packager),
installed in [plugins.sbt](project/plugins.sbt).

# Configuration and Environment variables

The service configuration is stored using Akka's
[HOCON](https://github.com/typesafehub/config/blob/master/HOCON.md)
format in [application.conf](conf/application.conf).

The HOCON format is a human readable format, very close to JSON, with some
useful features:

* Ability to write comments
* Support for substitutions, e.g. referencing environment variables
* Supports JSON notation

The configuration file in the microservice references some environment
variables that need to created at least once. Depending on your OS and
the IDE, there are typically set in 3 different ways:
* Environment variables as is the case with ${PCS_AAD_APPID}. This is typically only done with the 3 variables described above as these are needed to access Key Vault. More details about setting environment variables are located below.
* Key Vault: A number of the settings in this file will be blank as they are  expecting to get their value from a Key Vault secret of the same name.
* Direct Value: For some values that aren't typically changed or for local development you can set the value directly in the file.
* IntelliJ IDEA: env. vars, esp. key-vault related, can be set in each Run Configuration, see
  https://www.jetbrains.com/help/idea/run-debug-configuration-application.html

To save the environment variables globally and make them persistent, please follow these pages:
* https://superuser.com/questions/949560/
* https://stackoverflow.com/questions/13046624/how-to-permanently-export-a-variable-in-linux
* https://stackoverflow.com/questions/135688/setting-environment-variables-in-os-x
* https://help.ubuntu.com/community/EnvironmentVariables

# Contributing to the solution
Please follow our [contribution guidelines](CONTRIBUTING.md).  We love PRs too.

# Feedback
Please enter issues, bugs, or suggestions as
[GitHub Issues](https://github.com/Azure/device-telemetry-java/issues).

# License

Copyright (c) Microsoft Corporation. All rights reserved.
Licensed under the [MIT](LICENSE) License.

[build-badge]: https://solutionaccelerators.visualstudio.com/RemoteMonitoring/_apis/build/status/Consolidated%20Repo%20-%20Java
[build-url]: https://travis-ci.org/Azure/device-telemetry-java
[issues-badge]: https://img.shields.io/github/issues/azure/device-telemetry-java.svg
[issues-url]: https://github.com/Azure/remote-monitoring-services-java/issues
[gitter-badge]: https://img.shields.io/gitter/room/azure/iot-solutions.js.svg
[gitter-url]: https://gitter.im/azure/iot-solutions
[run-with-docker-url]:https://docs.microsoft.com/azure/iot-suite/iot-suite-remote-monitoring-deploy-local#run-the-microservices-in-docker
[rm-arch-url]:https://docs.microsoft.com/azure/iot-suite/iot-suite-remote-monitoring-sample-walkthrough
