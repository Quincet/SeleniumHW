package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckBox {
    private final String idCheckBox;
    private final WebDriver driver;

    public CheckBox(Enums.CheckBoxes checkBox, WebDriver driver) {
        this.driver = driver;
        this.idCheckBox = checkBox.getXpathOfCheckBox();
    }
    public void setStatus(boolean status){
        if(getCurrentStatusCheckBox() != status)
            driver.findElement(By.xpath(String.format("%s/parent::div",idCheckBox))).click();
    }

    public boolean getCurrentStatusCheckBox(){
        return Boolean.parseBoolean(driver.findElement(By.xpath(idCheckBox)).getAttribute("checked"));
    }

    public CheckBox changeTargetField(Enums.CheckBoxes selectedCheckBox){
        return new CheckBox(selectedCheckBox,driver);
    }

    public String getTextOfCheckBox(){
        return driver.findElement(By.xpath(String.format("'%s'/ancestor::div[contains(@class,'CheckboxWithDescription')]/label",idCheckBox))).getText();
    }
}