package app;

import lombok.Value;
import pages.GooglePage;
import pages.TinkoffDocuments;
import pages.TinkoffMobilePage;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.BrowserFactory;
import java.util.concurrent.TimeUnit;

@Value
public class FactoryPages {
    @Getter private TinkoffMobilePage tinkoffMobilePage;
    @Getter private GooglePage googlePage;
    @Getter private TinkoffDocuments tinkoffDocuments;
    private WebDriver driver;
    private WebDriverWait driverWait;
    private String browserName = System.getProperty("browser") == null ? "chrome" : System.getProperty("browser");

    public FactoryPages() {
        driver = new EventFiringWebDriver(getNewDriver());
        ((EventFiringWebDriver) driver).register(new BrowserFactory.MyListener());
        driverWait = new WebDriverWait(driver,10);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        tinkoffMobilePage = new TinkoffMobilePage(driver);
        googlePage = new GooglePage(driver);
        tinkoffDocuments = new TinkoffDocuments(driver);
    }

    private WebDriver getNewDriver() {
        return BrowserFactory.getBrowser(browserName);
    }

    public void quit() {
        driver.quit();
    }
}
