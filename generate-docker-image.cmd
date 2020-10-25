@REM Generating and running the docker image
@echo off
@REM mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

echo.
echo Building the Docker Image...
echo.
docker build -t productapi .

echo.
echo Executing container...
echo.
docker run -p 8080:8080 productapi
