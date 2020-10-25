@REM Generating JAR file
@echo off
echo.
echo =====================================================================
echo Generating Java Project JAR file with Maven...
echo =====================================================================
echo.
call mvn clean
call mvn package