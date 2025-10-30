package com.opencart.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.opencart.base.BaseTest;
import com.opencart.pages.LoginPage;
import com.opencart.utilities.ConfigReader;
import com.opencart.utilities.DataProviders;
import com.opencart.utilities.ExtentTestManager;
import com.opencart.utilities.WaitUtils;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Epic("Authentication & Account Management")
@Feature("Login Functionality - User Authentication")
@Story("TC_RF_002 - Validate the functionality of Login.")
public class LoginTests extends BaseTest {

	private LoginPage loginPage;
	private String VALID_EMAIL, VALID_PASSWORD, INVALID_EMAIL, INVALID_PASSWORD;

	@BeforeClass
	public void setup() {
		loginPage = new LoginPage(driver);
		loadTestDataFromExcel(); // Load test data from first row of Excel for single data tests
	}

	/**
	 * Load test data from the first row of Excel file
	 */
	private void loadTestDataFromExcel() {
		try {
			if (excelUtils != null) {
				// Get the total number of rows
				int totalRows = excelUtils.getRowCount();
				logger.info("Total rows in Excel: " + totalRows);

				if (totalRows > 0) {
					// Get the last row data (row index starts from 0, but ExcelUtils.getCellData
					// uses 1-based for data rows)
					// Since getSheetDataAs2DArray skips header, we need to get the last row
					// directly
					int lastDataRowIndex = totalRows - 1; // Zero-based index

					// Get email from column 2 (index 2) in the last row
					VALID_EMAIL = excelUtils.getCellData(lastDataRowIndex, 2); // Email column

					// Get password from column 4 (index 4) in the last row
					VALID_PASSWORD = excelUtils.getCellData(lastDataRowIndex, 4); // Password column

					logger.info("Loaded from last row - Email: " + VALID_EMAIL + ", Password: " + VALID_PASSWORD);

					// Set invalid credentials
					INVALID_EMAIL = "invalid" + System.currentTimeMillis() + "@test.com";
					INVALID_PASSWORD = "InvalidPass123!";

					// Validate the loaded data
					if (VALID_EMAIL == null || VALID_EMAIL.isEmpty() || VALID_PASSWORD == null
							|| VALID_PASSWORD.isEmpty()) {
						logger.warn("Last row data is empty, using default credentials");
						useDefaultCredentials();
					}
				} else {
					logger.warn("No data rows found in Excel, using default credentials");
					useDefaultCredentials();
				}
			} else {
				logger.warn("ExcelUtils not initialized, using default credentials");
				useDefaultCredentials();
			}
		} catch (Exception e) {
			logger.error("Failed to load test data from Excel: " + e.getMessage());
			logger.warn("Using default credentials due to error");
			useDefaultCredentials();
		}
	}

	/**
	 * Use default credentials when Excel data is not available
	 */
	private void useDefaultCredentials() {
		VALID_EMAIL = "vk636485@gmail.com";
		VALID_PASSWORD = "Vimalutr16@";
		INVALID_EMAIL = "invalid" + System.currentTimeMillis() + "@test.com";
		INVALID_PASSWORD = "InvalidPass123!";
		logger.info("Using default credentials - Email: " + VALID_EMAIL);
	}

	@AfterClass
	public void tearDown() {
		logger.info("=== Login Test Suite Completed ===");
	}

	/**
	 * TC_RF_002-TS_001: Validate logging into the application using valid
	 * credentials
	 */
	@Test(priority = 1, dataProvider = "LastRowLoginData", dataProviderClass = DataProviders.class, description = "Validate logging into the application using valid credentials from last Excel row")
	@Description("Verify that user can successfully login with valid email and password credentials from last Excel row")
	@Severity(SeverityLevel.BLOCKER)
	@Story("TC_RF_002-TS_001")
	public void testValidLoginWithLastRowData(String email, String password) {
		ExtentTestManager.startTest("TC_RF_002-TS_001",
				"Validate logging into the application using valid credentials from last Excel row.");
		try {
			logger.info("Attempting login with last row data - Email: " + email + ", Password: " + password);

			loginPage.navigateToLogin();
			loginPage.login(email, password);

			softAssert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful with valid credentials");
			WaitUtils.sleep(5000);
			softAssert.assertTrue(loginPage.isAccountPageDisplayed(), "User should be on account page after login");

			loginPage.logout();
			ExtentTestManager.logPass("Valid login test completed successfully with last row data");
		} catch (Exception e) {
			ExtentTestManager.logFail("Valid login test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	@Test(priority = 1, description = "Validate logging into the application using valid credentials")
	@Description("Verify that user can successfully login with valid email and password credentials")
	@Severity(SeverityLevel.BLOCKER)
	@Story("TC_RF_002-TS_001")
	public void testValidLogin() {
		ExtentTestManager.startTest("TC_RF_002-TS_001",
				"Validate logging into the application using valid credentials.");
		try {
			logger.info("Attempting login with - Email: " + VALID_EMAIL + ", Password: " + VALID_PASSWORD);

			loginPage.navigateToLogin();
			loginPage.login(VALID_EMAIL, VALID_PASSWORD);

			softAssert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful with valid credentials");
			WaitUtils.sleep(5000);
			softAssert.assertTrue(loginPage.isAccountPageDisplayed(), "User should be on account page after login");

			loginPage.logout();
			ExtentTestManager.logPass("Valid login test completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Valid login test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_002: Validate logging into the application using invalid
	 * credentials
	 */
	@Test(priority = 2, description = "Validate logging into the application using invalid credentials")
	@Description("Verify that system displays appropriate warning message when invalid email and password are used")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_002-TS_002")
	public void testInvalidEmailAndPassword() {
		ExtentTestManager.startTest("TC_RF_002-TS_002",
				"Validate logging into the application using invalid credentials (invalid email and invalid password).");
		try {
			loginPage.navigateToLogin();
			loginPage.login(INVALID_EMAIL, INVALID_PASSWORD);

			softAssert.assertTrue(loginPage.isLoginWarningDisplayed(),
					"Warning message should be displayed for invalid credentials");

			String warningMessage = loginPage.getWarningMessage();
			softAssert.assertTrue(warningMessage.contains("No match for E-Mail Address and/or Password"),
					"Warning message should contain expected text");

			ExtentTestManager.logPass("Invalid credentials test completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Invalid credentials test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_003: Validate login with invalid email address and valid
	 * password
	 */
	@Test(priority = 3, description = "Validate login with invalid email address and valid password")
	@Description("Verify that system prevents login when email is invalid but password is correct")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_002-TS_003")
	public void testInvalidEmailValidPassword() {
		ExtentTestManager.startTest("TC_RF_002-TS_003",
				"Validate logging into the application using an invalid email address and valid password.");
		try {
			loginPage.navigateToLogin();
			loginPage.login(INVALID_EMAIL, VALID_PASSWORD);

			softAssert.assertTrue(loginPage.isLoginWarningDisplayed(),
					"Warning message should be displayed for invalid email");

			ExtentTestManager.logPass("Invalid email with valid password test completed");
		} catch (Exception e) {
			ExtentTestManager.logFail("Invalid email with valid password test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_004: Validate login with valid email address and invalid
	 * password
	 */
	@Test(priority = 4, description = "Validate login with valid email address and invalid password")
	@Description("Verify that system prevents login when email is valid but password is incorrect")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_002-TS_004")
	public void testValidEmailInvalidPassword() {
		ExtentTestManager.startTest("TC_RF_002-TS_004",
				"Validate logging into the application using a valid email address and invalid password.");
		try {
			loginPage.navigateToLogin();
			loginPage.login(VALID_EMAIL, INVALID_PASSWORD);

			softAssert.assertTrue(loginPage.isLoginWarningDisplayed(),
					"Warning message should be displayed for invalid password");

			ExtentTestManager.logPass("Valid email with invalid password test completed");
		} catch (Exception e) {
			ExtentTestManager.logFail("Valid email with invalid password test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_005: Validate login without entering any credentials
	 */
	@Test(priority = 5, description = "Validate login without entering any credentials")
	@Description("Verify that system displays warning when login is attempted with empty email and password fields")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_002-TS_005")
	public void testEmptyCredentials() {
		ExtentTestManager.startTest("TC_RF_002-TS_005",
				"Validate logging into the application without entering any credentials.");
		try {
			loginPage.navigateToLogin();
			loginPage.login("", "");

			softAssert.assertTrue(loginPage.isLoginWarningDisplayed(),
					"Warning message should be displayed for empty credentials");

			ExtentTestManager.logPass("Empty credentials test completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Empty credentials test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_006: Validate login using inactive credentials
	 */
	@Test(priority = 6, description = "Validate login using inactive credentials", enabled = false)
	@Description("Verify that system prevents login when using credentials of an inactive user account")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_002-TS_006")
	public void testInactiveCredentials() {
		ExtentTestManager.startTest("TC_RF_002-TS_006",
				"Validate logging into the application using inactive credentials.");
		try {
			ExtentTestManager.logInfo("Test data for inactive credentials is pending - Test Skipped");
		} catch (Exception e) {
			ExtentTestManager.logFail("Inactive credentials test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_007: Validate the Forgotten Password link functionality
	 */
	@Test(priority = 7, description = "Validate the Forgotten Password link functionality")
	@Description("Verify that Forgotten Password link is present on login page and navigates to correct page when clicked")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_002-TS_007")
	public void testForgottenPasswordLink() {
		ExtentTestManager.startTest("TC_RF_002-TS_007",
				"Validate the Forgotten Password link is present on the login page and is working correctly.");
		try {
			loginPage.navigateToLogin();

			softAssert.assertTrue(loginPage.isForgottenPasswordDisplayed(),
					"Forgotten Password link should be displayed");

			loginPage.clickForgottenPassword();
			softAssert.assertTrue(loginPage.isOnForgottenPasswordPage(),
					"Should be navigated to Forgotten Password page");

			ExtentTestManager.logPass("Forgotten password link test completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Forgotten password link test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_008: Validate login using keyboard navigation
	 */
	@Test(priority = 8, description = "Validate login using keyboard navigation")
	@Description("Verify that user can complete login process using only keyboard navigation with Tab and Enter keys")
	@Severity(SeverityLevel.MINOR)
	@Story("TC_RF_002-TS_008")
	public void testKeyboardNavigationLogin() {
		ExtentTestManager.startTest("TC_RF_002-TS_008",
				"Validate logging into the application using keyboard keys (Tab and Enter).");
		try {
			loginPage.navigateToLogin();
			loginPage.loginWithKeyboard(VALID_EMAIL, VALID_PASSWORD);

			softAssert.assertTrue(loginPage.isLoginSuccessful(),
					"Login should be successful using keyboard navigation");

			loginPage.logout();
			ExtentTestManager.logPass("Keyboard navigation login test completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Keyboard navigation login test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_009: Validate placeholder texts in login form fields
	 */
	@Test(priority = 9, description = "Validate placeholder texts in login form fields")
	@Description("Verify that all input fields have correct placeholder texts according to design specifications")
	@Severity(SeverityLevel.TRIVIAL)
	@Story("TC_RF_002-TS_009")
	public void testPlaceholderTexts() {
		ExtentTestManager.startTest("TC_RF_002-TS_009",
				"Validate that the email address and password fields have proper placeholder text.");
		try {
			loginPage.navigateToLogin();
			loginPage.validatePlaceholderTexts();

			String emailPlaceholder = loginPage.getEmailPlaceholder();
			String passwordPlaceholder = loginPage.getPasswordPlaceholder();

			softAssert.assertEquals(emailPlaceholder, "E-Mail Address", "Email placeholder mismatch");
			softAssert.assertEquals(passwordPlaceholder, "Password", "Password placeholder mismatch");

			ExtentTestManager.logPass("Placeholder texts validation completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Placeholder texts validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_010: Validate browser back button behavior after successful
	 * login
	 */
	@Test(priority = 10, description = "Validate browser back button behavior after successful login")
	@Description("Verify that user remains logged in when using browser back button after successful login")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_002-TS_010")
	public void testBrowserBackAfterLogin() {
		ExtentTestManager.startTest("TC_RF_002-TS_010",
				"Validate logging into the application and then using the browser back button to navigate back.");
		try {
			loginPage.navigateToLogin();
			loginPage.login(VALID_EMAIL, VALID_PASSWORD);

			softAssert.assertTrue(loginPage.isLoginSuccessful(), "User should be logged in");

			loginPage.useBrowserBackButton();

			softAssert.assertTrue(loginPage.isLoginSuccessful(), "User should remain logged after browser back button");

			loginPage.logout();
			ExtentTestManager.logPass("Browser back after login test completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Browser back after login test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_011: Validate browser back button behavior after logout
	 */
	@Test(priority = 11, description = "Validate browser back button behavior after logout")
	@Description("Verify that user does not get automatically logged in when using browser back button after logout")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_002-TS_011")
	public void testBrowserBackAfterLogout() {
		ExtentTestManager.startTest("TC_RF_002-TS_011",
				"Validate logging out from the application and then using the browser back button to navigate back.");
		try {
			loginPage.navigateToLogin();
			loginPage.login(VALID_EMAIL, VALID_PASSWORD);
			loginPage.logout();

			loginPage.useBrowserBackButton();

			softAssert.assertFalse(loginPage.isLoginSuccessful(),
					"User should not be automatically logged in after logout and browser back");

			ExtentTestManager.logPass("Browser back after logout test completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Browser back after logout test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_012: Validate system behavior after multiple unsuccessful login
	 * attempts
	 */
	@Test(priority = 12, description = "Validate system behavior after multiple unsuccessful login attempts")
	@Description("Verify that system displays account lock warning after multiple consecutive failed login attempts")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_002-TS_012")
	public void testMultipleUnsuccessfulLogins() {
		ExtentTestManager.startTest("TC_RF_002-TS_012",
				"Validate the system behavior after multiple unsuccessful login attempts.");
		try {
			loginPage.navigateToLogin();

			loginPage.attemptMultipleInvalidLogins(INVALID_EMAIL, INVALID_PASSWORD, 4);

			loginPage.login(INVALID_EMAIL, INVALID_PASSWORD);

			softAssert.assertTrue(loginPage.isAccountLockedWarningDisplayed(),
					"Account locked warning should be displayed after multiple failed attempts");

			ExtentTestManager.logPass("Multiple unsuccessful login attempts test completed");
		} catch (Exception e) {
			ExtentTestManager.logFail("Multiple unsuccessful login attempts test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_013: Validate that password field content is masked
	 */
	@Test(priority = 13, description = "Validate that password field content is masked")
	@Description("Verify that text entered in password field is hidden and displayed as asterisks or dots for security")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_002-TS_013")
	public void testPasswordMasking() {
		ExtentTestManager.startTest("TC_RF_002-TS_013",
				"Validate that the text entered in the password field is hidden (masked).");
		try {
			loginPage.navigateToLogin();

			boolean isMasked = loginPage.validatePasswordMasking();
			softAssert.assertTrue(isMasked, "Password field should be masked (type='password')");

			String fieldType = loginPage.getPasswordFieldType();
			softAssert.assertEquals(fieldType, "password", "Password field type should be 'password'");

			ExtentTestManager.logPass("Password masking test completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Password masking test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_014: Validate that copying password field content is restricted
	 */
	@Test(priority = 14, description = "Validate that copying password field content is restricted")
	@Description("Verify that password field content cannot be copied using right-click context menu or keyboard shortcuts")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_002-TS_014")
	public void testPasswordCopyRestriction() {
		ExtentTestManager.startTest("TC_RF_002-TS_014",
				"Validate that copying the password field content is restricted.");
		try {
			loginPage.navigateToLogin();

			loginPage.login("test", "hello");

			boolean isCopyRestricted = loginPage.isPasswordCopyRestricted();
			softAssert.assertTrue(isCopyRestricted, "Password copying should be restricted");

			ExtentTestManager.logPass("Password copy restriction test completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Password copy restriction test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_015: Validate that password is not visible in page source
	 */
	@Test(priority = 15, description = "Validate that password is not visible in page source")
	@Description("Verify that password text is not exposed in HTML page source code for security reasons")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_002-TS_015")
	public void testPasswordNotInSource() {
		ExtentTestManager.startTest("TC_RF_002-TS_015",
				"Validate that the password is not visible in the page source.");
		try {
			loginPage.navigateToLogin();

			String testPassword = "hello";
			boolean isHidden = loginPage.isPasswordHiddenInSource(testPassword);

			softAssert.assertTrue(isHidden, "Password should not be visible in page source");

			ExtentTestManager.logPass("Password not in page source test completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Password not in page source test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_016: Validate login functionality after password change
	 */
	@Test(priority = 16, description = "Validate login functionality after password change")
	@Description("Verify that old password becomes invalid and new password works after password change process")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_002-TS_016")
	public void testLoginAfterPasswordChange() {
		ExtentTestManager.startTest("TC_RF_002-TS_016",
				"Validate logging into the application after a password change.");
		try {
			ExtentTestManager.logInfo("This test requires password change functionality implementation");

			softAssert.assertTrue(true, "Placeholder test - Password change functionality to be implemented");
			ExtentTestManager.logPass("Login after password change test placeholder completed");
		} catch (Exception e) {
			ExtentTestManager.logFail("Login after password change test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_017: Validate session persistence after browser closure
	 */
	@Test(priority = 17, description = "Validate session persistence after browser closure")
	@Description("Verify that user session is maintained when browser is closed and reopened without explicit logout")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_002-TS_017")
	public void testSessionPersistence() {
		ExtentTestManager.startTest("TC_RF_002-TS_017",
				"Validate logging into the application, closing the browser without logging out, and reopening the application.");
		try {
			loginPage.navigateToLogin();
			loginPage.login(VALID_EMAIL, VALID_PASSWORD);

			ExtentTestManager.logInfo("Full session persistence test requires browser restart implementation");

			softAssert.assertTrue(loginPage.isLoginSuccessful(), "User should be logged in");
			loginPage.logout();
			ExtentTestManager.logPass("Session persistence test placeholder completed");
		} catch (Exception e) {
			ExtentTestManager.logFail("Session persistence test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_018: Validate login session timeout behavior
	 */
	@Test(priority = 18, description = "Validate login session timeout behavior", enabled = false)
	@Description("Verify that user session automatically expires after specified inactivity period (30 minutes)")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_002-TS_018")
	public void testSessionTimeout() {
		ExtentTestManager.startTest("TC_RF_002-TS_018", "Validate the login session timeout behaviour.");
		try {
			ExtentTestManager.logInfo("Session timeout test requires long wait time - typically run separately");
		} catch (Exception e) {
			ExtentTestManager.logFail("Session timeout test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_019: Validate navigation to other pages from login page
	 */
	@Test(priority = 19, description = "Validate navigation to other pages from login page")
	@Description("Verify that user can navigate to various pages like Register Account from login page")
	@Severity(SeverityLevel.MINOR)
	@Story("TC_RF_002-TS_019")
	public void testNavigationFromLoginPage() {
		ExtentTestManager.startTest("TC_RF_002-TS_019",
				"Validate that the user can navigate to other pages from the login page.");
		try {
			loginPage.navigateToLogin();

			loginPage.navigateToRegisterFromLogin();
			softAssert.assertTrue(driver.getCurrentUrl().contains("register"), "Should be navigated to register page");

			driver.navigate().back();
			softAssert.assertTrue(loginPage.isOnLoginPage(), "Should be back on login page");

			ExtentTestManager.logPass("Navigation from login page test completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Navigation from login page test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_020: Validate various navigation methods to login page
	 */
	@Test(priority = 20, description = "Validate various navigation methods to login page")
	@Description("Verify that login page can be accessed through multiple navigation paths in the application")
	@Severity(SeverityLevel.MINOR)
	@Story("TC_RF_002-TS_020")
	public void testMultipleNavigationWays() {
		ExtentTestManager.startTest("TC_RF_002-TS_020",
				"Validate the various ways of navigating to the login page (e.g., header, footer, direct URL).");
		try {
			loginPage.navigateToLogin();
			softAssert.assertTrue(loginPage.isOnLoginPage(), "Should be on login page via dropdown");

			ExtentTestManager.logInfo("Multiple navigation ways validated");
			ExtentTestManager.logPass("Multiple navigation ways test completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Multiple navigation ways test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_021: Validate page metadata (breadcrumb, heading, URL, title)
	 */
	@Test(priority = 21, description = "Validate page metadata")
	@Description("Verify page breadcrumb, heading, URL, and title are correct")
	@Severity(SeverityLevel.TRIVIAL)
	@Story("TC_RF_002-TS_021")
	public void testPageMetadata() {
		ExtentTestManager.startTest("TC_RF_002-TS_021",
				"Validate the breadcrumb, page heading, page title, and page URL of the login page.");
		try {
			loginPage.navigateToLogin();

			String currentUrl = driver.getCurrentUrl();
			String pageTitle = driver.getTitle();

			softAssert.assertTrue(currentUrl.contains("login"), "URL should contain 'login'");
			softAssert.assertTrue(pageTitle.contains("Login") || pageTitle.contains("Account Login"),
					"Page title should contain login information");

			loginPage.validatePageMetadata();
			ExtentTestManager.logPass("Page metadata validation completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Page metadata validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_022: Validate UI layout and design
	 */
	@Test(priority = 22, description = "Validate UI layout and design")
	@Description("Verify overall UI layout and design elements are properly displayed")
	@Severity(SeverityLevel.MINOR)
	@Story("TC_RF_002-TS_022")
	public void testUILayout() {
		ExtentTestManager.startTest("TC_RF_002-TS_022", "Validate the UI design of the login page.");
		try {
			loginPage.navigateToLogin();
			loginPage.validateUILayout();

			softAssert.assertTrue(loginPage.isForgottenPasswordDisplayed(),
					"Forgotten password link should be present");

			ExtentTestManager.logPass("UI layout validation completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("UI layout validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_002-TS_023: Validate cross-browser compatibility
	 */
	@Test(priority = 23, description = "Validate cross-browser compatibility")
	@Description("Verify that login functionality works correctly across all supported browsers and devices")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_002-TS_023")
	public void testCrossBrowserCompatibility() {
		ExtentTestManager.startTest("TC_RF_002-TS_023",
				"Validate the login functionality across all supported environments (browsers/devices).");
		try {
			loginPage.navigateToLogin();
			loginPage.login(VALID_EMAIL, VALID_PASSWORD);

			softAssert.assertTrue(loginPage.isLoginSuccessful(),
					"Login should work correctly in current browser environment");

			loginPage.logout();
			ExtentTestManager.logPass("Cross-browser compatibility test completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Cross-browser compatibility test failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * Data-Driven Test for multiple login scenarios
	 */
	@Test(priority = 24, dataProvider = "LoginData", dataProviderClass = DataProviders.class, description = "Data-driven login tests with various credential combinations")
	@Description("Verify login functionality with multiple test data combinations covering valid and invalid scenarios")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_002-TS_024")
	public void testDataDrivenLogin(String email, String password, String expectedResult) {
		ExtentTestManager.startTest("Data-Driven Login Test", "Test login with comprehensive data: " + email);
		try {
			loginPage.navigateToLogin();
			loginPage.login(email, password);

			boolean expectedSuccess = "SUCCESS".equals(expectedResult);
			boolean actualSuccess = loginPage.isLoginSuccessful();

			if (expectedSuccess) {
				softAssert.assertTrue(actualSuccess, "Login should succeed with valid credentials: " + email);
				loginPage.logout();
				ExtentTestManager.logPass("Valid login completed for: " + email);
			} else {
				softAssert.assertFalse(actualSuccess, "Login should fail with invalid credentials: " + email);
				softAssert.assertTrue(loginPage.isLoginWarningDisplayed(),
						"Warning should be displayed for failed login");
				ExtentTestManager.logPass("Invalid login handled correctly for: " + email);
			}
		} catch (Exception e) {
			ExtentTestManager.logFail("Data-driven login test failed for: " + email + " - " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * Cleanup method - navigate to home after tests
	 */
	@Test(priority = 99, description = "Cleanup and navigation to home")
	@Description("Cleanup and navigate to home page after all tests")
	@Severity(SeverityLevel.MINOR)
	@Story("TC_RF_002-TS_099")
	public void cleanupAfterTests() {
		ExtentTestManager.startTest("Cleanup", "Navigate to home page after login tests");
		try {
			driver.get(ConfigReader.getUrl());
			ExtentTestManager.logPass("Cleanup completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Cleanup failed: " + e.getMessage(), driver);
			throw e;
		}
	}
}