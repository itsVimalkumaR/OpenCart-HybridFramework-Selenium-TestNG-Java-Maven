/* /OpenCart-HybridFramework-Selenium-TestNG-Java-Maven/src/main/java/com/opencart/utilities/WebDriverFactory.java */

package com.opencart.utilities;

import java.time.Duration;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverFactory {

    private static final Logger logger = Logger.getLogger(WebDriverFactory.class);
    private static WebDriver driver;

    public static WebDriver createDriver(String browserName) {
        if (driver != null) return driver;

        browserName = (browserName == null || browserName.isEmpty()) ? ConfigReader.getBrowser() : browserName;
        boolean headless = ConfigReader.isHeadless();

        try {
            ConfigReader config = new ConfigReader();

            switch (browserName.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOpts = new ChromeOptions();
                    chromeOpts.addArguments("--start-maximized", "--remote-allow-origins=*");
                    if (headless) chromeOpts.addArguments("--headless=new", "--disable-gpu");
                    driver = new ChromeDriver(chromeOpts);
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions ffOpts = new FirefoxOptions();
                    if (headless) ffOpts.addArguments("--headless");
                    driver = new FirefoxDriver(ffOpts);
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOpts = new EdgeOptions();
                    if (headless) edgeOpts.addArguments("--headless=new");
                    driver = new EdgeDriver(edgeOpts);
                    break;

                default:
                    throw new IllegalArgumentException("[WARNING] Invalid browser: " + browserName);
            }

            driver.manage().deleteAllCookies();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
            driver.get(ConfigReader.getUrl());
            
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
            
            logger.info("[INFO] '" + browserName + "' launched and navigated to: " + ConfigReader.getUrl());
            
        } catch (Exception e) {
            logger.error("[FAILED] WebDriver initialization failed: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return driver;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            logger.info("[INFO] WebDriver closed successfully.");
        }
    }
}
