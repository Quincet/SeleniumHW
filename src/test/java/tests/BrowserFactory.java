package tests;

import com.google.common.io.Files;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BrowserFactory {

    public static class MyListener extends AbstractWebDriverEventListener {
        Logger logger = LoggerFactory.getLogger(BrowserFactory.class);
        @Override
        public void beforeFindBy(By by, WebElement element, WebDriver driver) {
            logger.info("Обращение к элементу " + by);
        }
        @Override
        public void afterFindBy(By by, WebElement element, WebDriver driver) {
            logger.info("Найден элемент " + by);
        }
        @Override
        public void onException(Throwable throwable, WebDriver driver) {
            File tmp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File file = new File("target", "sccreen-" + System.currentTimeMillis() + ".png");
            try {
                Files.copy(tmp, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.error(file.getAbsolutePath());
        }
    }
    public static WebDriver getBrowser(String browser){
        switch (browser.toLowerCase()){
            case "firefox":
                System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
                FirefoxOptions ffOpt = new FirefoxOptions();
                ffOpt.addPreference("dom.webnotifications.enabled", false);
                return new FirefoxDriver();
            case "chrome":
                ChromeOptions chromeOpt = new ChromeOptions();
                chromeOpt.addArguments("--disable-notifications");
                Map<String, Object> preferences = new HashMap<>();
                preferences.put("plugins.always_open_pdf_externally", true);
                preferences.put("profile.default_content_settings.popups", 0);
                preferences.put("download.default_directory", System.getProperty("user.dir") + "/src/test/resources/documents/");
                chromeOpt.setExperimentalOption("prefs", preferences);
                chromeOpt.addArguments("--test-type");
                return new ChromeDriver(chromeOpt);
            case "opera":
                OperaOptions options = new OperaOptions();
                options.addArguments("--disable-notifications");
                return new OperaDriver(options);
            default:
                return getBrowser("chrome");
        }
    }
}
