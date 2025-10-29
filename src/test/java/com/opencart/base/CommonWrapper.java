/* /src/test/java/com/opencart/base/CommonWrapper.java */

package com.opencart.base;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.opencart.utilities.ConfigReader;
import com.opencart.utilities.ExtentTestManager;
import com.opencart.utilities.WaitUtils;

/**
 * CommonWrapper
 * 
 * Provides reusable Selenium actions with consistent waits and error handling.
 */

public class CommonWrapper extends BaseTest {

	protected WebDriver driver;
	protected WaitUtils waitUtils;
	protected Actions actions;

	public CommonWrapper(WebDriver driver) {
		this.driver = driver;
		this.waitUtils = new WaitUtils(driver);
		this.actions = new Actions(driver);
	}

	public WebDriverWait getWait(WebDriver driver, int seconds) {
		return new WebDriverWait(driver, Duration.ofSeconds(seconds));
	}

	/* ------- Logging ------- */
	public void logTestStart(String appName) {
		System.out.println("[RUNNING] " + appName + " Application...");
	}

	/* ------- Zoom Controls ------- */
	public void zoomIn() {
		int times = ConfigReader.getImplicitWait();
		for (int i = 0; i < times; i++) {
			actions.keyDown(Keys.CONTROL).sendKeys(Keys.ADD).keyUp(Keys.CONTROL).perform();
		}
	}

	public void zoomOut() {
		int times = ConfigReader.getImplicitWait();
		for (int i = 0; i < times; i++) {
			actions.keyDown(Keys.CONTROL).sendKeys(Keys.SUBTRACT).keyUp(Keys.CONTROL).perform();
		}
	}

	public void resetZoom() {
		actions.keyDown(Keys.CONTROL).sendKeys("0").keyUp(Keys.CONTROL).perform();
	}

	/* ------- Element Clicks ------- */

	// For locator (By)
	public void clickWhenVisible(By locator) {
		try {
			WebElement element = waitUtils.waitForElementToBeVisible(locator);
			waitUtils.waitForElementToBeClickable(element);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		} catch (Exception e) {
			System.out.println("[WARNING] Normal click failed for locator: " + locator + ". Retrying via JS...");
			try {
				WebElement element = driver.findElement(locator);
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			} catch (Exception inner) {
				throw new RuntimeException("[ERROR] Unable to click element: " + locator, inner);
			}
		}
	}

	// For WebElement
	public void clickWhenVisible(WebElement element) {
		waitUtils.waitForElementToBeVisible(element);
		waitUtils.waitForElementToBeClickable(element);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", element);
		((JavascriptExecutor) driver).executeScript("arguments[0].click()", element);
	}

	/* ---------------- Input Actions --------------- */

	public void enterValueWhenVisible(By locator, String value) {
		WebElement element = waitUtils.waitForPresence(locator);
		element.clear();
		element.sendKeys(value);
	}

	public void enterValueWhenVisible(WebElement element, String value) {
		waitUtils.waitForElementToBeVisible(element).clear();
		element.sendKeys(value);
	}

	public void clickAndInputValue(By locator, String value) {
		try {
			WebElement element = waitUtils.waitForElementToBeVisible(locator);
			element.clear();
			element.sendKeys(value);
			ExtentTestManager.logPass("Entered value '" + value + "' into element: " + locator);
		} catch (Exception e) {
			ExtentTestManager.logFail("Unable to enter value into element: " + locator + " - " + e.getMessage(),
					driver);
			throw e;
		}
	}

	public void clickAndInputValue(WebElement element, String value) {
		waitUtils.waitForElementToBeVisible(element);
		element.click();
		element.clear();
		element.sendKeys(value);
	}

	/* ---------------- Text Retrieval --------------- */

	public String getElementText(WebElement element) {
		return waitUtils.waitForElementToBeVisible(element).getText().trim();
	}

	public String getElementText(By locator) {
		return waitUtils.waitForElementToBeVisible(locator).getText().trim();
	}

	public boolean validateFieldWarning(By locator, String expectedMessage) {
		WebElement element = driver.findElement(locator);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		boolean isValid = (boolean) js.executeScript("return arguments[0].checkValidity();", element);
		if (!isValid) {
			String actualMessage = (String) js.executeScript("return arguments[0].validationMessage;", element);
			ExtentTestManager.logPass("Validation Message: " + actualMessage);
			return actualMessage.contains(expectedMessage);
		} else {
			return false;
		}
	}

	// Combine into a reusable method
	public void validateFieldWarningMessage(By locator, String expectedMessage) {
		WebElement element = driver.findElement(locator);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		boolean isValid = (boolean) js.executeScript("return arguments[0].checkValidity();", element);
		if (!isValid) {
			String actualMessage = (String) js.executeScript("return arguments[0].validationMessage;", element);
			Reporter.log("Validation Message: " + actualMessage, true);
		}
	}

	/**
	 * Check if field has validation warning
	 */
	public boolean validateFieldWarningMsg(By locator, String expectedWarning) {
		try {
			String validationMessage = getValidationMessage(locator);
			return validationMessage.contains(expectedWarning);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get validation message for a field
	 */
	public String getValidationMessage(By locator) {
		try {
			WebElement element = driver.findElement(locator);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			return (String) js.executeScript("return arguments[0].validationMessage;", element);
		} catch (Exception e) {
			return "";
		}
	}

	/* ------- Utility Actions ------- */

	public void clearTextField(By locator) {
		WebElement element = driver.findElement(locator);
		element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		element.sendKeys(Keys.BACK_SPACE);
	}

	/**
	 * Clear multiple text fields
	 */
	public void clearTextFields(List<By> locators) {
		for (By locator : locators) {
			try {
				WebElement element = driver.findElement(locator);
				element.clear();
			} catch (Exception e) {
				System.out.println("[WARNING] Could not clear field: " + locator);
			}
		}
	}

	/**
	 * Get placeholder text of an element
	 */
	public String getPlaceholderText(By locator) {
		try {
			WebElement element = waitUtils.waitForElementToBeVisible(locator);
			return element.getAttribute("placeholder");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Check if element has required attribute
	 */
	public boolean isFieldRequired(By locator) {
		try {
			WebElement element = driver.findElement(locator);
			String required = element.getAttribute("required");
			return required != null && (required.equals("true") || required.equals("required"));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Check if asterisk is present near the field label
	 */
	public boolean hasAsterisk(By fieldLocator) {
		try {
			// Find the parent element and check for asterisk in the label
			WebElement field = driver.findElement(fieldLocator);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String script = "return arguments[0].closest('.form-group').innerHTML;";
			String parentHtml = (String) js.executeScript(script, field);
			return parentHtml.contains("*") || parentHtml.contains("required");
		} catch (Exception e) {
			return false;
		}
	}

	public void clickContinue(BaseLocators locators) {
		WebElement continueBtn = waitUtils.waitForPresence(locators.CONTINUE_BUTTON);
		if (continueBtn.isEnabled()) {
			clickWhenVisible(locators.CONTINUE_BUTTON);
			ExtentTestManager.logPass("[Clicked Continue button.");
		} else {
			ExtentTestManager.logInfo("[WARNING] Continue button is disabled, skipping click.");
		}
	}

	/* ------- Page & Scroll Actions ------- */

	public void reloadPage() {
		driver.navigate().refresh();
		WaitUtils.sleep(2000);
	}

	public void scrollToElement(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void clickUsingJS(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	/* ------- Dropdown Validation ------- */
	public void validateDropdownOptions(By locator, List<String> expectedOptions) {
		Select dropdown = new Select(driver.findElement(locator));
		List<WebElement> actualOptions = dropdown.getOptions();

		for (String expected : expectedOptions) {
			boolean found = actualOptions.stream().anyMatch(opt -> opt.getText().trim().equals(expected));
			if (!found) {
				throw new AssertionError("Option not found: " + expected);
			}
		}
		System.out.println("All expected dropdown options are present.");
	}
}