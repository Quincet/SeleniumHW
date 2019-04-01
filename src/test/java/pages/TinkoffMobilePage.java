package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

public class TinkoffMobilePage extends Page {

    public TinkoffMobilePage(WebDriver driver) {
        super(driver);
    }

    public void clickMainForms(){
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
    }
    public void fillMainForms(String fio,String phone,String email,String nat){
        driver.findElement(By.cssSelector("input[name=fio]")).sendKeys(fio);
        driver.findElement(By.cssSelector("input[name=phone_mobile]")).sendKeys(phone);
        driver.findElement(By.cssSelector("input[name=email]")).sendKeys(email);
        changeNationality();
        driver.findElement(By.cssSelector("input[name=temp_non_resident_nationality]")).sendKeys(nat);
        driver.findElement(By.cssSelector("input[name=email]")).click();
    }
    public void changeRegion(String region) {
        ifHasElementClick(By.xpath("//span[contains(@class,'Region') and contains(@class,'option') and not(contains(@class,'Rejection'))]"));
        try {
            driverWait.until(x -> {
                x.findElement(By.xpath("//div[contains(@class,'region')]//div[@role='presentation']")).click();
                ifHasElementClick(By.xpath(String.format("//div[contains(text(),'%s')]", region)));
                x.findElement(By.xpath("//body")).sendKeys(Keys.ESCAPE); //бывает форма с выбором региона не закрывается, поэтому на крайний случай нажимаю ESC
                return true;
            });
            if (hasElement(By.xpath("//div[@class='ui-form-app-popup-close-button']"))) {
                driver.findElement(By.xpath("//div[@class='ui-form-app-popup-close-button']")).click();
                changeRegion(region);
            }
        } catch (StaleElementReferenceException ex) { //вываливается ошибка иногда, так как окно с выбором регионов так и не пропадает, приходится использовать замыкание
            refresCurrentPage();
            changeRegion(region);
        }
    }
    public String getCurrentRegion(){
        return driver.findElement(By.xpath("//div[contains(@class,'Region') and contains(@class,'title')]")).getText();
    }
    public String getCurrentPriceForSim(){
        return driver.findElement(By.cssSelector("h3")).getText();
    }
    private void changeNationality(){
        driver.findElement(By.cssSelector("span.ui-select__value")).click();
        driver.findElement(By.xpath("//span[contains(text(),'Не имею гражданства РФ')]")).click();
    }
    public void toSiteTinkoffMobile(){
        goToPage("https://www.tinkoff.ru/mobile-operator/tariffs/");
    }
}
