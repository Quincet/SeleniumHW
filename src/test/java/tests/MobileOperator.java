package tests;

import pages.GooglePage;
import pages.TinkoffDocuments;
import pages.TinkoffMobilePage;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.*;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class MobileOperator extends Runner {
    @Test
    public void clickingMainForms() {
        TinkoffMobilePage page = picker.getTinkoffMobilePage();
        page.toSiteTinkoffMobile();
        page.clickMainForms();
        assertEquals("Укажите ваше ФИО", picker.getDriver().findElement(By.xpath("//div[contains(@class,'fio')]//div[contains(@class,'error-message')]")).getText());
        assertEquals("Необходимо указать номер телефона", picker.getDriver().findElement(By.xpath("//div[contains(@class,'tel')]//div[contains(@class,'error-message')]")).getText());
        assertEquals("Поле обязательное", picker.getDriver().findElement(By.xpath("//input[@name='temp_non_resident_nationality']/following::div[contains(@class,'error-message')]")).getText());
    }
    @Test
    public void fillingUnvalidValuesMainForm() {
        TinkoffMobilePage page = picker.getTinkoffMobilePage();
        page.toSiteTinkoffMobile();
        page.fillMainForms("Невалид --123","254564689216","unvalid","nonnat");
        assertEquals("Допустимо использовать только буквы русского алфавита и дефис", picker.getDriver()
                .findElement(By.cssSelector("div[class *= fio] [class *= error-message]")).getText());
        assertEquals("Код оператора должен начинаться с цифры 9", picker.getDriver()
                .findElement(By.cssSelector("div[class *= 'tel'] [class *= 'error-message']")).getText());
        assertEquals("Введите корректный адрес эл. почты", picker.getDriver()
                .findElement(By.xpath("//input[@name='email']/following::div[contains(@class,'error-message')]")).getText()); //Не получается достать на странице текст ошибок по средством css коллектиора,
        assertEquals("Выберите страну из выпадающего списка", picker.getDriver()                                    //работает только xpath,так как там поля email и поле с выбором страной имеют одинаковую структуру для текста ошибок
                .findElement(By.xpath("//input[@name='temp_non_resident_nationality']/following::div[contains(@class,'error-message')]")).getText());
    }
    @Test
    public void switchBetweenTabs(){
        GooglePage googlePage = picker.getGooglePage();
        googlePage.searchOurRequest("мобайл тинькофф","мобайл тинькофф тарифы");
        googlePage.findOurLinkAndClick("https://www.tinkoff.ru/mobile-operator/tariffs/");
        assertTrue(googlePage.isLoadedByTitle("Тарифы Тинькофф Мобайл"));
        googlePage.closeSimilarTabs("Google");
        assertTrue(googlePage.isUrlEqualsTo("https://www.tinkoff.ru/mobile-operator/tariffs/"));
    }
    @Test
    public void changeRegion(){
        TinkoffMobilePage pageTinkoffMobile = picker.getTinkoffMobilePage();
        pageTinkoffMobile.toSiteTinkoffMobile();
        pageTinkoffMobile.changeRegion("Москва");
        assertEquals("Москва и Московская область",pageTinkoffMobile.getCurrentRegion());
        pageTinkoffMobile.refresCurrentPage();
        assertEquals("Москва и Московская область",pageTinkoffMobile.getCurrentRegion());
        String priceForMoskov = pageTinkoffMobile.getCurrentPriceForSim();
        pageTinkoffMobile.changeRegion("Краснодар");
        assertNotEquals(pageTinkoffMobile.getCurrentRegion(),priceForMoskov);
        Select selectors = new Select(Enums.SelectLists.Internet,picker.getDriver());//     Дальше использование вспомогательных
        selectors.changeList("Безлимитный интернет");                       //     классов для проверки общей цены с макс тарифами
        selectors = selectors.changeTargetField(Enums.SelectLists.Calls);
        selectors.changeList("Безлимитные минуты");
        CheckBox checkBoxes = new CheckBox(Enums.CheckBoxes.UnlimitedSms,picker.getDriver());
        checkBoxes.setStatus(true);
        checkBoxes.changeTargetField(Enums.CheckBoxes.ModemMode);
        checkBoxes.setStatus(true);
        String priceForKrasnodar = pageTinkoffMobile.getCurrentPriceForSim();
        pageTinkoffMobile.changeRegion("Москва");
        selectors = selectors.changeTargetField(Enums.SelectLists.Internet);
        selectors.changeList("Безлимитный интернет");
        selectors = selectors.changeTargetField(Enums.SelectLists.Calls);
        selectors.changeList("Безлимитные минуты");
        checkBoxes = checkBoxes.changeTargetField(Enums.CheckBoxes.UnlimitedSms);
        checkBoxes.setStatus(true);
        checkBoxes.changeTargetField(Enums.CheckBoxes.ModemMode);
        checkBoxes.setStatus(true);
        assertEquals(pageTinkoffMobile.getCurrentPriceForSim(),priceForKrasnodar);
    }
   @Test
    public void notActiveButton(){
        TinkoffMobilePage page = picker.getTinkoffMobilePage();
        WebDriver driver = picker.getDriver();
        page.toSiteTinkoffMobile();
        Select select = new Select(Enums.SelectLists.Calls,driver);
        select.changeList("0 минут");
        select = select.changeTargetField(Enums.SelectLists.Internet);
        select.changeList("0 ГБ");
        TextInput textInput = new TextInput(Enums.TextInputs.Fio,driver);
        textInput.setTextInTextArea("Генадий Васильевич Василий");
        textInput = textInput.changeTargetField(Enums.TextInputs.Telephone);
        textInput.setTextInTextArea("9999999999");
        CheckBox checkBoxex = new CheckBox(Enums.CheckBoxes.SocialNetworks,picker.getDriver());
        checkBoxex.setStatus(false);
        checkBoxex = checkBoxex.changeTargetField(Enums.CheckBoxes.Messagers);
        checkBoxex.setStatus(false);
        assertEquals(page.getCurrentPriceForSim(),"Общая цена: 0 \u20BD");
        Button button = new Button(driver);
        button.clickButton();
        assertTrue(button.hasButton());//почему то кнопка активна даже если выполняются условия из тест кейса
    }
   @Test
    public void downloadFile(){
        TinkoffDocuments tinkoffDocuments = picker.getTinkoffDocuments();
        tinkoffDocuments.goToSite();
        tinkoffDocuments.downloadRandomFile();
    }
}

