@REM Generating and running the docker image
@echo off
echo.
echo =====================================================================
echo Cleaning previous compilation (empties /target folder)
echo =====================================================================
echo.
call mvn clean

echo.
echo =====================================================================
echo Running tests and generating Project JAR file...
echo =====================================================================
echo.
call mvn package

echo.
echo =====================================================================
echo Building the Docker Image...
echo =====================================================================
echo.
docker build -t productapi .

echo.
echo =====================================================================
echo Executing container...
echo =====================================================================
echo.

docker run -p 8080:8080 productapi

pause 0