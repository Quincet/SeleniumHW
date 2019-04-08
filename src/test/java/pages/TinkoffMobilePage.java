package pages;

import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.Enums;

@Getter
public class TinkoffMobilePage extends Page {
    @FindBy(name = "fio")
    @CacheLookup private WebElement inputFieldFio;
    @FindBy(name = "phone_mobile")
    @CacheLookup private WebElement inputFieldTel;
    @FindBy(name = "email")
    @CacheLookup private WebElement inputFieldMail;
    @FindBy(name = "temp_non_resident_nationality")
    @CacheLookup private WebElement inputFieldNatyonal;
    @FindBy(xpath = "//div[contains(@class,'fio')]//div[contains(@class,'error-message')]")
    private WebElement errorMessageFio;
    @FindBy(xpath = "//div[contains(@class,'tel')]//div[contains(@class,'error-message')]")
    private WebElement errorMessageTel;
    @FindBy(xpath = "//input[@name='email']/following::div[contains(@class,'error-message')]")
    private WebElement errorMessageMail;
    @FindBy(xpath = "//input[contains(@name,'nationality')]/following::div[contains(@class,'error-message')]")
    private WebElement errorMessageNatyonal;
    @FindBy(css = "div[class *=FIO] [class *=error-message]")
    private WebElement cssErrorMessageFio;
    @FindBy(css = "div[class *=tel] [class *=error-message]")
    private WebElement cssErrorMessageTel;
    @FindBy(xpath = "//div[contains(@class,'Region') and contains(@class,'title')]")
    private WebElement buttonChangeRegion;
    @FindBy(css = "h3")
    private WebElement currentPriceForSim;
    @FindBy(css = "span.ui-select__value")
    private WebElement selectFieldNatyonal;
    @FindBy(xpath = "//button[contains(@class,'Button__button')]")
    private WebElement mainYellowButton;

    public TinkoffMobilePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver,this);
    }

    public TinkoffMobilePage clickMainForms(){
        inputFieldFio.click();
        inputFieldTel.click();
        changeNationality();
        inputFieldNatyonal.click();
        inputFieldMail.click();
        return this;
    }
    public TinkoffMobilePage fillMainForms(String fio,String phone,String email,String nat){
        driver.findElement(By.cssSelector("input[name=fio]")).sendKeys(fio);
        driver.findElement(By.cssSelector("input[name=phone_mobile]")).sendKeys(phone);
        driver.findElement(By.cssSelector("input[name=email]")).sendKeys(email);
        changeNationality();
        driver.findElement(By.cssSelector("input[name=temp_non_resident_nationality]")).sendKeys(nat);
        driver.findElement(By.cssSelector("input[name=email]")).click();
        return this;
    }
    public TinkoffMobilePage changeRegion(String region) {
        ifHasElementClick(By.xpath("//span[contains(@class,'Region') and contains(@class,'option') and not(contains(@class,'Rejection'))]"));
        try {
            driverWait.until(x -> {
                buttonChangeRegion.click();
                ifHasElementClick(By.xpath(String.format("//div[contains(text(),'%s')]", region)));
                return true;
            });
        } catch (StaleElementReferenceException ex) { //вываливается ошибка иногда, так как окно с выбором регионов так и не пропадает, приходится использовать замыкание
            refreshCurrentPage();
            changeRegion(region);
        }
        return this;
    }
    public String getCurrentRegion(){
        return buttonChangeRegion.getText();
    }
    public String getCurrentPriceForSim(){
        return currentPriceForSim.getText();
    }
    private TinkoffMobilePage changeNationality(){
        selectFieldNatyonal.click();
        driver.findElement(By.xpath("//span[contains(text(),'Не имею гражданства РФ')]")).click();
        return this;
    }
    public TinkoffMobilePage toSiteTinkoffMobile(){
        goToPage("https://www.tinkoff.ru/mobile-operator/tariffs/");
        try {
            driver.findElement(By.xpath("//select[@name='internet']/parent::div/div"));
        } catch (NoSuchElementException e) {
            toSiteTinkoffMobile();
        }//страница не всегда полностью прогружается
        return this;
    }

    public Select setAndGetSelect(Enums.SelectLists selectLists) {
        return new Select(selectLists);
    }
    public TextInput setAndGetTextInput(Enums.TextInputs textInputs) {
        return new TextInput(textInputs);
    }
    public CheckBox setAndGetCheckBox(Enums.CheckBoxes checkBoxes) {
        return new CheckBox(checkBoxes);
    }
    public Button getButton() {
        return new Button();
    }

    public class Select{
        private String list;

        private Select(Enums.SelectLists selectLists) {
            changeTargetField(selectLists);
        }

        private Select openList(){
            driverWait.until(x->{
                x.findElement(By.xpath(String.format("//select[@name='%s']/parent::div/div",list))).click();
                return true;
            });
            return this;
        }
        public Select changeList(String newValue){
            openList();
            if(hasElement(By.xpath(String.format("//*[text() = '%s']",newValue)))) {
                driverWait.until(x -> {
                    x.findElement(By.xpath(String.format("//span[text()= '%s' and not(@class = 'ui-select__value')]", newValue))).click();
                    return true;
                });
            } else {
                System.out.println("Не имеется такого значения");
                openList();
            }
            return this;
        }
        public String getCurrentValueList(){
            return driverWait.until(x-> x.findElement(By.xpath(String.format("//select[@name='%s']/parent::div//span[contains(@class,'title-flex-text') or contains(@class,'value')]",list))).getText());
        }
        public Select changeTargetField(Enums.SelectLists selectedList){
            list = selectedList.getNameOfSelector();
            return this;
        }
            //на форме фиг знает что творится, пытался получить список элементов по xpath //select[@name='неймэлемента']/option
        // но,например, по гигабайтам просто текст не достается и всё, по минутам нормально
    }

    public class CheckBox {
        private String idCheckBox;

        private CheckBox(Enums.CheckBoxes checkBox) {
            changeTargetField(checkBox);
        }
        public CheckBox setStatus(boolean status){
            if(getCurrentStatusCheckBox() != status)
                driver.findElement(By.xpath(String.format("%s/parent::div",idCheckBox))).click();
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
            return driver.findElement(By.xpath(String.format("'%s'/ancestor::div[contains(@class,'CheckboxWithDescription')]/label",idCheckBox))).getText();
        }
    }

    public class TextInput{
        private By xPathTextArea;

        private TextInput(Enums.TextInputs textInputs) {
            changeTargetField(textInputs);
        }
        public TextInput setTextInTextArea(String text) {
            if (xPathTextArea.toString().contains("nationality")) {
                Select selectNat = setAndGetSelect(Enums.SelectLists.Nationality);
                if (!selectNat.getCurrentValueList().equals("Не имею гражданства РФ"))
                    selectNat.changeList("Не имею гражданства РФ");
            }
            Actions deleteAndSendText = new Actions(driver);
            deleteAndSendText.moveToElement(driver.findElement(xPathTextArea)).click();
            if (System.getProperty("browser").equals("chrome")) {
                deleteAndSendText
                        .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE)) //в хроме удалить что-то из поля работает через это
                        .sendKeys(text)
                        .perform();
            } else {
                driver.findElement(xPathTextArea).clear();
                deleteAndSendText.sendKeys(text).perform();
            }
            return this;
        }
        public TextInput changeTargetField(Enums.TextInputs selectedTextField){
            this.xPathTextArea = By.xpath(String.format("//input[@name = '%s']",selectedTextField.getNameOfTextInputArea()));
            return this;
        }
        public String getCurrentValue(){
            return driver.findElement(xPathTextArea).getAttribute("value");
        }
    }

    public class Button{
        private Button(){}
        public boolean isButtonActive(){
            return mainYellowButton.isEnabled();
        }
        public Button clickButton(){
            if(isButtonActive())
                mainYellowButton.click();
            return this;
        }
        public boolean hasButton(){
            return mainYellowButton.isDisplayed();
        }
    }
}
