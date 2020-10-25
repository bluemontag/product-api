# Product API

A spring boot application for implementing a Product API

## Compilation

First, compile using maven:

### mvn clean package

This will compile the sources, execute the tests and if they pass, it will and generate the jar file

## Building the Docker image

Then, you can create the docker image from this jar file:

### docker build -t productapi .

## Running the container image

### docker run -p 8080:8080 productapi
