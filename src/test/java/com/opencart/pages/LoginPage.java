/* /src/test/java/com/opencart/pages/LoginPage.java */
package com.opencart.pages;

import java.util.Arrays;
import java.util.List;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

import com.opencart.base.BaseLocators;
import com.opencart.base.BaseTest;
import com.opencart.base.CommonWrapper;
import com.opencart.utilities.ExtentTestManager;
import com.opencart.utilities.ScreenshotUtils;
import com.opencart.utilities.WaitUtils;

/**
 * LoginPage - Handles navigation and actions on the Login page.
 */
public class LoginPage extends BaseTest {

//    protected WebDriver driver;
//    protected SoftAssert softAssert;
//    protected WaitUtils waitUtils;
//    protected BaseLocators locators;
//    protected CommonWrapper commonWrapper;

	// Constructor
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		this.softAssert = new SoftAssert();
		this.waitUtils = new WaitUtils(driver);
		this.locators = new BaseLocators(driver);
		this.commonWrapper = new CommonWrapper(driver);
//        this.commonWrapper = new CommonWrapper(driver, waitUtils, locators); // FIXED: Pass all parameters
	}

	/* ========== Navigation Methods ========== */

	@Step("Navigate to login page via My Account dropdown")
	public void navigateToLogin() {
		ExtentTestManager.logInfo("[STEP] Navigating to Login page...");
		try {
			String currentURL = driver.getCurrentUrl();
			ExtentTestManager.logInfo("Current page URL: " + currentURL);

			WebElement accountDropdown = waitUtils.waitForElementToBeClickable(locators.MY_ACCOUNT_DROPDOWN);
			accountDropdown.click();

			if (!WaitUtils.isElementPresent(driver, locators.LOGOUT_LINK)) {
				ExtentTestManager.logPass("Logged-out state detected. Opening Login page...");
				waitUtils.waitForElementToBeClickable(locators.LOGIN_LINK).click();
			} else {
				ExtentTestManager.logInfo("Logged-in state detected. Logging out first...");
				waitUtils.waitForElementToBeClickable(locators.LOGOUT_LINK).click();
				WaitUtils.sleep(2000);

				accountDropdown = waitUtils.waitForElementToBeClickable(locators.MY_ACCOUNT_DROPDOWN);
				accountDropdown.click();
				waitUtils.waitForElementToBeClickable(locators.LOGIN_LINK).click();
			}

			ExtentTestManager.logPass("Navigated to Login page successfully.");
		} catch (Exception e) {
			ExtentTestManager.logFail("Unable to navigate to Login page: " + e.getMessage(), driver);
			throw new RuntimeException("Failed to navigate to Login page", e);
		}
	}

	@Step("Navigate to login page from register page")
	public void navigateToLoginFromRegister() {
		try {
			commonWrapper.clickWhenVisible(locators.LOGIN_LINK);
			ExtentTestManager.logPass("Navigated to Login page from Register page.");
		} catch (Exception e) {
			ExtentTestManager.logFail("Unable to navigate to Login page from Register: " + e.getMessage(), driver);
		}
	}

	/* ========== Login Actions ========== */

	@Step("Login with credentials - Email: {email}, Password: {password}")
	public void login(String email, String password) {
	    try {
	        ExtentTestManager.logInfo("[STEP] Logging in with email: " + email);
	        
	        // Add null checks before sending keys
	        if (email != null) {
	            commonWrapper.clickAndInputValue(locators.EMAIL_INPUT_FIELD, email);
	        } else {
	            commonWrapper.clickAndInputValue(locators.EMAIL_INPUT_FIELD, "");
	        }
	        
	        if (password != null) {
	            commonWrapper.clickAndInputValue(locators.PASSWORD_INPUT_FIELD, password);
	        } else {
	            commonWrapper.clickAndInputValue(locators.PASSWORD_INPUT_FIELD, "");
	        }
	        
	        commonWrapper.clickWhenVisible(locators.LOGIN_BUTTON); // Use LOGIN_BUTTON instead of LOGIN_LINK
	        
	        ExtentTestManager.logPass("Login attempt completed.");
	    } catch (Exception e) {
	        ExtentTestManager.logFail("Error during login: " + e.getMessage(), driver);
	        throw new RuntimeException("Error during login", e);
	    }
	}

	@Step("Login using keyboard navigation - Email: {email}, Password: {password}")
	public void loginWithKeyboard(String email, String password) {
		try {
			// Navigate to email field using Tab and enter email
			WebElement emailField = waitUtils.waitForElementToBeVisible(locators.EMAIL_INPUT_FIELD);
			emailField.sendKeys(email);

			// Tab to password field
			emailField.sendKeys(org.openqa.selenium.Keys.TAB);
			WebElement passwordField = driver.switchTo().activeElement();
			passwordField.sendKeys(password);

			// Tab to login button and press Enter
			passwordField.sendKeys(org.openqa.selenium.Keys.TAB);
			driver.switchTo().activeElement().sendKeys(org.openqa.selenium.Keys.ENTER);

			ExtentTestManager.logPass("Keyboard navigation login completed.");
		} catch (Exception e) {
			ExtentTestManager.logFail("Keyboard navigation login failed: " + e.getMessage(), driver);
			throw e;
		}
	}

	@Step("Attempt login multiple times with invalid credentials")
	public void attemptMultipleInvalidLogins(String email, String password, int attempts) {
		for (int i = 1; i <= attempts; i++) {
			ExtentTestManager.logInfo("Login attempt " + i + " of " + attempts);
			login(email, password);
			WaitUtils.sleep(1000);
		}
	}

	/* ========== Validation Methods ========== */

	@Step("Verify login success")
	public boolean isLoginSuccessful() {
		try {
			// Check if we're on account page or if My Account shows logout option
			boolean isSuccess = WaitUtils.isElementPresent(driver, locators.LOGOUT_LINK)
					|| driver.getCurrentUrl().contains("account") || driver.getTitle().contains("Account")
					|| WaitUtils.isElementPresent(driver, locators.ACCOUNT_PAGE_HEADING);
			ScreenshotUtils.capturedScreenshot(driver, "Login Successful screenshot");
			ExtentTestManager.logInfo("Login success status: " + isSuccess);
			return isSuccess;
		} catch (Exception e) {
			ExtentTestManager.logFail("Error checking login success: " + e.getMessage(), driver);
			return false;
		}
	}

	@Step("Verify login failure warning message")
	public boolean isLoginWarningDisplayed() {
		try {
			WebElement warning = waitUtils.waitForElementToBeVisible(locators.WARNING_ALERT, 5);
			boolean isDisplayed = warning.isDisplayed();
			String warningText = warning.getText();
			ExtentTestManager.logInfo("Warning displayed: " + isDisplayed + ", Text: " + warningText);
			return isDisplayed && warningText.contains("No match for E-Mail Address and/or Password");
		} catch (Exception e) {
			ExtentTestManager.logInfo("No warning message displayed");
			return false;
		}
	}

	@Step("Get login warning message text")
	public String getWarningMessage() {
		try {
			WebElement warning = waitUtils.waitForElementToBeVisible(locators.WARNING_ALERT, 5);
			String message = warning.getText().trim();
			ExtentTestManager.logInfo("Login warning message: " + message);
			return message;
		} catch (Exception e) {
			ExtentTestManager.logInfo("No warning message displayed");
			return "";
		}
	}

	@Step("Verify account locked warning message")
	public boolean isAccountLockedWarningDisplayed() {
		try {
			WebElement warning = waitUtils.waitForElementToBeVisible(locators.WARNING_ALERT, 5);
			String warningText = warning.getText();
			boolean isLocked = warningText.contains("exceeded") || warningText.contains("attempt")
					|| warningText.contains("hour");
			ExtentTestManager.logInfo("Account locked warning: " + isLocked);
			return isLocked;
		} catch (Exception e) {
			return false;
		}
	}

	@Step("Verify user is on login page")
	public boolean isOnLoginPage() {
		try {
			boolean isLoginPage = driver.getCurrentUrl().contains("login")
					&& WaitUtils.isElementPresent(driver, locators.LOGIN_LINK);
			ExtentTestManager.logInfo("On login page: " + isLoginPage);
			return isLoginPage;
		} catch (Exception e) {
			return false;
		}
	}

	@Step("Verify account page is displayed after login")
	public boolean isAccountPageDisplayed() {
		try {
			boolean isDisplayed = WaitUtils.isElementPresent(driver, locators.ACCOUNT_PAGE_HEADING);
			ExtentTestManager.logInfo("Account page displayed: " + isDisplayed);
			return isDisplayed;
		} catch (Exception e) {
			ExtentTestManager.logFail("Error verifying account page: " + e.getMessage(), driver);
			return false;
		}
	}

	@Step("Verify placeholder texts")
	public void validatePlaceholderTexts() {
		String emailPlaceholder = commonWrapper.getPlaceholderText(locators.EMAIL_INPUT_FIELD);
		String passwordPlaceholder = commonWrapper.getPlaceholderText(locators.PASSWORD_INPUT_FIELD);

		ExtentTestManager.logInfo("Email placeholder: " + emailPlaceholder);
		ExtentTestManager.logInfo("Password placeholder: " + passwordPlaceholder);

		softAssert.assertEquals(emailPlaceholder, "E-Mail Address", "Email placeholder should be 'E-Mail Address'");
		softAssert.assertEquals(passwordPlaceholder, "Password", "Password placeholder should be 'Password'");
	}

	@Step("Get placeholder text from Email input field")
	public String getEmailPlaceholder() {
		try {
			String placeholder = commonWrapper.getPlaceholderText(locators.EMAIL_INPUT_FIELD);
			ExtentTestManager.logInfo("Email placeholder: " + placeholder);
			return placeholder;
		} catch (Exception e) {
			ExtentTestManager.logFail("Unable to get Email placeholder: " + e.getMessage(), driver);
			return "";
		}
	}

	@Step("Get placeholder text from Password input field")
	public String getPasswordPlaceholder() {
		try {
			String placeholder = commonWrapper.getPlaceholderText(locators.PASSWORD_INPUT_FIELD);
			ExtentTestManager.logInfo("Password placeholder: " + placeholder);
			return placeholder;
		} catch (Exception e) {
			ExtentTestManager.logFail("Unable to get Password placeholder: " + e.getMessage(), driver);
			return "";
		}
	}

	@Step("Verify password field is masked")
	public boolean isPasswordMasked() {
		try {
			WebElement passwordField = driver.findElement(locators.PASSWORD_INPUT_FIELD);
			String fieldType = passwordField.getAttribute("type");
			boolean isMasked = "password".equals(fieldType);
			ExtentTestManager.logInfo("Password field type: " + fieldType + ", Masked: " + isMasked);
			return isMasked;
		} catch (Exception e) {
			ExtentTestManager.logFail("Error checking password masking: " + e.getMessage(), driver);
			return false;
		}
	}

	@Step("Get password field type (masking validation)")
	public String getPasswordFieldType() {
		try {
			WebElement passwordField = waitUtils.waitForElementToBeVisible(locators.PASSWORD_INPUT_FIELD);
			String fieldType = passwordField.getAttribute("type");
			ExtentTestManager.logInfo("Password field type: " + fieldType);
			return fieldType;
		} catch (Exception e) {
			ExtentTestManager.logFail("Error checking password field type: " + e.getMessage(), driver);
			return "";
		}
	}

	@Step("Verify password copying is restricted")
	public boolean isPasswordCopyRestricted() {
		try {
			WebElement passwordField = driver.findElement(locators.PASSWORD_INPUT_FIELD);

			// Try to copy using JavaScript
			String script = "var element = arguments[0];" + "element.select();"
					+ "return document.execCommand('copy');";
			Boolean copyResult = (Boolean) ((JavascriptExecutor) driver).executeScript(script, passwordField);

			ExtentTestManager.logInfo("Copy command result: " + copyResult);
			return !copyResult; // Should return false if copy is restricted
		} catch (Exception e) {
			ExtentTestManager.logInfo("Copy restriction check completed");
			return true;
		}
	}

	@Step("Verify password not visible in page source")
	public boolean isPasswordHiddenInSource(String password) {
		try {
			String pageSource = driver.getPageSource();
			boolean passwordVisible = pageSource.contains(password);
			ExtentTestManager.logInfo("Password visible in source: " + passwordVisible);
			return !passwordVisible;
		} catch (Exception e) {
			ExtentTestManager.logFail("Error checking page source: " + e.getMessage(), driver);
			return false;
		}
	}

	@Step("Verify forgotten password link is displayed")
	public boolean isForgottenPasswordDisplayed() {
		try {
			boolean isDisplayed = WaitUtils.isElementPresent(driver, locators.FORGOTTEN_PASSWORD_LINK);
			ExtentTestManager.logInfo("Forgotten password link displayed: " + isDisplayed);
			return isDisplayed;
		} catch (Exception e) {
			ExtentTestManager.logFail("Error verifying forgotten password link: " + e.getMessage(), driver);
			return false;
		}
	}

	@Step("Navigate to forgotten password page")
	public void navigateToForgottenPassword() {
		try {
			commonWrapper.clickWhenVisible(locators.FORGOTTEN_PASSWORD_LINK);
			ExtentTestManager.logPass("Navigated to Forgotten Password page.");
		} catch (Exception e) {
			ExtentTestManager.logFail("Unable to navigate to Forgotten Password: " + e.getMessage(), driver);
		}
	}

	@Step("Click forgotten password link")
	public void clickForgottenPassword() {
		try {
			commonWrapper.clickWhenVisible(locators.FORGOTTEN_PASSWORD_LINK);
			ExtentTestManager.logPass("Clicked on Forgotten Password link successfully.");
		} catch (Exception e) {
			ExtentTestManager.logFail("Unable to click Forgotten Password link: " + e.getMessage(), driver);
		}
	}

	@Step("Verify on forgotten password page")
	public boolean isOnForgottenPasswordPage() {
		try {
			return driver.getCurrentUrl().contains("forgotten")
					&& WaitUtils.isElementPresent(driver, By.xpath("//h1[contains(text(), 'Forgot Your Password?')]"));
		} catch (Exception e) {
			return false;
		}
	}

	@Step("Navigate to register page from login")
	public void navigateToRegisterFromLogin() {
		try {
			commonWrapper.clickWhenVisible(locators.CONTINUE_BUTTON); // Continue button under New Customer section
			ExtentTestManager.logPass("Navigated to Register page from Login.");
		} catch (Exception e) {
			ExtentTestManager.logFail("Unable to navigate to Register from Login: " + e.getMessage(), driver);
		}
	}

	@Step("Logout user")
	public void logout() {
		try {
			commonWrapper.clickWhenVisible(locators.MY_ACCOUNT_DROPDOWN);
			WaitUtils.sleep(1000);
			commonWrapper.clickWhenVisible(locators.LOGOUT_LINK);
			WaitUtils.sleep(2000);
			ScreenshotUtils.capturedScreenshot(driver, "Logged out Successful screenshot");
			ExtentTestManager.logPass("User logged out successfully.");
		} catch (Exception e) {
			ExtentTestManager.logFail("Error during logout: " + e.getMessage(), driver);
		}
	}

	@Step("Verify page metadata")
	public void validatePageMetadata() {
		String currentUrl = driver.getCurrentUrl();
		String pageTitle = driver.getTitle();

		ExtentTestManager.logInfo("Current URL: " + currentUrl);
		ExtentTestManager.logInfo("Page Title: " + pageTitle);

		softAssert.assertTrue(currentUrl.contains("login"), "URL should contain 'login'");
		softAssert.assertTrue(pageTitle.contains("Login") || pageTitle.contains("Account Login"),
				"Page title should contain 'Login'");

		// Check breadcrumb
		try {
			WebElement breadcrumb = waitUtils.waitForElementToBeVisible(By.xpath("//ul[@class='breadcrumb']"));
			ExtentTestManager.logInfo("Breadcrumb present: " + breadcrumb.isDisplayed());
		} catch (Exception e) {
			ExtentTestManager.logInfo("Breadcrumb not found: " + e.getMessage());
		}
	}

	@Step("Verify UI layout elements")
	public void validateUILayout() {
		List<By> uiElements = Arrays.asList(locators.MY_ACCOUNT_DROPDOWN, locators.EMAIL_INPUT_FIELD,
				locators.PASSWORD_INPUT_FIELD, locators.LOGIN_LINK, locators.FORGOTTEN_PASSWORD_LINK,
				locators.CONTINUE_BUTTON);

		for (By element : uiElements) {
			try {
				WebElement webElement = waitUtils.waitForElementToBeVisible(element, 3);
				boolean isDisplayed = webElement.isDisplayed();
				boolean isEnabled = webElement.isEnabled();
				ExtentTestManager
						.logInfo("Element " + element + " - Displayed: " + isDisplayed + ", Enabled: " + isEnabled);
			} catch (Exception e) {
				ExtentTestManager.logInfo("Element " + element + " not found or visible: " + e.getMessage());
			}
		}
	}

	@Step("Use browser back button")
	public void useBrowserBackButton() {
		try {
			driver.navigate().back();
			WaitUtils.sleep(2000);
			ExtentTestManager.logPass("Browser back button used.");
		} catch (Exception e) {
			ExtentTestManager.logFail("Error using browser back button: " + e.getMessage(), driver);
		}
	}

	@Step("Clear browser data")
	public void clearBrowserData() {
		try {
			driver.manage().deleteAllCookies();
			ExtentTestManager.logPass("Browser data cleared.");
		} catch (Exception e) {
			ExtentTestManager.logFail("Error clearing browser data: " + e.getMessage(), driver);
		}
	}

	/* ========== Combined Validations ========== */

	@Step("Validate placeholders for email and password fields")
	public void validatePlaceholders() {
		String emailPlaceholder = getEmailPlaceholder();
		String passwordPlaceholder = getPasswordPlaceholder();

		softAssert.assertEquals(emailPlaceholder, "E-Mail Address", "Incorrect Email placeholder.");
		softAssert.assertEquals(passwordPlaceholder, "Password", "Incorrect Password placeholder.");
	}

	@Step("Validate password masking is enabled")
	public boolean validatePasswordMasking() {
		String fieldType = getPasswordFieldType();
		boolean isMasked = "password".equalsIgnoreCase(fieldType);
		ExtentTestManager.logInfo("Password masked: " + isMasked);
		return isMasked;
	}

	@Step("Assert all validations")
	public void assertAll() {
		softAssert.assertAll();
	}
}