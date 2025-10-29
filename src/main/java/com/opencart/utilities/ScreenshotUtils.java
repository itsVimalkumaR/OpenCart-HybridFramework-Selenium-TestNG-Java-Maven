/* /OpenCart-HybridFramework-Selenium-TestNG-Java-Maven/src/main/java/com/opencart/utilities/ScreenshotUtils.java */
package com.opencart.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;

/**
 * Screenshot helper that creates reports/screenshots directory if missing.
 */
public class ScreenshotUtils {
	
    public static Logger logger = Logger.getLogger(ScreenshotUtils.class);

	public static String captureScreenshot(WebDriver driver, String screenshotName) {
		if (driver == null)
			return null;
		
		try {
			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String dir = System.getProperty("user.dir") + "/reports/screenshots";
			File outDir = new File(dir);
			if (!outDir.exists())
				outDir.mkdirs();

			String destPath = dir + "/" + screenshotName + "_" + timestamp + ".png";
			File dest = new File(destPath);
			FileUtils.copyFile(src, dest);
			
			logger.info("[INFO] Screenshot saved: " + dest);
			return destPath;
		} catch (WebDriverException | IOException e) {
			logger.error("[FAILED] To capture screenshot: " + e.getMessage(), e);
			return null;
		}
	}
}

