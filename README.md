# OpenCart Test Automation Framework

![Java](https://img.shields.io/badge/Java-17-blue)
![TestNG](https://img.shields.io/badge/TestNG-7.6-red)
![Selenium](https://img.shields.io/badge/Selenium-4.0-green)
![Maven](https://img.shields.io/badge/Maven-3.8-orange)
![License](https://img.shields.io/badge/License-MIT-yellow)
![DataDriven](https://img.shields.io/badge/Data-Driven-ff69b4)
![Hybrid](https://img.shields.io/badge/Framework-Hybrid-blueviolet)

A **robust, maintainable automation framework** for testing the OpenCart e-commerce platform using **Selenium WebDriver, TestNG, and Java** with **Page Object Model (POM)** design.  
Supports **Hybrid + Data-driven approach** for flexible and reusable test scripts.

---

## Project Details

- **Framework**: Selenium WebDriver + TestNG + Java  
- **Design Pattern**: Page Object Model (POM)  
- **Framework Type**: Hybrid (Keyword + Data-driven approach)  
- **Reporting**: TestNG reports + Screenshots on failures  
- **Data Management**: Excel (`testdata.xlsx`) and Properties (`config.properties`)  
- **Utilities**:  
  - `ExcelUtils.java` - Reads/writes test data from Excel  
  - `WaitUtils.java` - Custom waits for dynamic elements  
  - `ScreenshotUtils.java` - Captures screenshots on test failure  

---

## Setup Guide

### Prerequisites

1. **Java JDK 17** ([Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))  
2. **Maven 3.8+** ([Download](https://maven.apache.org/download.cgi))  
3. **IDE**: IntelliJ IDEA or Eclipse (with TestNG plugin installed)  
4. **Browser drivers**: ChromeDriver / GeckoDriver (matching your browser version)  

### Installation
1. Clone the repository:

```bash
git clone https://github.com/itsVimalkumaR/OpenCart-HybridFramework-Selenium-TestNG-Java-Maven.git
```
   
2. Import the project as a **Maven project** in your IDE.

3. Resolve Maven dependencies:

```bash
mvn clean install
```

---

## Configuration

### Environment Setup:

Edit `src/main/resources/config.properties`:

```properties
browser=chrome
baseUrl=https://opencart.abstracta.us
implicitWait=10
```

### Test Data:

Update test data in `src/main/resources/testdata.xlsx`.

---

## Running Tests

### Run via TestNG XML

Execute `testing.xml` from your IDE: **Right-click → Run**.

OR 

**Run via Maven**:

```bash
mvn test -DtestngXml=testing.xml
```

**Run Specific Test Class**

```bash
mvn test -Dtest=LoginTest
```

**Reports**

- **TestNG reports**: `target/surefire-reports/index.html`

- **Screenshots**: `target/screenshots/` (on test failures)

---

## Project Structure

```text
OpenCart-HybridFramework-Selenium-TestNG-Java-Maven/
├── src/
│   ├── main/java/
│   │   ├── com.opencart.drivers/       # Browser drivers setup
│   │   ├── com.opencart.pages/         # Page classes (POM)
│   │   ├── com.opencart.utils/         # Reusable utilities (Excel, Waits, Screenshots)
│   │   └── com.opencart.resources/     # Configs and constants
│   ├── main/resources/                 # Properties & Excel files
│   └── test/java/com.opencart.tests/   # Test classes
├── target/                             # Test reports and screenshots
└── pom.xml                             # Maven dependencies
```

---

## Best Practices

- Add new **page classes** under `com.opencart.pages`.

- Store locators in `Constants.java` for maintainability.

- Use `WaitUtils.java` for dynamic waits instead of `Thread.sleep()`.

- Keep test data separate from scripts in `testdata.xlsx`.

- Use Hybrid approach: define keywords and data in Excel to reduce hard-coded logic.

---

## License

This project is licensed under the **MIT License**. See `LICENSE` for details.

---

## Contact

For questions or contributions, contact: vimalkumarm523@gmail.com

---

Ready to use! 🚀
