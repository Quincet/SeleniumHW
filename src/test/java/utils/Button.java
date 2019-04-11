package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Button{
    private final By buttonXpath = By.xpath("//button[contains(@class,'Button__button')]");
    private final WebDriver driver;
    private final Logger logger = LoggerFactory.getLogger(Button.class);

    public Button(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isButtonActive(){
        return driver.findElement(buttonXpath).isEnabled();
    }

    public Button clickButton(){
        if(isButtonActive())
            driver.findElement(buttonXpath).click();
        logger.info("Была нажата мейн кнопка страницы");
        return this;
    }
    public boolean hasButton(){
        return driver.findElement(buttonXpath).isDisplayed();
    }
}