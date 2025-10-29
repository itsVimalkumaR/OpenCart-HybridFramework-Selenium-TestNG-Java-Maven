/* /src/test/java/com/opencart/base/BaseTest.java */
package com.opencart.base;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;

import com.opencart.utilities.ConfigReader;
import com.opencart.utilities.ExcelUtils;
import com.opencart.utilities.ExtentManager;
import com.opencart.utilities.ExtentTestManager;
import com.opencart.utilities.Log;
import com.opencart.utilities.TestListener;
import com.opencart.utilities.WaitUtils;
import com.opencart.utilities.WebDriverFactory;

@Listeners(TestListener.class)
public class BaseTest {

	protected WebDriver driver;
	protected SoftAssert softAssert;
	protected ExcelUtils excelUtils;
	protected WaitUtils waitUtils;
	protected ConfigReader config;
	protected Log log;
	public static Logger logger = Logger.getLogger(BaseTest.class);

	public Actions actions;
	public BaseLocators locators;
	public CommonWrapper commonWrapper;

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() {
		PropertyConfigurator.configure(System.getProperty("user.dir") + "/src/test/resources/log4j.properties");
		logger.info("[INFO] Log4j configured successfully.");
		ExtentManager.getInstance();
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() {
		try {
			ExtentTestManager.flushReport();
			logger.info("[INFO] Extent report flushed successfully.");
		} catch (Exception e) {
			logger.error("[ERROR] While flushing Extent report: " + e.getMessage());
		} finally {
			if (driver != null) {
				WebDriverFactory.quitDriver();
			}
		}
	}

	@BeforeClass(alwaysRun = true)
	public void setupClass() throws IOException {
		driver = WebDriverFactory.createDriver(ConfigReader.getBrowser());
		config = new ConfigReader();
		log = new Log();
		softAssert = new SoftAssert();
		actions = new Actions(driver);
		waitUtils = new WaitUtils(driver);

		// Initialize page objects
		locators = new BaseLocators(driver);
		commonWrapper = new CommonWrapper(driver); // This should properly initialize

		try {
			String excelPath = System.getProperty("user.dir") + "/src/test/resources/testdata.xlsx";
			File file = new File(excelPath);
			if (file.exists()) {
				excelUtils = new ExcelUtils(excelPath, "Data");
				logger.info("[INFO] Excel loaded: " + excelPath);
			} else {
				logger.warn("[WARNING] Excel file not found: " + excelPath);
			}
		} catch (Exception e) {
			logger.error("[ERROR] Excel initialization failed: " + e.getMessage());
		}
	}

	@AfterClass(alwaysRun = true)
	public void tearDownClass() {
		if (softAssert != null) {
			softAssert.assertAll();
		}
		if (driver != null) {
			WebDriverFactory.quitDriver();
		}
	}

	// Provide getter for driver
	public WebDriver getDriver() {
		return driver;
	}
}