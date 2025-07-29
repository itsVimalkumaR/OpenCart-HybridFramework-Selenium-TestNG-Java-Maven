# OpenCart Test Automation Framework

![Java](https://img.shields.io/badge/Java-17-blue)
![TestNG](https://img.shields.io/badge/TestNG-7.6-red)
![Selenium](https://img.shields.io/badge/Selenium-4.0-green)
![Maven](https://img.shields.io/badge/Maven-3.8-orange)
![License](https://img.shields.io/badge/License-MIT-yellow)

A robust automation framework for testing OpenCart e-commerce platform using Selenium WebDriver, TestNG, and Java with Page Object Model (POM) design.

---

## 📌 Project Details
- **Framework**: Selenium WebDriver + TestNG (Java)
- **Design Pattern**: Page Object Model (POM)
- **Reporting**: TestNG default reports + Screenshots on failure
- **Data Management**: Excel (`testdata.xlsx`) and Properties files (`config.properties`)
- **Utilities**: 
  - `ExcelUtils.java` - Excel data reader
  - `WaitUtils.java` - Custom waits
  - `ScreenshotUtils.java` - Failure screenshots

---

## 🛠 Setup Guide

### Prerequisites
1. **Java JDK 17** ([Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))
2. **Maven 3.8+** ([Download](https://maven.apache.org/download.cgi))
3. **IDE** (IntelliJ/Eclipse with TestNG plugin)

### Installation
1. Clone the repository:
   ```bash
   git clone [your-repo-url.git]
2. Import as a Maven project in your IDE.

3. Resolve dependencies:

bash
mvn clean install
⚙ Configuration
Environment Setup:

Edit src/main/resources/config.properties:

properties
browser=chrome
baseUrl=https://opencart.abstracta.us
implicitWait=10
Test Data:

Update src/main/resources/testdata.xlsx with your test cases.

🚀 Running Tests
Run via TestNG XML
Execute testing.xml in your IDE (Right-click → Run).

OR use Maven:

bash
mvn test -DtestngXml=testing.xml
Run Specific Test Class
bash
mvn test -Dtest=LoginTest
Reports
TestNG reports: target/surefire-reports/index.html

Screenshots: target/screenshots/ (on test failures)

📂 Project Structure
text
automationtest/
├── src/
│   ├── main/java/
│   │   ├── com.opencart.drivers/       # Browser drivers
│   │   ├── com.opencart.pages/         # Page classes (POM)
│   │   ├── com.opencart.utils/         # Reusable utilities
│   │   └── com.opencart.resources/     # Configs/constants
│   ├── main/resources/                 # Properties/Excel files
│   └── test/java/com.opencart.tests/   # Test classes
├── target/                             # Reports/screenshots
└── pom.xml                             # Maven dependencies
💡 Best Practices
Add new page classes under com.opencart.pages.

Store locators in Constants.java.

Use WaitUtils.java for dynamic waits instead of Thread.sleep().

📜 License
This project is licensed under the MIT License. See LICENSE for details.

📧 Contact
For questions or contributions, contact: your-email@example.com

text

### Key Enhancements:
1. Added **MIT License badge** and section.
2. Fixed broken Java JDK link (removed duplicate `https`).
3. Improved **contact section** for contributions.
4. Consistent emoji usage for better readability.

Simply:
1. Create a `LICENSE` file (content provided in previous message).
2. Replace `[your-repo-url.git]` and `[your-email@example.com]` with your details.  

Ready to use! 🚀
