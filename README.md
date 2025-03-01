#  Cucumber REST API Automation Framework

This project is an API automation framework designed to validate the API endpoints and test scenarios using Cucumber BDD and RestAssured.

## Prerequisites

- Java 23
- Maven 3.9.9

## Setup Instructions

1. Clone the repository:
   ```
   git clone https://github.com/rajatsharma111/cucumber-rest-framework.git
   cd cucumber-rest-framework
   ```

2. Build the project:
   ```
   mvn clean install
   ```

3. Run the tests:
   ```
   mvn test
   ```
   OR use the `env` property to specify the environment to run the tests.
   ```
   mvn test -Denv=test
   ```

4. Install the Allure CLI:
   ```
   # macOS
   brew install allure
   ```
   ```
   # Windows (using Scoop)
   scoop install allure
   ```   

5. Generate Allure report:
   ```
   allure generate allure-results -o allure-report --clean
   ```

6. Open the report:
   ```
   allure serve allure-report
   ```

## Configuration

   The framework uses a configuration system based on the environment.
   Environment-specific properties (e.g., `test.properties`, `dev.properties`)
