/* /OpenCart-HybridFramework-Selenium-TestNG-Java-Maven/src/main/java/com/opencart/utilities/ExtentManager.java */

package com.opencart.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Handles ExtentReports instance creation and configuration.
 */
public class ExtentManager {

    private static ExtentReports extent;
    private static final Properties config = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/config.properties")) {
            config.load(fis);
            System.out.println("[INFO] config.properties loaded successfully");
        } catch (Exception e) {
            System.err.println("[ERROR] Unable to load config.properties: " + e.getMessage());
        }
    }

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            extent = createInstance();
        }
        return extent;
    }

    private static ExtentReports createInstance() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportDir = System.getProperty("user.dir") + File.separator + "reports";
            new File(reportDir).mkdirs();
//            String reportPath = reportDir + File.separator + "ExtentReport_" + timestamp + ".html";
            String reportPath = reportDir + File.separator + "ExtentReport_Log.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("OpenCart Automation Report");
            spark.config().setReportName("Functional Test Execution Report");
            spark.config().setEncoding("utf-8");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            // System Information
            extent.setSystemInfo("Application URL", config.getProperty("url", "N/A"));
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Tester", "Vimalkumar");
            extent.setSystemInfo("Browser", config.getProperty("browser", "N/A"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));

            System.out.println("[INFO] Extent report initialized: " + reportPath);
            return extent;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to initialize Extent Report - " + e.getMessage());
            return null;
        }
    }
}