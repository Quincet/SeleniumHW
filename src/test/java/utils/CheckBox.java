package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckBox {
    private String idCheckBox;
    private final WebDriver driver;
    private final Logger logger = LoggerFactory.getLogger(CheckBox.class);

    public CheckBox(Enums.CheckBoxes checkBox, WebDriver driver) {
        this.driver = driver;
        changeTargetField(checkBox);
    }
    public CheckBox setStatus(boolean status){
        if(getCurrentStatusCheckBox() != status)
            driver.findElement(By.xpath(String.format("%s/parent::div",idCheckBox))).click();
        logger.info(String.format("У чек бокса %s был поставлен статус %s",getTextOfCheckBox(),status));
        return this;
    }

    public boolean getCurrentStatusCheckBox(){
        return Boolean.parseBoolean(driver.findElement(By.xpath(idCheckBox)).getAttribute("checked"));
    }

    public CheckBox changeTargetField(Enums.CheckBoxes selectedCheckBox){
        this.idCheckBox = selectedCheckBox.getXpathOfCheckBox();
        return this;
    }

    public String getTextOfCheckBox(){
        return driver.findElement(By.xpath(String.format("%s/ancestor::div[contains(@class,'CheckboxWithDescription')]/label",idCheckBox))).getText();
    }
}