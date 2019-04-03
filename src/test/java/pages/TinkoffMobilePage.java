package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Enums;

import java.util.concurrent.TimeUnit;

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

    public Select setAndGetSelect(Enums.SelectLists selectLists) {
        return new Select(selectLists,driver);
    }
    public TextInput setAndGetTextInput(Enums.TextInputs textInputs) {
        return new TextInput(textInputs,driver);
    }
    public CheckBox setAndGetCheckBox(Enums.CheckBoxes checkBoxes) {
        return new CheckBox(checkBoxes,driver);
    }
    public Button getButton(){
        return new Button(driver);
    }

    public class Select{
        private String list;
        private final WebDriver driver;
        private final WebDriverWait driverWait;

        private Select(Enums.SelectLists selectLists, WebDriver driver) {
            this.driver = driver;
            changeTargetField(selectLists);
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
        public void changeTargetField(Enums.SelectLists selectedList){
            list = selectedList.getNameOfSelector();
        }
        private boolean hasValue(String value){
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            boolean isExist = driver.findElements(By.xpath(String.format("//*[text() = '%s']",value))).size() > 0;
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            return isExist;
        }     //на форме фиг знает что творится, пытался получить список элементов по xpath //select[@name='неймэлемента']/option
        // но,например, по гигабайтам просто текст не достается и всё, по минутам нормально
    }

    public class CheckBox {
        private String idCheckBox;
        private final WebDriver driver;

        private CheckBox(Enums.CheckBoxes checkBox, WebDriver driver) {
            this.driver = driver;
            changeTargetField(checkBox);
        }
        public void setStatus(boolean status){
            if(getCurrentStatusCheckBox() != status)
                driver.findElement(By.xpath(String.format("%s/parent::div",idCheckBox))).click();
        }

        public boolean getCurrentStatusCheckBox(){
            return Boolean.parseBoolean(driver.findElement(By.xpath(idCheckBox)).getAttribute("checked"));
        }

        public void changeTargetField(Enums.CheckBoxes selectedCheckBox){
            this.idCheckBox = selectedCheckBox.getXpathOfCheckBox();
        }

        public String getTextOfCheckBox(){
            return driver.findElement(By.xpath(String.format("'%s'/ancestor::div[contains(@class,'CheckboxWithDescription')]/label",idCheckBox))).getText();
        }
    }

    public class TextInput{
        private By xPathTextArea;
        private final WebDriver driver;

        private TextInput(Enums.TextInputs textInputs, WebDriver driver) {
            this.driver = driver;
            changeTargetField(textInputs);
        }
        public void setTextInTextArea(String text) {
            if(xPathTextArea.toString().contains("nationality")){
                Select selectNat = setAndGetSelect(Enums.SelectLists.Nationality);
                if(!selectNat.getCurrentValueList().equals("Не имею гражданства РФ"))
                    selectNat.changeList("Не имею гражданства РФ");
            }
            new Actions(driver).moveToElement(driver.findElement(xPathTextArea))
                    .click()
                    .sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE))
                    .sendKeys(text)
                    .perform();
        }
        public void changeTargetField(Enums.TextInputs selectedTextField){
            this.xPathTextArea = By.xpath(String.format("//input[@name = '%s']",selectedTextField.getNameOfTextInputArea()));
        }
        public String getCurrentValue(){
            return driver.findElement(xPathTextArea).getAttribute("value");
        }
    }


    public class Button{
        private final By buttonXpath = By.xpath("//button[contains(@class,'Button__button')]");
        private final WebDriver driver;

        private Button(WebDriver driver) {
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
}
