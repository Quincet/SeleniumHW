package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.concurrent.TimeUnit;

public class Picker {
    public TinkoffMobilePage tinkoffMobilePage;
    public WebDriver driver;
    private final String browserName = System.getProperty("browser") == null ? "firefox" : System.getProperty("browser");

    public Picker() {
        driver = new EventFiringWebDriver(getDriver());
        ((EventFiringWebDriver) driver).register(new BrowserFactory.MyListener());
        tinkoffMobilePage = new TinkoffMobilePage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    private WebDriver getDriver() {
        return BrowserFactory.getBrowser(browserName);
    }

    public void quit() {
        driver.quit();
        driver = null;
    }

}
