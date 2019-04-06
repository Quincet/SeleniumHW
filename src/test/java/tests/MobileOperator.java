package tests;

import lombok.var;
import org.junit.Test;
import org.openqa.selenium.By;
import pages.GooglePage;
import pages.TinkoffDocuments;
import pages.TinkoffMobilePage;
import utils.*;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class MobileOperator extends Runner {
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
        pageTinkoffMobile.refreshCurrentPage();
        assertEquals("Москва и Московская область",pageTinkoffMobile.getCurrentRegion());
        String priceForMoskov = pageTinkoffMobile.getCurrentPriceForSim();
        pageTinkoffMobile.changeRegion("Краснодар");
        assertNotEquals(pageTinkoffMobile.getCurrentRegion(),priceForMoskov);
        var selectors = pageTinkoffMobile.setAndGetSelect(Enums.SelectLists.Internet);//     Дальше использование вспомогательных
        selectors.changeList("Безлимитный интернет");                       //     классов для проверки общей цены с макс тарифами
        selectors.changeTargetField(Enums.SelectLists.Calls);
        selectors.changeList("Безлимитные минуты");
        var checkBoxes = pageTinkoffMobile.setAndGetCheckBox(Enums.CheckBoxes.UnlimitedSms);
        checkBoxes.setStatus(true);
        checkBoxes.changeTargetField(Enums.CheckBoxes.ModemMode);
        checkBoxes.setStatus(true);
        String priceForKrasnodar = pageTinkoffMobile.getCurrentPriceForSim();
        pageTinkoffMobile.changeRegion("Москва");
        selectors.changeTargetField(Enums.SelectLists.Internet);
        selectors.changeList("Безлимитный интернет");
        selectors.changeTargetField(Enums.SelectLists.Calls);
        selectors.changeList("Безлимитные минуты");
        checkBoxes.changeTargetField(Enums.CheckBoxes.UnlimitedSms);
        checkBoxes.setStatus(true);
        checkBoxes.changeTargetField(Enums.CheckBoxes.ModemMode);
        checkBoxes.setStatus(true);
        assertEquals(pageTinkoffMobile.getCurrentPriceForSim(),priceForKrasnodar);
    }
   @Test
    public void notActiveButton(){
        TinkoffMobilePage page = picker.getTinkoffMobilePage();
        page.toSiteTinkoffMobile();
        var select = page.setAndGetSelect(Enums.SelectLists.Calls);
        select.changeList("0 минут");
        select.changeTargetField(Enums.SelectLists.Internet);
        select.changeList("0 ГБ");
        var textInput = page.setAndGetTextInput(Enums.TextInputs.Fio);
        textInput.setTextInTextArea("Генадий Васильевич Василий");
        textInput.changeTargetField(Enums.TextInputs.Telephone);
        textInput.setTextInTextArea("9999999999");
        var checkBoxes = page.setAndGetCheckBox(Enums.CheckBoxes.SocialNetworks);
        checkBoxes.setStatus(false);
        checkBoxes.changeTargetField(Enums.CheckBoxes.Messagers);
        checkBoxes.setStatus(false);
        assertEquals(page.getCurrentPriceForSim(),"Общая цена: 0 \u20BD");
        var button = page.getButton();
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

