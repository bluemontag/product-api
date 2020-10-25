@REM Generating JAR file
@echo off
echo.
echo Generating Java Project JAR file with Maven...
echo.
SET MAVEN_CMD=mvn clean package
%MAVEN_CMD%