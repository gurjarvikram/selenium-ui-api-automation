```md
# 🚀 UI Selenium WebDriver Automation Framework

## 📖 Overview
This framework provides a robust **end-to-end UI automation testing solution** using:
✅ **Selenium WebDriver**  
✅ **TestNG**  
✅ **Maven**  
✅ **Cucumber (integrated with TestNG)**  
✅ **Jenkins (CI/CD ready)**  

It follows **industry best practices** and supports:
- **Data-driven testing** (JSON, Excel, HashMap)
- **Page Object Model (POM) and Page Factory**
- **Parallel test execution**
- **Retry mechanisms & TestNG listeners**
- **Comprehensive reporting** (Extent Reports, Cucumber Reports, TestNG Reports)
- **Global configuration management**
- **Seamless Jenkins CI/CD pipeline integration**

---

## 🛠️ Technologies Used
| Tool            | Purpose |
|----------------|---------|
| **Selenium WebDriver** | UI test automation |
| **Java** | Primary programming language |
| **TestNG** | Test execution, assertions, and reporting |
| **Maven** | Dependency and build management |
| **Cucumber** | BDD framework integrated with TestNG |
| **Jenkins** | Continuous Integration (CI/CD) |
| **Apache POI** | Excel-based test data handling |
| **Extent Reports** | Detailed test execution reports |
| **JSON Test Data** | Data-driven testing |

---

## 📌 Features

### ✅ **TestNG + Selenium**
- **Annotations** (`@Test`, `@BeforeSuite`, `@AfterSuite`, etc.)
- **Parallel Execution** – Speeds up execution.
- **Data-Driven Testing** – Supports JSON, Excel, and HashMap test data.
- **Test Grouping & Dependency Management** – Organize test execution with `groups` and `dependsOnMethods`.
- **Retry Mechanism** – Uses `IRetryAnalyzer` for failed tests.
- **Assertions & Listeners** – Implements `Assert` and `ITestListener` for reporting.
- **Multiple Test Suites** – Configurable via **TestNG XML**.

### ✅ **Cucumber BDD Integration**
- **Feature Files & Step Definitions** – Implements **Cucumber BDD** with **Selenium WebDriver**.
- **Cucumber Tags & Parameterization** – Supports **tag-based execution** and **data-driven testing**.
- **Cucumber TestNG Runner** – Executes Cucumber feature files with TestNG.

### ✅ **Advanced Reporting**
- **Extent Reports** – Tracks:
  - Test execution results
  - Assertions & failures
  - Debug logs & screenshots
- **Cucumber Reports** – Generates step-by-step execution results.
- **TestNG Reports** – Standard XML & HTML reports.

### ✅ **Page Object Model (POM) & Abstract Components**
- Implements **POM design pattern** with `@FindBy` annotations.
- Uses **Page Factory for better element handling**.
- **Abstract Component** – Provides reusable UI interaction methods.

### ✅ **Data-Driven Testing**
- **JSON-based test data** – Reads dynamic test sets.
- **Excel-based test data** – Uses **Apache POI** for reading spreadsheets.
- **TestNG `@DataProvider`** – Handles parameterized test execution.

### ✅ **Parallel Execution & CI/CD Integration**
- **Parallel Test Execution** – Configurable via TestNG XML.
- **Jenkins Integration** – Executes tests in **CI/CD pipelines**.
- **Maven Integration** – Run tests via command line:
  ```sh
  mvn clean test
  ```

---

## 🔧 **Project Setup**
### **1️⃣ Prerequisites**
Ensure you have the following installed:
- **Java 21+**
- **Maven 3.9+**
- **Selenium WebDriver**
- **TestNG**
- **Cucumber**
- **Jenkins (optional for CI/CD execution)**

### **2️⃣ Clone the Repository**
```sh
git clone https://github.com/gurjarvikram/selenium-framework-automation-java.git
cd selenium-framework-automation-java
```

### **3️⃣ Install Dependencies**
```sh
mvn clean install
```

### **4️⃣ Run Tests**
#### **TestNG Execution**
```sh
mvn test
```
#### **Cucumber Test Execution**
```sh
mvn test -Dcucumber.options="src/test/resources/features"
```
#### **Parallel Execution**
```sh
mvn test -Dgroups=parallel
```

---

## 🤝 **Contributing**
We welcome contributions!  
- **Fork the repo** and create a feature branch.
- **Submit a Pull Request (PR)** for review.

---

## 📜 **License**
This project is licensed under the **MIT License**.

---
