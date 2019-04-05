package app;

import pages.GooglePage;
import pages.TinkoffDocuments;
import pages.TinkoffMobilePage;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.BrowserFactory;

import java.util.concurrent.TimeUnit;

public class Picker {
    @Getter private final TinkoffMobilePage tinkoffMobilePage;
    @Getter private final GooglePage googlePage;
    @Getter private final TinkoffDocuments tinkoffDocuments;
    @Getter private WebDriver driver;
    @Getter private final WebDriverWait driverWait;

    public Picker() {
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
        return BrowserFactory.getBrowser();
    }

    public void quit() {
        driver.quit();
        driver = null;
    }
}
