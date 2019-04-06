package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Page {
    protected WebDriver driver;
    protected WebDriverWait driverWait;
    protected Logger logger = LoggerFactory.getLogger(Page.class);

    public Page(WebDriver driver) {
        this.driver = driver;
        this.driverWait = new WebDriverWait(driver,10);
    }
    public boolean isLoadedByTitle(String title) {
        return driverWait.until(x -> x.getTitle().contains(title));
    }
    public boolean isUrlEqualsTo(String url){
        return driverWait.until(x -> x.getCurrentUrl().equals(url));
    }
    public void switchToWindow(String windowName){
        driverWait.until(x -> {
            boolean find = false;
            for (String title : driver.getWindowHandles()) {
                System.out.println(title);
                driver.switchTo().window(title);
                if(x.getTitle().equals(windowName)){
                    find = true;
                    break;
                }
            }
            return find;
        });
    }

    public void ignoringAnimation(Function<? super WebDriver,Boolean> func){
        driverWait.ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class)
                .until(func);
    }

    public void closeTab(String tabName){
        String currentTab = driver.getTitle();
        switchToWindow(tabName);
        driver.close();
        switchToWindow(currentTab);
    }

    public void closeOtherTabs(){
        String currentTitleTab = driver.getTitle();
        driverWait.until(x->{
            for (String window : driver.getWindowHandles()) {
                driver.switchTo().window(window);
                if(driver.getTitle().equals(currentTitleTab))
                    continue;
                driver.close();
            }
            return true;
        });
        switchToWindow(currentTitleTab);
    }

    public void closeSimilarTabs(String similarNameOfTitle){
        String currentTitleTab = driver.getTitle();
        driverWait.until(x->{
            for (String window : driver.getWindowHandles()) {
                driver.switchTo().window(window);
                if(driver.getTitle().toLowerCase().contains(similarNameOfTitle.toLowerCase()))
                    driver.close();
                if(driver.getTitle().equals(currentTitleTab))
                    continue;
            }
            return true;
        });
        switchToWindow(currentTitleTab);
    }

    public void goToPage(String url) {
        driver.navigate().to(url);
    }

    public boolean hasElement(By by){
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        boolean isExist = driver.findElements(by).size() > 0;
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return isExist;
    }
    public void ifHasElementClick(By by){
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        if(driver.findElements(by).size() > 0)
            driverWait.until(x->{
                x.findElements(by).get(0).click();
                return true;
            });
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void refreshCurrentPage(){
        driver.navigate().refresh();
    }

    public void closeCurrentTab(){
        driver.close();
        logger.info("Закрыта активная вкладка");
    }

    public void switchToMainTab(){
        driver.switchTo().window(driver.getWindowHandles().iterator().next());
    }

}
