@ECHO off & setlocal enableextensions enabledelayedexpansion

:: Usage:
:: Build the project in the local environment:  scripts\build
:: Build the project inside a Docker container: scripts\build -s
:: Build the project inside a Docker container: scripts\build --in-sandbox

:: strlen("\scripts\") => 9
SET APP_HOME=%~dp0
SET APP_HOME=%APP_HOME:~0,-9%
cd %APP_HOME%

IF "%1"=="-s" GOTO :RunInSandbox
IF "%1"=="--in-sandbox" GOTO :RunInSandbox


:RunLocally

    :: Check dependencies
    java -version > NUL 2>&1
    IF %ERRORLEVEL% NEQ 0 GOTO MISSING_JAVA

    :: Check settings
    call .\scripts\env-vars-check.cmd
    IF %ERRORLEVEL% NEQ 0 GOTO FAIL

    :: Run tests
    call sbt test
    IF %ERRORLEVEL% NEQ 0 GOTO FAIL

    goto :END


:RunInSandbox

    :: Folder where PCS sandboxes cache data. Reuse the same folder to speed up the
    :: sandbox and to save disk space.
    :: Use PCS_CACHE="%APP_HOME%\.cache" to cache inside the project folder
    SET PCS_CACHE="%TMP%\azure\iotpcs\.cache"

    :: Check dependencies
    docker version > NUL 2>&1
    IF %ERRORLEVEL% NEQ 0 GOTO MISSING_DOCKER

    :: Check settings
    call .\scripts\env-vars-check.cmd
    IF %ERRORLEVEL% NEQ 0 GOTO FAIL

    :: Create cache folders to speed up future executions
    mkdir %PCS_CACHE%\sandbox\.ivy2 > NUL 2>&1
    mkdir %PCS_CACHE%\sandbox\.sbt > NUL 2>&1
    echo Note: caching build files in %PCS_CACHE%

    :: Start the sandbox and execute the build script
    docker run -it ^
        -e PCS_KEYVAULT_NAME ^
        -e PCS_AAD_APPID ^
        -e PCS_AAD_APPSECRET ^
        -v %PCS_CACHE%\sandbox\.ivy2:/root/.ivy2 ^
        -v %PCS_CACHE%\sandbox\.sbt:/root/.sbt ^
        -v %APP_HOME%:/opt/code ^
        azureiotpcs/code-builder-java:1.0 /opt/code/scripts/build

    :: Error 125 typically triggers in Windows if the drive is not shared
    IF %ERRORLEVEL% EQU 125 GOTO DOCKER_SHARE
    IF %ERRORLEVEL% NEQ 0 GOTO FAIL

    goto :END


:MISSING_JAVA
    echo ERROR: 'java' command not found.
    echo Install OpenJDK or Oracle JDK and make sure the 'java' command is in the PATH.
    echo OpenJDK installation: http://openjdk.java.net/install
    echo Oracle Java Standard Edition: http://www.oracle.com/technetwork/java/javase/downloads
    exit /B 1

:MISSING_DOCKER
    echo ERROR: 'docker' command not found.
    echo Install Docker and make sure the 'docker' command is in the PATH.
    echo Docker installation: https://www.docker.com/community-edition#/download
    exit /B 1

:DOCKER_SHARE
    echo ERROR: the drive containing the source code cannot be mounted.
    echo Open Docker settings from the tray icon, and fix the settings under 'Shared Drives'.
    exit /B 1

:FAIL
    echo Command failed
    endlocal
    exit /B 1

:END
endlocal
