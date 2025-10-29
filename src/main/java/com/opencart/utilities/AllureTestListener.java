package com.opencart.utilities;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;

public class AllureTestListener implements ITestListener {

	@Override
    public void onTestFailure(ITestResult result) {
        // Capture screenshot on failure
        Object currentClass = result.getInstance();
        try {
            // Use reflection to get driver from test class
            WebDriver driver = (WebDriver) currentClass.getClass()
                    .getMethod("getDriver")
                    .invoke(currentClass);
            
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Screenshot on Failure", "image/png", 
                    new ByteArrayInputStream(screenshot), ".png");
            }
        } catch (Exception e) {
            Allure.addAttachment("Screenshot Error", "text/plain", 
                "Failed to capture screenshot: " + e.getMessage());
        }

        // Add error details
        if (result.getThrowable() != null) {
            Allure.addAttachment("Stack Trace", "text/plain", 
                result.getThrowable().toString());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        Allure.addAttachment("Test Duration", "text/plain", 
            duration + "ms");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Allure.addAttachment("Skipped Test", "text/plain", 
            "Test was skipped: " + result.getName());
    }

    /**
     * Universal screenshot capture using reflection
     */
    private void captureScreenshot(ITestResult result, String attachmentName) {
        try {
            Object testInstance = result.getInstance();
            WebDriver driver = getDriverFromTestInstance(testInstance);
            
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(attachmentName, "image/png", 
                    new ByteArrayInputStream(screenshot), ".png");
            } else {
                Allure.addAttachment("Driver Status", "text/plain", 
                    "WebDriver not available for screenshot");
            }
        } catch (Exception e) {
            Allure.addAttachment("Screenshot Error", "text/plain", 
                "Failed to capture screenshot: " + e.getMessage());
        }
    }

    /**
     * Get WebDriver from test instance using reflection
     */
    private WebDriver getDriverFromTestInstance(Object testInstance) {
        try {
            // Try to get driver using getDriver() method
            Method getDriverMethod = testInstance.getClass().getMethod("getDriver");
            Object driverObj = getDriverMethod.invoke(testInstance);
            
            if (driverObj instanceof WebDriver) {
                return (WebDriver) driverObj;
            }
        } catch (NoSuchMethodException e) {
            // Try alternative method names
            try {
                Method getDriverMethod = testInstance.getClass().getMethod("getWebDriver");
                Object driverObj = getDriverMethod.invoke(testInstance);
                
                if (driverObj instanceof WebDriver) {
                    return (WebDriver) driverObj;
                }
            } catch (Exception e2) {
                // Ignore and try next approach
            }
        } catch (Exception e) {
            // Ignore and try field access
        }
        
        // Try to access driver field directly
        try {
            java.lang.reflect.Field driverField = testInstance.getClass().getDeclaredField("driver");
            driverField.setAccessible(true);
            Object driverObj = driverField.get(testInstance);
            
            if (driverObj instanceof WebDriver) {
                return (WebDriver) driverObj;
            }
        } catch (Exception e) {
            // Could not access driver field
        }
        
        return null;
    }

    private String getFullStackTrace(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.toString()).append("\n");
        
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        
        Throwable cause = throwable.getCause();
        if (cause != null) {
            sb.append("Caused by: ").append(cause.toString()).append("\n");
            for (StackTraceElement element : cause.getStackTrace()) {
                sb.append("\tat ").append(element.toString()).append("\n");
            }
        }
        
        return sb.toString();
    }
}