## File converter

This sample application takes pipe (|) separated file as input and produces JSON file as output.
The REST endpoint is `/api/file/upload`. 
Please check the `How to run` section for examples.

### Notes
* The wiremock folder contains a standalone wiremock sever. The requests are mapped to the following IPs:
   * `24.48.01` : is an example of a non-blocked IP
   * `40.1.0.21`: blocked country, in this case it is **China**
   * `50.1.0.21`: blocked ISP, in this case it is **AWS**

The list of blocked countries or ISPs can be modified in the **application.yml** file under project `resources` directory.

### How to run

The easiest way would be to import the project in any IDE: 

* Clone the repository
* Run the wiremock server `java -jar wiremock-standalone-3.4.1.jar --port 8029` (or in any convenient port)
* Run `mvn clean install`
* Run the main method in the `FileConverterApplication` class

Here are few sample `cUrl` request which I have tested:
* **200 (success):** `curl --header "X-Forwarded-For: 24.40.0.1" -v -F  file="@./test.txt" http://localhost:8080/api/file/upload -o OutcomeFile.json`
* **200 (success):** `curl -v -F  file="@./test.txt" http://localhost:8080/api/file/upload -o OutcomeFile.json`
* **403 (forbidden):** `curl --header "X-Forwarded-For: 40.1.0.21" -v -F  file="@./test.txt" http://localhost:8080/api/file/upload -o OutcomeFile.json`
* **403 (forbidden):** `curl --header "X-Forwarded-For: 50.1.0.21" -v -F  file="@./test.txt" http://localhost:8080/api/file/upload -o OutcomeFile.json`

### Unit and integration tests

Spring integration test for the controller is available in the `FileUploaderControllerIntegrationTest` class.
Rest of the test are unit tests


### Design consideration

* The `application.yml` allows to set runtime environment (dev or non-dev). Based on the environment the wiremock enabled bean is created
* There are few other settings including max file size, list of blocked countries, ISPs etc.
* `IPFilter` class is responsible initiate the IP validation process.
* `CsvEntryVerifier` class is responsible for validating each line of data of the input file.
* There is a `ConverterFactory` class for future enhancement.
* There is an `AuditController` which exposed `audit` endpoint to get the list of audit entries


### Improvements

* The `CsvEntryVerifier` needs more attention. I haven't thought much about the `name` field in case it contains malicious code.
* Haven't added a default error handler.
* I would prefer to have more tests.




 


