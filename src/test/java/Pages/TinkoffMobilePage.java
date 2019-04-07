package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;

public class TinkoffMobilePage {

    private WebDriver driver;
    private WebDriverWait driverWait;
    @FindBy(name = "fio")
    private WebElement fio;
    @FindBy(name = "phone_mobile")
    private WebElement phoneMobile;
    @FindBy(name = "email")
    private WebElement email;
    @FindBy(name = "temp_non_resident_nationality")
    private WebElement nationality;

    public TinkoffMobilePage(WebDriver driver) {
        this.driver = driver;
        driverWait = new WebDriverWait(driver,5);
        PageFactory.initElements(driver,this);
    }

    public void clickMainForms(){
        ignoringAnimation(x -> {
                    fio.click();
                    phoneMobile.click();
                    email.click();
                    return true;
        });
    }
    public void fillUnvalidValueMainForms(){
        ignoringAnimation(x -> {
                    fio.sendKeys("1578 - ,");
                    phoneMobile.sendKeys("2343251232");
                    email.sendKeys("unvalid");
                    return true;
        });
    }
    public void changeNationality(){
        ignoringAnimation(x -> {
                    driver.findElement(By.xpath("//span[@class='ui-select__value']")).click();
                    driver.findElement(By.xpath("//span[contains(text(),'Не имею гражданства РФ')]")).click();
                    return true;
                });
    }
    public void fillNationality(){
        changeNationality();
        ignoringAnimation(x -> {
                    nationality.sendKeys("nestrana999");
                    email.click();
                    return true;
                });
    }
    public void clickNationality(){
        changeNationality();
        ignoringAnimation(x -> {
            nationality.click();
            email.click();
            return true;
        });
    }
    private void ignoringAnimation(Function<? super WebDriver,Boolean> func){
        driverWait.ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class)
                .until(func);
    }
    public void checkUnvalidValueByFillkMainForms(){
        assertEquals("Допустимо использовать только буквы русского алфавита и дефис", driver.findElement(By.xpath("//div[contains(text(),'Допустимо использовать только буквы русского')]")).getText());
        assertEquals("Код оператора должен начинаться с цифры 9", driver.findElement(By.xpath("//div[contains(text(),'Код оператора')]")).getText());
        assertEquals("Введите корректный адрес эл. почты", driver.findElement(By.xpath("//div[contains(text(),'Введите корректный адрес эл. почты')]")).getText());
        assertEquals("Выберите страну из выпадающего списка", driver.findElement(By.xpath("//div[contains(text(),'Выберите страну из выпадающего списка')]")).getText());
    }
    public void checkUnvalidValueByClickMainForms(){
        assertEquals("Укажите ваше ФИО", driver.findElement(By.xpath("//div[contains(text(),'Укажите ваше ФИО')]")).getText());
        assertEquals("Необходимо указать номер телефона", driver.findElement(By.xpath("//div[contains(text(),'Необходимо указать номер телефона')]")).getText());
        assertEquals("Поле обязательное", driver.findElement(By.xpath("//div[contains(text(),'Поле обязательное')]")).getText());
    }
    public void toSite(){
        driver.navigate().to("https://www.tinkoff.ru/mobile-operator/tariffs/");
    }
}
