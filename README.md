# UI Selenium WebDriver Automation Framework

## Overview
This framework is designed for end-to-end UI automation testing using **Selenium WebDriver, TestNG, and Maven**, following industry best practices. It enables:
- **Data-driven testing**
- **Abstract components**
- **Base test implementation**
- **Retry mechanism**
- **Listeners**
- **Extent reporting**
- **Page Object Model (POM) and Page Factory**
- **Global properties management**
- **Dependency and build management using Maven**
- **Parallel execution**
- **Multiple test suites support**
- **Seamless integration into Jenkins CI/CD pipelines for continuous testing**

## Technologies Used
- **Selenium WebDriver** – For UI testing and verification
- **Java** – Primary programming language
- **TestNG** – For test execution, assertions, and reporting
- **Maven** – Manages dependencies and builds
- **Jenkins** – Continuous Integration (CI/CD) execution
- **Test Data** – Supports multiple test data sets including Excel, JSON, simple object arrays, and HashMap-based key-value pairs for data-driven testing.
- **Extent Reporting** – Generates comprehensive test execution reports

## Key Features

### ✅ End-to-End UI Testing
- Comprehensive **E2E UI automation testing** for an **e-commerce purchase order** complete journey.
- Covers **cart page, checkout, order placement, and order verification**.
- Validates **error handling** using **TestNG assertions**.

### ✅ Data-Driven Testing 
- Supports DataProvider method retrieves **JSON-driven test data** for multiple datasets.
- Utilized **DataProvider test data** where each test data element is a HashMap containing key-value pairs for email, password, and product.
- Alternative approach using a simple Object array for test data.
- **Apache POI** for Excel-driven data-driven testing - Specify the absolute path to the Excel file containing the test data and adjust this path according to the location of your `testData.xlsx` file.

### ✅ Multiple Test Suites
- Implements multiple test suites such as:
  - **PurchaseTest**
  - **Regression Suite**
  - **ErrorValidationTests**

### ✅ Parallel Execution
- Enables **parallel test execution** for faster test runs.

### ✅ Modular & Scalable Design
This framework follows a **structured and reusable** test automation approach:
- **Abstract Component** – Represents API request/response payloads.
- **POM (Page Object Model)** – Implements the POM design pattern and utilizes the **DRY (Don't Repeat Yourself) principle**.
- **PageFactory** – Uses the **Page Factory mechanism** for managing locators.
- **Global Properties** – Centralized configuration management.

### ✅ Base Test Implementation
- Implements a **Base Test class** that is **reusable** and can be **extended** in other test files.

### ✅ Retry Mechanism
- Ensures test case **re-execution** on failures.

### ✅ TestNG Listeners
- Implements **TestNG listeners** for test monitoring and reporting.

### ✅ Extent Reporting
- Generates **detailed Extent reports** to track:
  - Test execution results
  - Assertions
  - Failures
- Supports **logging** and **debugging of UI responses**.

### ✅ Continuous Integration (CI/CD) Ready
- **Seamless Jenkins integration** for automated test execution.
- Supports **Maven-based test execution** with CI/CD pipelines.
- Generates **automated reports** and logs after each test run.

## Contributing
Contributions are welcome! Please submit a **Pull Request (PR)** or open an **issue** for discussions.

## License
This project is open-source software licensed under the **MIT License**.

