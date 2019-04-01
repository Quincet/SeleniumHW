package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Select{
    private final String list;
    private final WebDriver driver;
    private final WebDriverWait driverWait;

    public Select(Enums.SelectLists list, WebDriver driver) {
        this.driver = driver;
        this.list = list.getNameOfSelector();
        driverWait = new WebDriverWait(driver,10);
    }
    private void openList(){
        driverWait.until(x->{
            x.findElement(By.xpath(String.format("//select[@name='%s']/parent::div/div",list))).click();
            return true;
        });
    }
    public void changeList(String newValue){
        openList();
        if(hasValue(newValue)) {
            driverWait.until(x -> {
                x.findElement(By.xpath(String.format("//span[text()= '%s' and not(@class = 'ui-select__value')]", newValue))).click();
                return true;
            });
        }else{
            System.out.println("Не имеется такого значения");
            openList();
        }
    }
    public String getCurrentValueList(){
        return driverWait.until(x-> x.findElement(By.xpath(String.format("//select[@name='%s']/parent::div//span[contains(@class,'title-flex-text') or contains(@class,'value')]",list))).getText());
    }
    public Select changeTargetField(Enums.SelectLists selectedList){
        return new Select(selectedList,driver);
    }
    private boolean hasValue(String value){
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        boolean isExist = driver.findElements(By.xpath(String.format("//*[text() = '%s']",value))).size() > 0;
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return isExist;
    }     //на форме фиг знает что творится, пытался получить список элементов по xpath //select[@name='неймэлемента']/option
            // но,например, по гигабайтам просто текст не достается и всё, по минутам нормально
}
