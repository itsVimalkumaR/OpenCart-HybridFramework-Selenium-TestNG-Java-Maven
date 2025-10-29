/* /src/main/java/com/opencart/utilities/ConfigReader.java*/

package com.opencart.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * ConfigReader loads and provides access to configuration properties.
 */
public class ConfigReader {

    private static final Properties prop = new Properties();
    private static final Logger logger = Logger.getLogger(ConfigReader.class);

    static {
        String configPath = System.getProperty("user.dir") + "/src/test/resources/config.properties";
        try (FileInputStream fis = new FileInputStream(configPath)) {
            prop.load(fis);
            logger.info("[INFO] config.properties loaded successfully from: " + configPath);
        } catch (IOException e) {
            logger.error("[ERROR] Unable to load config.properties file: " + e.getMessage());
            throw new RuntimeException("[FAILED] To load config.properties", e);
        }
    }

    public static String getProperty(String key) {
        String value = prop.getProperty(key);
        if (value == null) {
            logger.warn("[WARNING] Property not found in config.properties: " + key);
            throw new RuntimeException("[FAILED] Property not found: " + key);
        }
        return value.trim();
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(prop.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            logger.warn("[WARNING] Invalid number format for key: " + key + " - using default value: " + defaultValue);
            return defaultValue;
        }
    }

    // ------------------------------------------------
    // Specific Getters
    // ------------------------------------------------
    public static String getUrl() { return getProperty("url"); }
    public static String getDemoUrl() { return getProperty("demo.url"); }
    public static String getRegisterUrl() { return getProperty("register_url"); }
    public static String getBrowser() { return prop.getProperty("browser", "chrome"); }
    public static String getAdminUsername() { return getProperty("admin.username"); }
    public static String getAdminPassword() { return getProperty("admin.password"); }
    public static int getImplicitWait() { return getInt("implicit.wait", 10); }
    public static int getExplicitWait() { return getInt("explicit.wait", 20); }
    public static int getPageLoadTimeout() { return getInt("page.load.timeout", 30); }
    public static int getShortTimeout() { return getInt("short.timeout", 5); }
    public static int getMediumTimeout() { return getInt("medium.timeout", 10); }
    public static int getLongTimeout() { return getInt("long.timeout", 90); }
    public static int getRetryCount() { return getInt("retry.count", 1); }
    public static boolean isHeadless() { return getBoolean("headless"); }
    public static boolean isScreenshotOnFailure() { return getBoolean("screenshot.on.failure"); }
    public static String getScreenshotPath() { return prop.getProperty("screenshot.path", "./screenshots/"); }
    public static String getLogPath() { return prop.getProperty("log.path", "./logs/"); }
    public static String getReportPath() { return prop.getProperty("report.path", "./test-output/"); }

    /**
     * Returns the page title based on the page name.
     * Example: pageTitle.logout=Account Logout
     */
    public static String getPageTitle(String pageName) {
        return prop.getProperty("pageTitle." + pageName);
    }
}
