package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Button{
    private final By buttonXpath = By.xpath("//button[contains(@class,'Button__button')]");
    private final WebDriver driver;

    public Button(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isButtonActive(){
        return driver.findElement(buttonXpath).isEnabled();
    }

    public void clickButton(){
        if(isButtonActive())
            driver.findElement(buttonXpath).click();
    }
    public boolean hasButton(){
        return driver.findElement(buttonXpath).isDisplayed();
    }
}