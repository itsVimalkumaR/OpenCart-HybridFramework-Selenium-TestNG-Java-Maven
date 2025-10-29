/*
 * /src/main/java/com/opencart/utilities/WaitUtils.java 
 * */

package com.opencart.utilities;

import java.time.Duration;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

/**
 * WaitUtils
 *
 * Provides reusable explicit, fluent, and custom wait methods for Selenium
 * Stability.
 */
public class WaitUtils {

    protected WebDriver driver; // Remove static
    protected WebDriverWait wait;
    protected static final Logger logger = Logger.getLogger(WaitUtils.class);

    private static final int DEFAULT_TIMEOUT = ConfigReader.getExplicitWait();
    private static final int DEFAULT_SLEEP_MILLIS = 500;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public WaitUtils(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /* ------------ For By locators (Wait for By) ------------ */

    public WebElement waitForElementToBeVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("[ERROR] Element not visible: " + locator, e);
            throw e;
        }
    }

    public WebElement waitForElementToBeVisible(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementToBeClickable(By locator) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            logger.error("[ERROR] Element not clickable: " + locator, e);
            throw e;
        }
    }

    public WebElement waitForPresence(By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("[ERROR] Element not present in DOM: " + locator, e);
            throw e;
        }
    }

    public boolean waitForTitleContains(String titleFragment) {
        return wait.until(ExpectedConditions.titleContains(titleFragment));
    }

    /* ------------ For WebElement (Wait for WebElement) ------------ */

    public WebElement waitForElementToBeVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForElementToBeClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /* ------------ Timed versions ------------ */

    public WebElement waitForElementToBeClickable(By locator, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForElementToBeClickable(WebElement element, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /* ---------------------- Page & Sleep ---------------------- */

    public void waitForPageToLoad() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
                    .until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
                            .equals("complete"));
            logger.info("[INFO] Page loaded successfully.");
        } catch (Exception e) {
            logger.error("[ERROR] Page did not load properly: " + e.getMessage());
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.getLogger(WaitUtils.class).error("[ERROR] Sleep interrupted: " + e.getMessage());
        }
    }

    public static void sleep() {
        sleep(DEFAULT_SLEEP_MILLIS);
    }

    /* ---------------------- JS Utilities ---------------------- */

    public static void jsClick(WebDriver driver, WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            Logger.getLogger(WaitUtils.class).warn("[Warning] JS click failed: " + e.getMessage());
        }
    }
    
    /* -------------------- Element presence -------------------- */

    public static boolean isElementPresent(WebDriver driver, By locator) {
        try {
            return driver.findElements(locator).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isElementPresent(WebDriver driver, WebElement element) {
        try {
            // For WebElement, we can't use findElements directly
            // Instead, check if element is displayed and enabled
            return element.isDisplayed() && element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // ---------------------- Fluent Wait ----------------------

    public WebElement fluentWait(By locator, int timeoutSeconds, int pollingMillis) {
        try {
            Wait<WebDriver> fluentWait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(timeoutSeconds))
                    .pollingEvery(Duration.ofMillis(pollingMillis)).ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class);
            return fluentWait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("[ERROR] Element not found using fluent wait: " + locator, e);
            throw e;
        }
    }
}