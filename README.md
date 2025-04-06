```md
# 🚀 UI Selenium WebDriver Automation Framework

## 📖 Overview

This framework provides a robust **end-to-end UI automation testing solution** using:

- **Selenium WebDriver**  
- **TestNG**  
- **Maven**  
- **Cucumber (integrated with TestNG)**  
- **Jenkins (CI/CD ready)**  

It follows **industry best practices** and supports:

- ✅ Data-driven testing (JSON, Excel, HashMap)  
- ✅ Page Object Model (POM) and Page Factory  
- ✅ Parallel test execution  
- ✅ Retry mechanisms & TestNG listeners  
- ✅ Comprehensive reporting (Extent, Cucumber, TestNG)  
- ✅ Global configuration management  
- ✅ Jenkins pipeline integration for CI/CD  

---

## 🛠️ Technologies Used

| Tool             | Purpose                             |
|------------------|--------------------------------------|
| Selenium WebDriver | UI automation                      |
| Java             | Programming language                 |
| Maven            | Build and dependency management      |
| TestNG           | Test runner                          |
| Cucumber         | BDD framework                        |
| Jenkins          | Continuous Integration               |
| Apache POI       | Excel-based data handling            |
| Extent Reports   | Detailed test execution reports      |
| JSON             | Parameterized test data              |

---

## 📌 Features

### ✅ TestNG + Selenium
- Annotations: `@Test`, `@BeforeSuite`, `@AfterSuite`
- Parallel Execution
- Data-driven testing using JSON & Excel
- Test grouping & dependency (`groups`, `dependsOnMethods`)
- Retry logic via `IRetryAnalyzer`
- TestNG Listeners: `ITestListener`
- Multiple test suites via XML

### ✅ Cucumber BDD Integration
- Feature files + step definitions
- Tag-based selective execution
- Data-driven scenario parameterization
- Executed using Cucumber TestNG Runner

### ✅ Reporting
- **Extent Reports**: test logs, screenshots, results  
- **Cucumber Reports**: step-wise scenario reports  
- **TestNG Reports**: default XML + HTML output  

### ✅ Page Object Model (POM)
- Uses `@FindBy` with PageFactory
- AbstractComponent for reusable actions
- Clean, modular test structure

### ✅ Data-Driven Testing
- JSON: scenario inputs from `.json`
- Excel: read from `.xlsx` via Apache POI
- TestNG `@DataProvider` for parametrized tests

### ✅ CI/CD & Parallel Execution
- Parallel execution via TestNG XML
- Jenkins-compatible for CI pipeline automation
- Maven CLI support:
  ```sh
  mvn clean test
  ```

---

## 🧱 Project Structure

<pre>
selenium-framework-automation-java/
├── reports/              # Auto-generated reports and screenshots
├── src/                  # Main codebase (Java, POM, Utilities)
├── testSuites/           # TestNG XML suite files
├── .gitignore
├── README.md
├── pom.xml
</pre>

---

## 🔧 Setup Instructions

### ✅ Prerequisites
- Java 21+
- Maven 3.9+
- ChromeDriver / WebDriver Manager
- TestNG
- Jenkins (optional for CI)

### ✅ Clone the Repository
```sh
git clone https://github.com/gurjarvikram/selenium-framework-automation-java.git
cd selenium-framework-automation-java
```

### ✅ Install Dependencies
```sh
mvn clean install
```

### ✅ Run Tests

**TestNG Default Execution**
```sh
mvn test
```

**Cucumber Feature Execution**
```sh
mvn test -Dcucumber.options="src/test/resources/features"
```

**Parallel Execution**
```sh
mvn test -Dgroups=parallel
```

---

## 🤝 Contributing

We welcome contributions!  
1. Fork the repo  
2. Create a feature branch  
3. Submit a pull request 🚀

---

## 📜 License

This project is licensed under the [MIT License](LICENSE).

---

## ⭐ Support

If you like this framework, don’t forget to **star** 🌟 the repository and share it with others!
