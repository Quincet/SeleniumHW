package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class TextInput{
    private final By xPathTextArea;
    private final WebDriver driver;

    public TextInput(Enums.TextInputs textInputs, WebDriver driver) {
        this.driver = driver;
        xPathTextArea = By.xpath(String.format("//input[@name = '%s']",textInputs.getNameOfTextInputArea()));
    }
    public void setTextInTextArea(String text) {
        if(xPathTextArea.toString().contains("nationality")){
            Select selectNat = new Select(Enums.SelectLists.Nationality,driver);
            if(!selectNat.getCurrentValueList().equals("Не имею гражданства РФ"))
                selectNat.changeList("Не имею гражданства РФ");
        }
        new Actions(driver).moveToElement(driver.findElement(xPathTextArea))
                .click()
                .sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE))
                .sendKeys(text)
                .perform();
    }
    public TextInput changeTargetField(Enums.TextInputs selectedTextField){
        return new TextInput(selectedTextField,driver);
    }
    public String getCurrentValue(){
        return driver.findElement(xPathTextArea).getAttribute("value");
    }
}