/* /OpenCart-HybridFramework-Selenium-TestNG-Java-Maven/src/test/java/com/opencart/utilities/TestListener.java  */

package com.opencart.utilities;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.openqa.selenium.WebDriver;

/**
 * TestNG Listener that integrates with ExtentReports.
 * Automatically attaches screenshots for failed tests.
 */
public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("[INFO] Test Suite started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("[INFO] Test Suite finished: " + context.getName());
        ExtentTestManager.flushReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTestManager.startTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
        System.out.println("[INFO] Test started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().pass("✅ Test Passed");
        System.out.println("[INFO] Test passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Object testClass = result.getInstance();
        WebDriver driver = null;
        try {
            driver = (WebDriver) testClass.getClass().getDeclaredField("driver").get(testClass);
        } catch (Exception e) {
            System.err.println("[WARNING] Unable to access WebDriver: " + e.getMessage());
        }

        ExtentTestManager.logFail(result.getThrowable(), driver);
        System.out.println("[INFO] Test failed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTestManager.getTest().skip("⚠️ Test Skipped");
        System.out.println("[INFO] Test skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not used
    }
}
