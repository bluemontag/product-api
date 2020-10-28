# The TextPlus Product API

This is a spring boot application that implements a Product API.

## Compilation

To compile the project using maven, you can run the script "generate-jar.cmd" on Windows. This will trigger the following maven command:

### mvn clean package

This will compile the sources, execute the tests and if they pass, it will and generate the jar file.

## Running a local environment

Alternatively, you can run the script "compile-and-run-container.cmd", that will compile the project (as in the first script), build the Docker image, and then will run the container in a local environment (using the port 8080).

Remember to have Docker installed and visible in the path when running this script.

As a last step, the script will open a browser pointing to the main endpoint of each API, showing that everything started OK.

## Open the Main Page of the API

There is a script called "open-api-in-browser.cmd", that will open your default browser with the main endpoind of the API, to test that the API is listening to the port 8080.

Alternatively, you can open your browser and enter the address manually:

### http://localhost:8080/productsapi/

and

### http://localhost:8080/ordersapi/

In each case, you should see a welcome message to indicate that the Spring Boot application and the container have started without any errors.

## Test the API

To test the application, you can install Postman, and load/import the collection "ProductAPI.postman_collection.json" included in this project. It contains pre loaded data and parameters ready to make HTTP calls to the local API.

