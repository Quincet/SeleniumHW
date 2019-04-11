package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

public class TinkoffMobilePage extends Page {

    public TinkoffMobilePage(WebDriver driver) {
        super(driver);
    }

    public TinkoffMobilePage clickMainForms(){
        driver.findElement(By.xpath("//input[@name = 'fio']")).click();
        driver.findElement(By.xpath("//input[@name='phone_mobile']")).click();
        changeNationality();
        driver.findElement(By.xpath("//input[@name='temp_non_resident_nationality']")).click();
        driver.findElement(By.xpath("//input[@name='email']")).click();
        if(!hasElement(By.xpath("//div[contains(@class,'fio')]//div[contains(@class,'error-message')]"))
                | !hasElement(By.xpath("//div[contains(@class,'tel')]//div[contains(@class,'error-message')]"))
                | !hasElement(By.xpath("//input[@name='temp_non_resident_nationality']/following::div[contains(@class,'error-message')]"))){
            clickMainForms(); //иногда некоторые формы просто не кликаются, приходится выполнять проверку на наличии ошибок на странице
        }
        logger.info("Были прокликаны мейн формы на странице");
        return this;
    }
    public TinkoffMobilePage fillMainForms(String fio,String phone,String email,String nat){
        driver.findElement(By.cssSelector("input[name=fio]")).sendKeys(fio);
        driver.findElement(By.cssSelector("input[name=phone_mobile]")).sendKeys(phone);
        driver.findElement(By.cssSelector("input[name=email]")).sendKeys(email);
        changeNationality();
        driver.findElement(By.cssSelector("input[name=temp_non_resident_nationality]")).sendKeys(nat);
        driver.findElement(By.cssSelector("input[name=email]")).click();
        logger.info("Были заполненые мейн формы на странцие");
        return this;
    }
    public TinkoffMobilePage changeRegion(String region) {
        ifHasElementClick(By.xpath("//span[contains(@class,'Region') and contains(@class,'option') and not(contains(@class,'Rejection'))]"));
        try {
            driverWait.until(x -> {
                x.findElement(By.xpath("//div[contains(@class,'region')]//div[@role='presentation']")).click();
                ifHasElementClick(By.xpath(String.format("//div[contains(text(),'%s')]", region)));
                x.findElement(By.xpath("//body")).sendKeys(Keys.ESCAPE);
                return true;
            });
            if (hasElement(By.xpath("//div[@class='ui-form-app-popup-close-button']"))) {
                driver.findElement(By.xpath("//div[@class='ui-form-app-popup-close-button']")).click();
                changeRegion(region);
            }
        } catch (StaleElementReferenceException ex) {
            refresCurrentPage();
            changeRegion(region);
        }
        logger.info("Регион сменился на " + region);
        return this;
    }
    public String getCurrentRegion(){
        return driver.findElement(By.xpath("//div[contains(@class,'Region') and contains(@class,'title')]")).getText();
    }
    public String getCurrentPriceForSim(){
        return driver.findElement(By.cssSelector("h3")).getText();
    }
    private TinkoffMobilePage changeNationality(){
        driver.findElement(By.cssSelector("span.ui-select__value")).click();
        driver.findElement(By.xpath("//span[contains(text(),'Не имею гражданства РФ')]")).click();
        logger.info("Сменили национальность на не из РФ");
        return this;
    }
    public TinkoffMobilePage toSiteTinkoffMobile(){
        goToPage("https://www.tinkoff.ru/mobile-operator/tariffs/");
        logger.info("Перешли на сайт Тинькофф мобайл тарифы");
        return this;
    }
}
