# The TextPlus Product API

A spring boot application for implementing a Product API

## Compilation

First, to compile the project using maven, you can run the script "generate-jar.cmd" on Windows. This will trigger the following maven command:

### mvn clean package

This will compile the sources, execute the tests and if they pass, it will and generate the jar file.

## Building the Docker image

Once you have compiled the project with the previous command, you can run the second script "compile-and-run-container.cmd", that will build the Docker image, and will run the container in a local environment (using the port 8080).

## Testing the API

You can open your browser and enter the address: 

### http://localhost:8080/productsapi/

or 

### http://localhost:8080/ordersapi/

In each case, you should see a welcome message to indicate that the Spring Boot application and the container have started without any errors.

