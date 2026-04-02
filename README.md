# CK Demo Framework

Java 21, RestAssured, JUnit 5, Spring for IoC.

Overenginnered for demo sake, to show how it could be scalled-up in real life cases. 
Shows how multiple apps could be added on example of ckappalpha, ckappbeta - mocked APIs.
Configuration / test data per environments kept in .properties

Each app response is mapped to a bussiness model, where custom logic can be handled.

Parallel execution. World object added for the sake of it - not really needed with current tests.
By default, requests will be made with particular API config defaults,
but RequestBuilder can be used to make custom calls. 

Simple test example - com/api/ckappbeta/UserApiTest.java
Did not add any crazy custom assertions - should be easy when needed.

Note - one test actually fails due to mocked API data randomization (and that is OK).

## Run tests

```bash
# All tests (default: UAT)
mvn test

# By suite
mvn test -Dgroups=smoke
mvn test -Dgroups=regression

# By app
mvn test -Dtest="com.api.ckappalpha.**"
mvn test -Dtest="com.api.ckappbeta.**"

# Suite + app
mvn test -Dgroups=smoke -Dtest="com.api.ckappalpha.**"

# Switch environment
mvn test -Denv=preprod
mvn test -Denv=preprod -Dgroups=smoke
```

## Test report

```bash
mvn surefire-report:report
# opens at target/reports/surefire-report.html
```
