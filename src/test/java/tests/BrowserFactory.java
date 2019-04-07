package tests;

import com.google.common.io.Files;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
        String filePath = (System.getProperty("user.dir") + "\\src\\test\\resources\\").replace("\\",File.separator);
        switch (browser.toLowerCase()){
            case "chrome":
                ChromeOptions chromeOpt = new ChromeOptions();
                chromeOpt.addArguments("--disable-notifications");
                Map<String, Object> preferences = new HashMap<>();
                preferences.put("plugins.always_open_pdf_externally", true);
                preferences.put("profile.default_content_settings.popups", 0);
                preferences.put("download.prompt_for_download",false);
                preferences.put("download.default_directory", filePath);
                chromeOpt.setExperimentalOption("prefs", preferences);
                chromeOpt.addArguments("--test-type");
                return new ChromeDriver(chromeOpt);
            default:
                return getBrowser("chrome");
        }
    }
}
