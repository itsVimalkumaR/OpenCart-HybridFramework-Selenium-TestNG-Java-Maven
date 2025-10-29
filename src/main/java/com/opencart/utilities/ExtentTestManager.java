/* /OpenCart-HybridFramework-Selenium-TestNG-Java-Maven/src/main/java/com/opencart/utilities/ExtentTestManager.java */
package com.opencart.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread-safe ExtentTest manager with automatic screenshot logging.
 */
public class ExtentTestManager {

	private static final ExtentReports extent = ExtentManager.getInstance();
	private static final Map<Long, ExtentTest> extentTestMap = new HashMap<>();

	/**
	 * Start a new Extent test for the given test name and description.
	 */
	public static synchronized void startTest(String testName, String description) {
		ExtentTest test = extent.createTest(testName, description);
		extentTestMap.put(Thread.currentThread().getId(), test);
	}

	/**
	 * Returns the ExtentTest instance associated with the current thread.
	 */
	public static synchronized ExtentTest getTest() {
		return extentTestMap.get(Thread.currentThread().getId());
	}

	/**
	 * Flush the report after all tests are done.
	 */
	public static synchronized void flushReport() {
		if (extent != null) {
			try {
				extent.flush();
				System.out.println("[INFO] Extent report flushed successfully.");
			} catch (Exception e) {
				System.err.println("[ERROR] Failed to flush Extent report - " + e.getMessage());
			}
		}
	}

	// ====================== Helper Methods ======================

	/**
	 * Log a passed step.
	 */
	public static void logPass(String message) {
		getTest().log(Status.PASS, message);
	}

	/**
	 * Log a failed step with optional screenshot capture.
	 */
	public static void logFail(String message, WebDriver driver) {
		getTest().log(Status.FAIL, message);
		if (driver != null) {
			String screenshotPath = ScreenshotUtils.captureScreenshot(driver, getTest().getModel().getName());
			if (screenshotPath != null) {
				try {
					getTest().addScreenCaptureFromPath(screenshotPath);
				} catch (Exception e) {
					System.err.println("[ERROR] Failed to attach screenshot: " + e.getMessage());
				}
			}
		}
	}

	/**
	 * Log exception with optional screenshot.
	 */
	public static synchronized void logFail(Throwable throwable, WebDriver driver) {
		getTest().log(Status.FAIL, throwable);
		if (driver != null) {
			String screenshotPath = ScreenshotUtils.captureScreenshot(driver, getTest().getModel().getName());
			if (screenshotPath != null) {
				try {
					getTest().addScreenCaptureFromPath(screenshotPath);
				} catch (Exception e) {
					System.err.println("[ERROR] Failed to attach screenshot: " + e.getMessage());
				}
			}
		}
	}

	/** Capture failure with screenshot (Base64). */
	/*
	 * public static synchronized void logFail(Throwable throwable, WebDriver
	 * driver) { ExtentTest test = getTest(); if (test != null) { try { if (driver
	 * != null) { String base64Screenshot = ((TakesScreenshot)
	 * driver).getScreenshotAs(OutputType.BASE64); test.fail(throwable,
	 * MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).
	 * build()); } else { test.fail(throwable); } } catch (Exception e) {
	 * test.fail("[ERROR] Could not attach screenshot: " + e.getMessage()); } } }
	 */

	/** Optional: capture step screenshots (not only failures). */
	public static void logStepWithScreenshot(String message, WebDriver driver) {
		try {
			String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
			getTest().info(message, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
		} catch (Exception e) {
			getTest().info(message + " [ERROR capturing screenshot: " + e.getMessage() + "]");
		}
	}

	/**
	 * Log an info step.
	 */
	public static void logInfo(String message) {
		getTest().log(Status.INFO, message);
	}

	/**
	 * Log an warning step.
	 */
	public static void logWarn(String message) {
		getTest().log(Status.WARNING, message);
		
	}
}
