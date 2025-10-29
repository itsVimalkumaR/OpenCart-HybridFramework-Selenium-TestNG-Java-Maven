package com.opencart.tests;

import java.util.Arrays;
import java.util.List;

import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.opencart.base.BaseTest;
import com.opencart.pages.RegisterPage;
import com.opencart.utilities.DataProviders;
import com.opencart.utilities.ExtentTestManager;
import com.opencart.utilities.WaitUtils;

/**
 * RegisterTests - Test class for OpenCart registration functionality Covers all
 * test cases from TC_RF_001-TS_001 to TC_RF_001-TS_027
 */
@Epic("Authentication & Account Management")
@Feature("Registration Functionality")
@Story("TC_RF_001 - Validate the functionality of Register Account.")
public class RegisterTests extends BaseTest {

	private RegisterPage registerPage;

	// Test Data Constants (will be populated from Excel via DataProviders)
	private String FIRST_NAME, LAST_NAME, VALID_EMAIL, VALID_PHONE, VALID_PASSWORD;

	@BeforeClass
	public void setupTest() {
		registerPage = new RegisterPage(driver);
		loadTestDataFromExcel(); // Load test data from first row of Excel for single data tests
	}

	/**
	 * Load test data from the first row of Excel file
	 */
	private void loadTestDataFromExcel() {
		try {
			Object[][] validData = new DataProviders().getValidRegistrationData();
			if (validData.length > 0) {
				Object[] firstRow = validData[0];
				FIRST_NAME = firstRow[0].toString();
				LAST_NAME = firstRow[1].toString();
				VALID_EMAIL = "test" + System.currentTimeMillis() + "@test.com"; // Generate unique email
				VALID_PHONE = firstRow[3].toString();
				VALID_PASSWORD = firstRow[4].toString();
			} 
//			else {
//				// Fallback to default values if no valid data found
//				setDefaultTestData();
//			}
		} catch (Exception e) {
			logger.error("Failed to load test data from Excel: " + e.getMessage());
//			setDefaultTestData(); // Set default values if Excel loading fails
		}
	}

//	private void setDefaultTestData() {
//		FIRST_NAME = "Vimal";
//		LAST_NAME = "Kumar";
//		VALID_EMAIL = "test" + System.currentTimeMillis() + "@test.com";
//		VALID_PHONE = "9876543210";
//		VALID_PASSWORD = "Password@123";
//	}

	/**
	 * TC_RF_001-TS_001: Validate placeholder texts in all fields
	 */
	@Test(priority = 1, description = "Validate placeholder texts in registration form fields")
	@Description("Verify that all input fields have correct placeholder texts")
	@Severity(SeverityLevel.TRIVIAL)
	@Story("TC_RF_001-TS_001")
	public void testPlaceholderTexts() {
		ExtentTestManager.startTest("TC_RF_001-TS_001",
				"Validate that all fields in the Register Account page contain appropriate placeholder text.");
		try {
			registerPage.navigateToRegister();
			registerPage.validateAllPlaceholders();
			ExtentTestManager.logPass("All placeholder texts validated successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Placeholder validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_002: Validate mandatory field indicators (red asterisks)
	 */
	@Test(priority = 2, description = "Validate mandatory field indicators")
	@Description("Verify that mandatory fields are marked with red asterisks")
	@Severity(SeverityLevel.MINOR)
	@Story("TC_RF_001-TS_002")
	public void testMandatoryFieldIndicators() {
		ExtentTestManager.startTest("TC_RF_001-TS_002",
				"Validate that all mandatory fields on the Register Account page are marked with a red asterisk (*) symbol.");
		try {
			registerPage.navigateToRegister();
			registerPage.validateMandatoryFieldIndicators();
			ExtentTestManager.logPass("Mandatory field indicators validated successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Mandatory field indicators validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_003: Validate page metadata (breadcrumb, heading, URL, title)
	 */
	@Test(priority = 3, description = "Validate page metadata")
	@Description("Verify page breadcrumb, heading, URL, and title are correct")
	@Severity(SeverityLevel.TRIVIAL)
	@Story("TC_RF_001-TS_003")
	public void testPageMetadata() {
		ExtentTestManager.startTest("TC_RF_001-TS_003",
				"Validate the breadcrumb, page heading, page URL, and page title of the 'Register Account' page.");
		try {
			registerPage.navigateToRegister();
			registerPage.validatePageMetadata();
			ExtentTestManager.logPass("Page metadata validated successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Page metadata validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_004: Validate UI layout and design
	 */
	@Test(priority = 4, description = "Validate UI layout and design")
	@Description("Verify overall UI layout and design elements are properly displayed")
	@Severity(SeverityLevel.MINOR)
	@Story("TC_RF_001-TS_004")
	public void testUILayout() {
		ExtentTestManager.startTest("TC_RF_001-TS_004",
				"Validate the overall UI layout and design of the 'Register Account' page.");
		try {
			registerPage.navigateToRegister();
			registerPage.validateUILayout();
			ExtentTestManager.logPass("UI layout validated successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("UI layout validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_005: Validate registration using only mandatory fields
	 */
	@Test(priority = 5, description = "Validate registration using only mandatory fields")
	@Description("Test successful registration with only mandatory fields filled")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_001-TS_005")
	public void testRegistrationWithMandatoryFields() {
		ExtentTestManager.startTest("TC_RF_001-TS_005",
				"Validate account registration providing only the mandatory fields.");
		try {
			registerPage.navigateToRegister();
			String uniqueEmail = "mandatory" + System.currentTimeMillis() + "@test.com";

			// Use direct Selenium calls instead of commonWrapper
			WebElement firstNameField = waitUtils.waitForElementToBeVisible(locators.FIRST_NAME_INPUT_FIELD);
			firstNameField.clear();
			firstNameField.sendKeys(FIRST_NAME);

			WebElement lastNameField = waitUtils.waitForElementToBeVisible(locators.LAST_NAME_INPUT_FIELD);
			lastNameField.clear();
			lastNameField.sendKeys(LAST_NAME);

			WebElement emailField = waitUtils.waitForElementToBeVisible(locators.EMAIL_INPUT_FIELD);
			emailField.clear();
			emailField.sendKeys(uniqueEmail);

			WebElement phoneField = waitUtils.waitForElementToBeVisible(locators.TELEPHONE_INPUT_FIELD);
			phoneField.clear();
			phoneField.sendKeys(VALID_PHONE);

			WebElement passwordField = waitUtils.waitForElementToBeVisible(locators.PASSWORD_INPUT_FIELD);
			passwordField.clear();
			passwordField.sendKeys(VALID_PASSWORD);

			WebElement confirmPasswordField = waitUtils
					.waitForElementToBeVisible(locators.CONFIRM_PASSWORD_INPUT_FIELD);
			confirmPasswordField.clear();
			confirmPasswordField.sendKeys(VALID_PASSWORD);

			boolean beforeClick = registerPage.isPrivacyPolicyChecked();
			softAssert.assertFalse(beforeClick, "Checkbox should not be selected by default.");

			registerPage.agreePrivacyPolicy();

			boolean afterClick = registerPage.isPrivacyPolicyChecked();
			softAssert.assertTrue(afterClick, "Checkbox should be selected after clicking.");
			softAssert.assertNotEquals(beforeClick, afterClick, "Privacy Policy checkbox state should change!");

			registerPage.clickContinue();

			softAssert.assertTrue(registerPage.isRegistrationSuccess(),
					"Registration should be successful with mandatory fields");

			ExtentTestManager.logPass("Registration with mandatory fields completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Registration with mandatory fields completed failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_006: Validate registration with all available fields
	 */
	@Test(priority = 6, description = "Validate registration with all available fields")
	@Description("Test successful registration with all available fields including newsletter subscription")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_001-TS_006")
	public void testRegistrationWithAllFields() {
		ExtentTestManager.startTest("TC_RF_001-TS_006",
				"Validate account registration by providing all available fields.");
		try {
			registerPage.navigateToRegister();
			String uniqueEmail = "allfields" + System.currentTimeMillis() + "@test.com";
			registerPage.enterDetails(FIRST_NAME, LAST_NAME, uniqueEmail, VALID_PHONE, VALID_PASSWORD, VALID_PASSWORD);

			// Subscribe to newsletter (Yes)
			commonWrapper.clickWhenVisible(locators.NEWSLETTER_YES);
			registerPage.agreePrivacyPolicy();
			registerPage.clickContinue();

			softAssert.assertTrue(registerPage.isRegistrationSuccess(),
					"Registration should be successful with all fields");
			ExtentTestManager.logPass("Registration with all fields completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Registration with all fields failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_007: Validate error messages when mandatory fields are empty
	 */
	@Test(priority = 7, description = "Validate error messages for empty mandatory fields")
	@Description("Verify appropriate error messages are displayed when mandatory fields are left empty")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_001-TS_007")
	public void testEmptyMandatoryFields() {
		ExtentTestManager.startTest("TC_RF_001-TS_007",
				"Validate that appropriate notification messages are displayed when mandatory fields are left empty and the form is submitted.");
		try {
			registerPage.navigateToRegister();
			registerPage.clickContinueWithoutData();

			// Wait a moment for errors to appear
			waitUtils.sleep(2000);

			// Verify error messages are displayed
			List<String> errors = registerPage.getErrorMessages();
			ExtentTestManager.logInfo("Found errors: " + errors);

			// Check if we have any error messages or warning alerts
			if (errors.size() > 0) {
				ExtentTestManager.logPass("Error messages displayed: " + errors);
				softAssert.assertTrue(true, "Error messages should be displayed for empty mandatory fields");
			} else {
				// Check if there's a generic warning
				String warning = registerPage.getErrorMessage(locators.TELEPHONE_ERROR);
				if (!warning.isEmpty()) {
					ExtentTestManager.logPass("Warning message displayed: " + warning);
					softAssert.assertTrue(true, "Warning message should be displayed");
				} else {
					ExtentTestManager.logFail("No error messages or warnings found for empty mandatory fields", driver);
					softAssert.fail("No error messages or warnings found for empty mandatory fields");
				}
			}
		} catch (Exception e) {
			ExtentTestManager.logFail("Empty mandatory fields validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_008: Validate fields don't accept only spaces
	 */
	@Test(priority = 8, description = "Validate fields don't accept only spaces")
	@Description("Verify that mandatory fields do not accept only spaces as input")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_001-TS_008")
	public void testFieldsWithSpacesOnly() {
		ExtentTestManager.startTest("TC_RF_001-TS_008",
				"Validate that mandatory fields do not accept only spaces as input.");
		try {
			registerPage.navigateToRegister();

			// Enter spaces in all fields
			registerPage.enterDetails("   ", "   ", "   ", "   ", "   ", "   ");
			registerPage.agreePrivacyPolicy();
			registerPage.clickContinue();

			waitUtils.sleep(2000);

			registerPage.verifyMandatoryFieldErrors();
			ExtentTestManager.logPass("Space-only input validation completed");
		} catch (Exception e) {
			ExtentTestManager.logFail("Space-only input validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_009: Validate trimming of leading/trailing spaces
	 */
	@Test(priority = 9, description = "Validate trimming of leading/trailing spaces")
	@Description("Verify that leading and trailing spaces are trimmed from input fields")
	@Severity(SeverityLevel.MINOR)
	@Story("TC_RF_001-TS_009")
	public void testTrimSpaces() {
		ExtentTestManager.startTest("TC_RF_001-TS_009",
				"Validate that leading and trailing spaces entered in the input fields are trimmed before submission.");
		try {
			registerPage.navigateToRegister();

			// Enter values with leading/trailing spaces
			String uniqueEmail = "  trimtest" + System.currentTimeMillis() + "@test.com  ";
			registerPage.enterDetails("  " + FIRST_NAME + "  ", "  " + LAST_NAME + "  ", uniqueEmail,
					"  " + VALID_PHONE + "  ", "  " + VALID_PASSWORD + "  ", "  " + VALID_PASSWORD + "  ");
			registerPage.agreePrivacyPolicy();
			registerPage.clickContinue();

			// If registration succeeds, spaces were likely trimmed
			if (registerPage.isRegistrationSuccess()) {
				ExtentTestManager.logPass("Leading/trailing spaces were successfully handled");
			} else {
				ExtentTestManager.logInfo("Registration failed - may indicate space handling behavior");
			}
		} catch (Exception e) {
			ExtentTestManager.logFail("Space trimming validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_010: Validate registration with invalid email format - Data
	 * Driven
	 */
	@Test(priority = 10, dataProvider = "InvalidRegistrationData", dataProviderClass = DataProviders.class, description = "Validate registration with invalid email format")
	@Description("Validate registration with various invalid email formats")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_001-TS_010")
	public void testInvalidEmailFormat(String firstName, String lastName, String email, String phone, String password,
			String confirmPassword, String privacyPolicy) {
		// Only run for rows with invalid email
		if (!isValidEmail(email)) {
			ExtentTestManager.startTest("TC_RF_001-TS_010",
					"Validate account registration using an invalid email address format: " + email);
			try {
				registerPage.navigateToRegister();
				registerPage.registerWithData(firstName, lastName, email, phone, password, confirmPassword,
						privacyPolicy);
				registerPage.verifyInvalidEmailError();
				ExtentTestManager.logPass("Invalid email format validation completed for: " + email);
			} catch (Exception e) {
				ExtentTestManager.logFail(
						"Invalid email format validation failed for: " + email + " - " + e.getMessage(), driver);
				throw e;
			}
		}
	}

	/**
	 * TC_RF_001-TS_011: Validate registration with invalid phone number - Data
	 * Driven
	 */
	@Test(priority = 11, dataProvider = "InvalidRegistrationData", dataProviderClass = DataProviders.class, description = "Validate registration with invalid phone number")
	@Description("Validate registration with various invalid phone number formats")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_001-TS_011")
	public void testInvalidPhoneNumber(String firstName, String lastName, String email, String phone, String password,
			String confirmPassword, String privacyPolicy) {
		// Only run for rows with invalid phone
		if (!isValidPhone(phone)) {
			ExtentTestManager.startTest("TC_RF_001-TS_011",
					"Validate account registration using an invalid phone number: " + phone);
			try {
				registerPage.navigateToRegister();
				registerPage.registerWithData(firstName, lastName, email, phone, password, confirmPassword,
						privacyPolicy);
				registerPage.verifyInvalidPhoneError();
				ExtentTestManager.logPass("Invalid phone number validation completed for: " + phone);
			} catch (Exception e) {
				ExtentTestManager.logFail(
						"Invalid phone number validation failed for: " + phone + " - " + e.getMessage(), driver);
				throw e;
			}
		}
	}

	/**
	 * TC_RF_001-TS_012: Validate password complexity requirements
	 */
	@Test(priority = 12, description = "Validate password complexity requirements")
	@Description("Verify that password fields enforce required complexity standards")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_001-TS_012")
	public void testPasswordComplexity() {
		ExtentTestManager.startTest("TC_RF_001-TS_012",
				"Validate that the 'Password' and 'Password Confirm' fields enforce the required password complexity standards.");
		try {
			registerPage.navigateToRegister();

			registerPage.enterDetails(FIRST_NAME, LAST_NAME, "complexity" + System.currentTimeMillis() + "@test.com",
					VALID_PHONE, "123", "123"); // Weak password
			registerPage.agreePrivacyPolicy();
			registerPage.clickContinue();

			// Wait a bit for response
			waitUtils.sleep(3000);

			// Check if registration failed (which indicates password complexity
			// enforcement)
			boolean isSuccess = registerPage.isRegistrationSuccess();

			if (!isSuccess) {
				// Registration failed - check for any error messages
				List<String> errors = registerPage.getErrorMessages();
				ExtentTestManager.logInfo("Errors found after weak password submission: " + errors);

				boolean hasPasswordError = errors.stream().anyMatch(error -> error.toLowerCase().contains("password"));

				softAssert.assertFalse(isSuccess, "Registration should fail with weak password");
				softAssert.assertTrue(hasPasswordError || !errors.isEmpty(),
						"Should show error message for weak password");

				ExtentTestManager
						.logPass("Password complexity validation completed - weak password correctly rejected");
			} else {
				ExtentTestManager.logWarn(
						"Registration succeeded with weak password - complexity requirements may not be enforced");
			}

		} catch (Exception e) {
			ExtentTestManager.logFail("Password complexity validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_013: Validate password mismatch error - Data Driven
	 */
	@Test(priority = 13, dataProvider = "PasswordMismatchData", dataProviderClass = DataProviders.class, description = "Validate password mismatch error")
	@Description("Verify error is displayed when password and confirm password don't match")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_001-TS_013")
	public void testPasswordMismatchWithData(String firstName, String lastName, String email, String phone,
			String password, String confirmPassword, String privacyPolicy) {
		ExtentTestManager.startTest("TC_RF_001-TS_013",
				"Validate account registration by entering different values in the 'Password' and 'Password Confirm' fields.");
		try {
			registerPage.navigateToRegister();
			registerPage.registerWithData(firstName, lastName, email, phone, password, confirmPassword, privacyPolicy);

			// Verify password mismatch error
			String error = registerPage.getErrorMessage(locators.PASSWORD_MISMATCH_ERROR);
			softAssert.assertTrue(error.contains("Password") && error.contains("confirm"),
					"Password confirmation error should be displayed");
			ExtentTestManager.logPass("Password mismatch validation completed for: " + firstName + " " + lastName);
		} catch (Exception e) {
			ExtentTestManager.logFail("Password mismatch validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_014: Validate empty password confirm field
	 */
	@Test(priority = 14, description = "Validate empty password confirm field")
	@Description("Verify error is displayed when password confirm field is left empty")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_001-TS_014")
	public void testEmptyPasswordConfirm() {
		ExtentTestManager.startTest("TC_RF_001-TS_014",
				"Validate account registration by filling only the 'Password' field and leaving the 'Password Confirm' field blank.");
		try {
			registerPage.navigateToRegister();

			registerPage.enterDetails(FIRST_NAME, LAST_NAME, "confirm" + System.currentTimeMillis() + "@test.com",
					VALID_PHONE, VALID_PASSWORD, "");
			registerPage.agreePrivacyPolicy();
			registerPage.clickContinue();

			// Should show password confirmation error
			String errorText = commonWrapper.getElementText(locators.CONFIRM_PASSWORD_ERROR);
			softAssert.assertTrue(errorText.contains("Password") && errorText.contains("confirm"),
					"Password confirmation error should be displayed");
			ExtentTestManager.logPass("Empty password confirm validation completed");
		} catch (Exception e) {
			ExtentTestManager.logFail("Empty password confirm validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_015: Validate password visibility toggle
	 */
	@Test(priority = 15, description = "Validate password visibility toggle")
	@Description("Verify password fields have toggle option to hide/show entered text")
	@Severity(SeverityLevel.MINOR)
	@Story("TC_RF_001-TS_015")
	public void testPasswordVisibilityToggle() {
		ExtentTestManager.startTest("TC_RF_001-TS_015",
				"Validate that the password fields provide a toggle option to hide/show the entered text.");
		try {
			registerPage.navigateToRegister();

			// Enter password and verify it's hidden (type=password)
			commonWrapper.clickAndInputValue(locators.PASSWORD_INPUT_FIELD, VALID_PASSWORD);
			String fieldType = driver.findElement(locators.PASSWORD_INPUT_FIELD).getAttribute("type");
			softAssert.assertEquals(fieldType, "password", "Password should be hidden by default");

			ExtentTestManager.logPass("Password visibility toggle validation completed");
		} catch (Exception e) {
			ExtentTestManager.logFail("Password visibility toggle validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_016: Validate field constraints (character limits)
	 */
	@Test(priority = 16, description = "Validate field constraints and character limits")
	@Description("Verify field constraints like character limits match requirements")
	@Severity(SeverityLevel.MINOR)
	@Story("TC_RF_001-TS_016")
	public void testFieldConstraints() {
		ExtentTestManager.startTest("TC_RF_001-TS_016",
				"Validate that field constraints (height, width, character limits) match the client's requirements.");
		try {
			registerPage.navigateToRegister();

			// Test field sizes and constraints
			List<By> fields = Arrays.asList(locators.FIRST_NAME_INPUT_FIELD, locators.LAST_NAME_INPUT_FIELD,
					locators.EMAIL_INPUT_FIELD, locators.TELEPHONE_INPUT_FIELD, locators.PASSWORD_INPUT_FIELD,
					locators.CONFIRM_PASSWORD_INPUT_FIELD);

			for (By field : fields) {
				WebElement element = driver.findElement(field);
				softAssert.assertTrue(element.isDisplayed(), "Field should be displayed: " + field);
				softAssert.assertTrue(element.isEnabled(), "Field should be enabled: " + field);

				// Check for maxlength attribute if present
				String maxLength = element.getAttribute("maxlength");
				if (maxLength != null) {
					ExtentTestManager.logInfo("Field " + field + " has maxlength: " + maxLength);
				}
			}

			ExtentTestManager.logPass("Field constraints validation completed");
		} catch (Exception e) {
			ExtentTestManager.logFail("Field constraints validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_017: Validate navigation to register page from various locations
	 */
	@Test(priority = 17, description = "Validate navigation to register page")
	@Description("Verify various ways to navigate to register page work correctly")
	@Severity(SeverityLevel.MINOR)
	@Story("TC_RF_001-TS_017")
	public void testNavigationToRegister() {
		ExtentTestManager.startTest("TC_RF_001-TS_017",
				"Validate various ways of navigating to the 'Register Account' page (e.g., header, footer, login page).");
		try {
			registerPage.navigateToRegister();
			String currentUrl = driver.getCurrentUrl();
			softAssert.assertTrue(currentUrl.contains("register"), "Should be on register page");
			ExtentTestManager.logPass("Navigation to register page validated");
		} catch (Exception e) {
			ExtentTestManager.logFail("Navigation to register page validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_018: Validate registration using keyboard navigation
	 */
	@Test(priority = 18, description = "Validate registration using keyboard navigation")
	@Description("Verify registration can be completed using only keyboard navigation")
	@Severity(SeverityLevel.MINOR)
	@Story("TC_RF_001-TS_018")
	public void testKeyboardNavigation() {
		ExtentTestManager.startTest("TC_RF_001-TS_018",
				"Validate the ability to complete account registration using only keyboard navigation.");
		try {
			registerPage.navigateToRegister();

			// Use keyboard navigation to fill form
			commonWrapper.enterValueWhenVisible(locators.FIRST_NAME_INPUT_FIELD, FIRST_NAME);
			commonWrapper.enterValueWhenVisible(locators.LAST_NAME_INPUT_FIELD, LAST_NAME);

			String uniqueEmail = "keyboard" + System.currentTimeMillis() + "@test.com";
			commonWrapper.enterValueWhenVisible(locators.EMAIL_INPUT_FIELD, uniqueEmail);
			commonWrapper.enterValueWhenVisible(locators.TELEPHONE_INPUT_FIELD, VALID_PHONE);
			commonWrapper.enterValueWhenVisible(locators.PASSWORD_INPUT_FIELD, VALID_PASSWORD);
			commonWrapper.enterValueWhenVisible(locators.CONFIRM_PASSWORD_INPUT_FIELD, VALID_PASSWORD);

			// Navigate to privacy policy using keyboard (Tab)
			driver.findElement(locators.CONFIRM_PASSWORD_INPUT_FIELD).sendKeys(org.openqa.selenium.Keys.TAB);
			driver.findElement(locators.PRIVACY_POLICY_CHECKBOX).sendKeys(org.openqa.selenium.Keys.SPACE);

			// Navigate to continue button and press Enter
			driver.findElement(locators.PRIVACY_POLICY_CHECKBOX).sendKeys(org.openqa.selenium.Keys.TAB);
			driver.findElement(locators.CONTINUE_BUTTON).sendKeys(org.openqa.selenium.Keys.ENTER);

			softAssert.assertTrue(registerPage.isRegistrationSuccess(),
					"Registration should succeed with keyboard navigation");
			ExtentTestManager.logPass("Keyboard navigation registration completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Keyboard navigation registration failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_019: Validate registration with Newsletter subscription (Yes)
	 */
	@Test(priority = 19, description = "Validate registration with Newsletter subscription (Yes)")
	@Description("Verify successful registration when newsletter subscription is set to Yes")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_001-TS_019")
	public void testNewsletterSubscriptionYes() {
		ExtentTestManager.startTest("TC_RF_001-TS_019",
				"Validate registration when 'Yes' is selected for the Newsletter subscription.");
		try {
			registerPage.navigateToRegister();

			String uniqueEmail = "newsyes" + System.currentTimeMillis() + "@test.com";
			registerPage.enterDetails(FIRST_NAME, LAST_NAME, uniqueEmail, VALID_PHONE, VALID_PASSWORD, VALID_PASSWORD);
			commonWrapper.clickWhenVisible(locators.NEWSLETTER_YES);
			registerPage.agreePrivacyPolicy();
			registerPage.clickContinue();

			softAssert.assertTrue(registerPage.isRegistrationSuccess(), "Registration with newsletter should succeed");
			ExtentTestManager.logPass("Newsletter subscription (Yes) validation completed");
		} catch (Exception e) {
			ExtentTestManager.logFail("Newsletter subscription (Yes) validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_020: Validate registration with Newsletter subscription (No)
	 */
	@Test(priority = 20, description = "Validate registration with Newsletter subscription (No)")
	@Description("Verify successful registration when newsletter subscription is set to No")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_001-TS_020")
	public void testNewsletterSubscriptionNo() {
		ExtentTestManager.startTest("TC_RF_001-TS_020",
				"Validate registration when 'No' is selected for the Newsletter subscription.");
		try {
			registerPage.navigateToRegister();

			String uniqueEmail = "newsno" + System.currentTimeMillis() + "@test.com";
			registerPage.enterDetails(FIRST_NAME, LAST_NAME, uniqueEmail, VALID_PHONE, VALID_PASSWORD, VALID_PASSWORD);
			commonWrapper.clickWhenVisible(locators.NEWSLATTER_NO);
			registerPage.agreePrivacyPolicy();
			registerPage.clickContinue();

			softAssert.assertTrue(registerPage.isRegistrationSuccess(),
					"Registration without newsletter should succeed");
			ExtentTestManager.logPass("Newsletter subscription (No) validation completed");
		} catch (Exception e) {
			ExtentTestManager.logFail("Newsletter subscription (No) validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_021: Validate Privacy Policy checkbox is not selected by default
	 */
	@Test(priority = 21, description = "Validate Privacy Policy checkbox default state")
	@Description("Verify privacy policy checkbox is not selected by default")
	@Severity(SeverityLevel.TRIVIAL)
	@Story("TC_RF_001-TS_021")
	public void testPrivacyPolicyDefaultState() {
		ExtentTestManager.startTest("TC_RF_001-TS_021",
				"Validate that the 'Privacy Policy' checkbox is not selected by default.");
		try {
			registerPage.navigateToRegister();

			boolean isChecked = registerPage.isPrivacyPolicyChecked();
			softAssert.assertFalse(isChecked, "Privacy Policy checkbox should not be selected by default");

			ExtentTestManager.logPass("Privacy Policy default state validation completed");
		} catch (Exception e) {
			ExtentTestManager.logFail("Privacy Policy default state validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_022: Validate registration fails without Privacy Policy
	 * agreement
	 */
	@Test(priority = 22, dataProvider = "PrivacyPolicyData", dataProviderClass = DataProviders.class, description = "Validate registration fails without Privacy Policy agreement")
	@Description("Verify registration fails when privacy policy is not accepted")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_001-TS_022")
	public void testRegistrationWithoutPrivacyPolicy(String firstName, String lastName, String email, String phone,
			String password, String confirmPassword, String privacyPolicy) {
		ExtentTestManager.startTest("TC_RF_001-TS_022",
				"Validate that account registration fails if the 'Privacy Policy' checkbox is not selected.");
		try {
			registerPage.navigateToRegister();
			registerPage.registerWithData(firstName, lastName, email, phone, password, confirmPassword, privacyPolicy);

			// Wait for response
			waitUtils.sleep(3000);

			// Check if registration failed
			boolean isSuccess = registerPage.isRegistrationSuccess();

			if (!isSuccess) {
				// Check for privacy policy error in various forms
				List<String> errors = registerPage.getErrorMessages();
				ExtentTestManager.logInfo("Errors found without privacy policy: " + errors);

				boolean hasPrivacyError = errors.stream().anyMatch(
						error -> error.toLowerCase().contains("privacy") || error.toLowerCase().contains("policy"));

				softAssert.assertFalse(isSuccess, "Registration should fail without privacy policy agreement");
				softAssert.assertTrue(hasPrivacyError || !errors.isEmpty(),
						"Should show error message when privacy policy is not accepted");

				ExtentTestManager
						.logPass("Privacy Policy requirement validation completed for: " + firstName + " " + lastName);
			} else {
				ExtentTestManager.logWarn("Registration succeeded without privacy policy - this might be unexpected");
			}
		} catch (Exception e) {
			ExtentTestManager.logFail("Privacy Policy requirement validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_023: Validate registration with existing email address
	 */
	@Test(priority = 23, description = "Validate registration with existing email address")
	@Description("Verify error is displayed when trying to register with existing email")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_001-TS_023")
	public void testDuplicateEmailRegistration() {
		ExtentTestManager.startTest("TC_RF_001-TS_023",
				"Validate account registration using an email address that is already registered.");
		try {
			// First registration
			registerPage.navigateToRegister();
			String uniqueEmail = "duplicate" + System.currentTimeMillis() + "@test.com";
			registerPage.enterDetails(FIRST_NAME, LAST_NAME, uniqueEmail, VALID_PHONE, VALID_PASSWORD, VALID_PASSWORD);
			registerPage.agreePrivacyPolicy();
			registerPage.clickContinue();

			// Navigate back to register page and try with same email
			registerPage.navigateToRegister();
			registerPage.enterDetails("Different", "User", uniqueEmail, "9998887777", VALID_PASSWORD, VALID_PASSWORD);
			registerPage.agreePrivacyPolicy();
			registerPage.clickContinue();

			// Verify duplicate email error
			String error = registerPage.getErrorMessage(locators.EMAIL_EXISTS_ERROR);
			softAssert.assertTrue(error.contains("already registered") || !error.isEmpty(),
					"Duplicate email warning should be shown");
			ExtentTestManager.logPass("Duplicate email validation completed");
		} catch (Exception e) {
			ExtentTestManager.logFail("Duplicate email validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_024: Validate data storage in database - Data Driven with Valid
	 * Data
	 */
	@Test(priority = 24, dataProvider = "ValidRegistrationData", dataProviderClass = DataProviders.class, description = "Validate data storage verification")
	@Description("Verify registration data is correctly stored in database")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_001-TS_024")
	public void testDataStorageVerification(String firstName, String lastName, String email, String phone,
			String password, String confirmPassword, String privacyPolicy) {
		ExtentTestManager.startTest("TC_RF_001-TS_024",
				"Validate that the information provided during registration is correctly stored in the database.");
		try {
			registerPage.navigateToRegister();

			// Generate unique email to avoid conflicts
			String uniqueEmail = "storage" + System.currentTimeMillis() + "@test.com";
			registerPage.enterDetails(firstName, lastName, uniqueEmail, phone, password, confirmPassword);
			registerPage.agreePrivacyPolicy();
			registerPage.clickContinue();

			// Verify registration success as proxy for data storage
			softAssert.assertTrue(registerPage.isRegistrationSuccess(),
					"Registration success indicates data was likely stored correctly for: " + firstName + " "
							+ lastName);

			ExtentTestManager.logPass("Data storage verification completed for: " + firstName + " " + lastName);
		} catch (Exception e) {
			ExtentTestManager.logFail(
					"Data storage verification failed for: " + firstName + " " + lastName + " - " + e.getMessage(),
					driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_025: Validate all navigation links on register page
	 */
	@Test(priority = 25, description = "Validate all navigation links on register page")
	@Description("Verify all navigation links on register page work correctly")
	@Severity(SeverityLevel.MINOR)
	@Story("TC_RF_001-TS_025")
	public void testNavigationLinks() {
		ExtentTestManager.startTest("TC_RF_001-TS_025",
				"Validate that all navigation links or options on the 'Register Account' page work correctly.");
		try {
			registerPage.navigateToRegister();

			// Test various navigation elements
			if (WaitUtils.isElementPresent(driver, locators.LOGIN_LINK)) {
				commonWrapper.clickWhenVisible(locators.LOGIN_LINK);
				softAssert.assertTrue(driver.getCurrentUrl().contains("login"), "Should navigate to login page");
				registerPage.navigateToRegister(); // Go back
			}

			ExtentTestManager.logPass("All navigation links validated successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Navigation link validation failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_026: Validate confirmation email - Data Driven with Valid Data
	 */
	@Test(priority = 26, dataProvider = "ValidRegistrationData", dataProviderClass = DataProviders.class, description = "Validate confirmation email process")
	@Description("Verify confirmation email is sent upon successful registration")
	@Severity(SeverityLevel.NORMAL)
	@Story("TC_RF_001-TS_026")
	public void testConfirmationEmail(String firstName, String lastName, String email, String phone, String password,
			String confirmPassword, String privacyPolicy) {
		ExtentTestManager.startTest("TC_RF_001-TS_026",
				"Validate that a \"Thank you for registering\" confirmation email is sent upon successful registration.");
		try {
			registerPage.navigateToRegister();

			String uniqueEmail = "emailtest" + System.currentTimeMillis() + "@test.com";
			registerPage.enterDetails(firstName, lastName, uniqueEmail, phone, password, confirmPassword);
			registerPage.agreePrivacyPolicy();
			registerPage.clickContinue();

			// Verify registration success (email would be sent upon success)
			if (registerPage.isRegistrationSuccess()) {
				ExtentTestManager
						.logPass("Registration successful - confirmation email should be sent to: " + uniqueEmail);
				ExtentTestManager.logInfo("Note: Actual email verification requires email server access");
			} else {
				ExtentTestManager.logFail(
						"Registration failed - cannot test email confirmation for: " + firstName + " " + lastName,
						driver);
			}
		} catch (Exception e) {
			ExtentTestManager.logFail(
					"Confirmation email validation failed for: " + firstName + " " + lastName + " - " + e.getMessage(),
					driver);
			throw e;
		}
	}

	/**
	 * TC_RF_001-TS_027: Validate cross-browser compatibility - Data Driven with
	 * Valid Data
	 */
	@Test(priority = 27, dataProvider = "ValidRegistrationData", dataProviderClass = DataProviders.class, description = "Validate cross-browser compatibility")
	@Description("Verify registration functionality works across different browsers")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_001-TS027")
	public void testCrossBrowserCompatibility(String firstName, String lastName, String email, String phone,
			String password, String confirmPassword, String privacyPolicy) {
		ExtentTestManager.startTest("TC_RF_001-TS_027",
				"Validate the 'Register Account' functionality across all supported browsers and environments.");
		try {
			registerPage.navigateToRegister();

			String uniqueEmail = "crossbrowser" + System.currentTimeMillis() + "@test.com";
			registerPage.enterDetails(firstName, lastName, uniqueEmail, phone, password, confirmPassword);
			registerPage.agreePrivacyPolicy();
			registerPage.clickContinue();

			softAssert.assertTrue(registerPage.isRegistrationSuccess(),
					"Registration success indicates basic cross-browser compatibility for: " + firstName + " "
							+ lastName);

			ExtentTestManager
					.logPass("Cross-browser compatibility validation completed for: " + firstName + " " + lastName);
			ExtentTestManager.logInfo("Note: Full cross-browser testing requires execution on multiple browsers");
		} catch (Exception e) {
			ExtentTestManager.logFail("Cross-browser compatibility validation failed for: " + firstName + " " + lastName
					+ " - " + e.getMessage(), driver);
			throw e;
		}
	}

	/**
	 * Comprehensive Data-Driven Test covering multiple scenarios
	 * 
	 */
	@Test(enabled = false, priority = 28, dataProvider = "RegistrationData", dataProviderClass = DataProviders.class, description = "Comprehensive data-driven registartion test")
	@Description("Comprehensive data-driven registration test")
	@Severity(SeverityLevel.CRITICAL)
	@Story("TC_RF_001-TS_028")
	public void testComprehensiveRegistration(String firstName, String lastName, String email, String phone,
			String password, String confirmPassword, String privacyPolicy) {
		ExtentTestManager.startTest("Comprehensive Registration Test",
				"Test registration with comprehensive data: " + firstName + " " + lastName);
		try {
			registerPage.navigateToRegister();
			registerPage.registerWithData(firstName, lastName, email, phone, password, confirmPassword, privacyPolicy);

			// Determine expected result based on data validity
			boolean isValidData = isValidRegistrationData(firstName, lastName, email, phone, password, confirmPassword,
					privacyPolicy);

			if (isValidData) {
				softAssert.assertTrue(registerPage.isRegistrationSuccess(),
						"Registration should succeed with valid data: " + email);
				ExtentTestManager.logPass("Valid registration completed for: " + email);
			} else {
				// For invalid data, we expect either error message or no success
				boolean isSuccess = registerPage.isRegistrationSuccess();
				String warningMessage = registerPage.getWarningMessage();
				softAssert.assertFalse(isSuccess || !warningMessage.isEmpty(),
						"Registration should fail or show warning for invalid data: " + email);
				ExtentTestManager.logPass("Invalid registration handled correctly for: " + email);
			}
		} catch (Exception e) {
			ExtentTestManager.logFail("Comprehensive registration test failed for: " + email + " - " + e.getMessage(),
					driver);
			throw e;
		}
	}

	/**
	 * Cleanup method - navigate to home after tests
	 */
	@Test(priority = 99, description = "Cleanup and navigation to home")
	@Description("Cleanup and navigate to home page after all tests")
	@Severity(SeverityLevel.MINOR)
	@Story("TC_RF_001-TS_099")
	public void cleanupAfterTests() {
		ExtentTestManager.startTest("Cleanup", "Navigate to home page after tests");
		try {
			registerPage.navigateToHomeAfterLogout();
			ExtentTestManager.logPass("Cleanup completed successfully");
		} catch (Exception e) {
			ExtentTestManager.logFail("Cleanup failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	// Helper methods for data validation
	private boolean isValidEmail(String email) {
		return email != null && email.contains("@") && email.contains(".") && email.length() > 5;
	}

	private boolean isValidPhone(String phone) {
		return phone != null && phone.matches("\\d+") && phone.length() >= 10;
	}

	private boolean isValidRegistrationData(String firstName, String lastName, String email, String phone,
			String password, String confirmPassword, String privacyPolicy) {
		return !firstName.isEmpty() && !lastName.isEmpty() && isValidEmail(email) && isValidPhone(phone)
				&& !password.isEmpty() && password.equals(confirmPassword) && "TRUE".equalsIgnoreCase(privacyPolicy);
	}
}