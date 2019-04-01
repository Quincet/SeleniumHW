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
        driverWait.until(x->{
            x.findElement(By.xpath("//input[@name = 'fio']")).click();
            x.findElement(By.xpath("//input[@name='phone_mobile']")).click();
            changeNationality();
            x.findElement(By.xpath("//input[@name='temp_non_resident_nationality']")).click();
            x.findElement(By.xpath("//input[@name='email']")).click();
            return true;
        });
    }
    public void fillMainForms(String fio,String phone,String email,String nat){
        driverWait.until(x->{
            x.findElement(By.cssSelector("input[name=fio]")).sendKeys(fio);
            x.findElement(By.cssSelector("input[name=phone_mobile]")).sendKeys(phone);
            x.findElement(By.cssSelector("input[name=email]")).sendKeys(email);
            changeNationality();
            x.findElement(By.cssSelector("input[name=temp_non_resident_nationality]")).sendKeys(nat);
            x.findElement(By.cssSelector("input[name=email]")).click();
            return true;
        });
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
        } catch (StaleElementReferenceException ex) {
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
    public void toSiteTinkoff(){
        goToPage("https://www.tinkoff.ru/mobile-operator/tariffs/");
    }
}
