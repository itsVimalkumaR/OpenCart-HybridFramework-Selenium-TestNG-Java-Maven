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
import com.opencart.utilities.AllureTestListener;
import com.opencart.utilities.WaitUtils;
import com.opencart.utilities.WebDriverFactory;

@Listeners({ TestListener.class, AllureTestListener.class })
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
		try {
			// Configure Log4j
			String log4jPath = System.getProperty("user.dir") + "/src/test/resources/log4j.properties";

			PropertyConfigurator.configure(log4jPath);
			logger.info("[INFO] Log4j configured successfully from: " + log4jPath);

			// Initialize Extent Reports
			ExtentManager.getInstance();
			logger.info("[INFO] Extent Reports initialized successfully");
		} catch (Exception e) {
			logger.error("[ERROR] During suite setup: " + e.getMessage(), e);
			throw new RuntimeException("Suite setup failed", e);
		}
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() {
		try {
			ExtentTestManager.flushReport();
			logger.info("[INFO] Extent report flushed successfully.");
		} catch (Exception e) {
			logger.error("[ERROR] While flushing Extent report: " + e.getMessage(), e);
		} finally {
			if (driver != null) {
				WebDriverFactory.quitDriver();
			}
		}
	}

	@BeforeClass(alwaysRun = true)
	public void setupClass() throws IOException {
		try {
			logger.info("[INFO] Starting test class setup...");

			// Initialize WebDriver
			driver = WebDriverFactory.createDriver(ConfigReader.getBrowser());
			logger.info("[INFO] WebDriver initialized successfully");

			// Initialize utilities
			softAssert = new SoftAssert();
			actions = new Actions(driver);
			waitUtils = new WaitUtils(driver);
			logger.info("[INFO] Utilities initilaized successfully");

			// Initialize page objects
			locators = new BaseLocators(driver);
			commonWrapper = new CommonWrapper(driver);
//			commonWrapper = new CommonWrapper(driver, waitUtils, locators);
			logger.info("[INFO] Page objects initialized successfully");

			// Other utilities
			initializeExcelUtils();
			config = new ConfigReader();
			log = new Log();

			logger.info("[INFO] Test class setup completed successfully");

		} catch (Exception e) {
			logger.error("[ERROR] During test class setup: " + e.getMessage(), e);
			// Clean up resources if setup fails
			if (driver != null) {
				WebDriverFactory.quitDriver();
			}
			throw new RuntimeException("Test class setup failed", e);
		}
	}

	/**
	 * Initialize Excel utilities with proper error handling
	 */
	private void initializeExcelUtils() {

		try {
			String excelPath = System.getProperty("user.dir") + "/src/test/resources/testdata.xlsx";
			File file = new File(excelPath);

			if (file.exists() && !file.isDirectory()) {
				excelUtils = new ExcelUtils(excelPath, "Data");
				logger.info("[INFO] Excel utilities initialized: " + excelPath);

				// Test data loading
				int rowCount = excelUtils.getRowCount();
				logger.info("[INFO] Excel sheet conatins " + rowCount + " rows of test data");
			} else {
				logger.warn("[WARNING] Excel file not found or is directory: " + excelPath);
				excelUtils = null;
			}
		} catch (Exception e) {
			logger.error("[ERROR] Excel utilities initialization failed: " + e.getMessage(), e);
			excelUtils = null;
		}
	}

	@AfterClass(alwaysRun = true)
	public void tearDownClass() {
		try {
			logger.info("[INFO] Starting test class teardown...");

			// Assert all soft assertions
			if (softAssert != null) {
				softAssert.assertAll();
				logger.info("[INFO] Soft assertions validated");
			}

			// Close WebDriver
			if (driver != null) {
				WebDriverFactory.quitDriver();
				logger.info("[INFO] WebDriver closed successfiully");
			}

			logger.info("[INFO] Test class teardown completed successfully");
		} catch (Exception e) {
			logger.error("[ERROR] During test class teardown: " + e.getMessage(), e);
		}
	}

// ========== GETTER METHODS ==========

	/**
	 * Get WebDriver instance
	 */
	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * Get SoftAssert instance
	 */
	public SoftAssert getSoftAssert() {
		return softAssert;
	}

	/**
	 * Get WaitUtils instance
	 */
	public WaitUtils getWaitUtils() {
		return waitUtils;
	}

	/**
	 * Get CommonWrapper instance
	 */
	public CommonWrapper getCommonWrapper() {
		return commonWrapper;
	}

	/**
	 * Get BaseLocators instance
	 */
	public BaseLocators getLocators() {
		return locators;
	}

	/**
	 * Get ExcelUtils instance
	 */
	public ExcelUtils getExcelUtils() {
		return excelUtils;
	}

	/**
	 * Get base URL from configuration
	 */
	public String getBaseUrl() {
		return ConfigReader.getUrl();
	}

	/**
	 * Check if Excel data is available
	 */
	public boolean isExcelDataAvailable() {
		return excelUtils != null;
	}
}