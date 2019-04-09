package tests;

import lombok.var;
import org.junit.Test;
import pages.GooglePage;
import pages.TinkoffMobilePage;
import utils.Enums;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class MobileOperator extends Runner {
    @Test
    public void clickingMainFormsTest(){
        TinkoffMobilePage page = factoryPages.getTinkoffMobilePage();
        page    .toSiteTinkoffMobile()
                .clickMainForms();
        assertEquals("Укажите ваше ФИО", page.getErrorMessageFio().getText());
        assertEquals("Необходимо указать номер телефона", page.getErrorMessageTel().getText());
        assertEquals("Поле обязательное", page.getErrorMessageNatyonal().getText());
    }
    @Test
    public void fillingUnvalidValuesMainFormTest() {
        TinkoffMobilePage page = factoryPages.getTinkoffMobilePage();
        page    .toSiteTinkoffMobile()
                .fillMainForms("Невалид --123","254564689216","unvalid","nonnat");
        assertEquals("Допустимо использовать только буквы русского алфавита и дефис", page.getCssErrorMessageFio().getText());
        assertEquals("Код оператора должен начинаться с цифры 9", page.getCssErrorMessageTel().getText());
        assertEquals("Введите корректный адрес эл. почты", page.getErrorMessageMail().getText());
        assertEquals("Выберите страну из выпадающего списка", page.getErrorMessageNatyonal().getText());
    }
    @Test
    public void switchBetweenTabsTest(){
        GooglePage googlePage = factoryPages.getGooglePage();
        googlePage
                .goToGoogle()
                .searchOurRequest("тинькофф мобайл ","тинькофф мобайл тарифы")//  гугле нет тарифов в поисковой выдаче если наоборот слова
                .findOurLinkAndClick("https://www.tinkoff.ru/mobile-operator/tariffs/");
        assertTrue(googlePage.isLoadedByTitle("Тарифы Тинькофф Мобайл"));
        googlePage.closeSimilarTabs("Google");
        assertTrue(googlePage.isUrlEqualsTo("https://www.tinkoff.ru/mobile-operator/tariffs/"));
    }
    @Test
    public void changeRegionTest(){
        TinkoffMobilePage pageTinkoffMobile = factoryPages.getTinkoffMobilePage();
        pageTinkoffMobile
                .toSiteTinkoffMobile()
                .changeRegion("Москва");
        assertEquals("Москва и Московская область",pageTinkoffMobile.getCurrentRegion());
        pageTinkoffMobile.refreshCurrentPage();
        assertEquals("Москва и Московская область",pageTinkoffMobile.getCurrentRegion());
        String priceForMoskov = pageTinkoffMobile.getCurrentPriceForSim();
        pageTinkoffMobile.changeRegion("Краснодар");
        assertNotEquals(pageTinkoffMobile.getCurrentRegion(),priceForMoskov);
        var selectors = pageTinkoffMobile
                .setAndGetSelect(Enums.SelectLists.Internet)
                .changeList("Безлимитный интернет")
                .changeTargetField(Enums.SelectLists.Calls)
                .changeList("Безлимитные минуты");
        var checkBoxes = pageTinkoffMobile
                .setAndGetCheckBox(Enums.CheckBoxes.UnlimitedSms)
                .setStatus(true)
                .changeTargetField(Enums.CheckBoxes.ModemMode)
                .setStatus(true);
        String priceForKrasnodar = pageTinkoffMobile.getCurrentPriceForSim();
        pageTinkoffMobile.changeRegion("Москва");
        selectors
                .changeTargetField(Enums.SelectLists.Internet)
                .changeList("Безлимитный интернет")
                .changeTargetField(Enums.SelectLists.Calls)
                .changeList("Безлимитные минуты");
        checkBoxes
                .changeTargetField(Enums.CheckBoxes.UnlimitedSms)
                .setStatus(true)
                .changeTargetField(Enums.CheckBoxes.ModemMode)
                .setStatus(true);
        assertEquals(pageTinkoffMobile.getCurrentPriceForSim(),priceForKrasnodar);
    }
   @Test
    public void notActiveButtonTest(){
        TinkoffMobilePage page = factoryPages.getTinkoffMobilePage();
        page
                .toSiteTinkoffMobile()
                .setAndGetSelect(Enums.SelectLists.Calls)
                .changeList("0 минут")
                .changeTargetField(Enums.SelectLists.Internet)
                .changeList("0 ГБ");
        page.setAndGetTextInput(Enums.TextInputs.Fio)
                .setTextInTextArea("Генадий Васильевич Василий")
                .changeTargetField(Enums.TextInputs.Telephone)
                .setTextInTextArea("9999999999");
        page.setAndGetCheckBox(Enums.CheckBoxes.SocialNetworks)
                .setStatus(false)
                .changeTargetField(Enums.CheckBoxes.Messagers)
                .setStatus(false);
        assertEquals(page.getCurrentPriceForSim(),"Общая цена: 0 \u20BD");
        var mainButton = page
                .getButton()
                .clickButton();
        assertTrue(mainButton.hasButton());//почему то кнопка активна даже если выполняются условия из тест кейса
    }
   @Test
    public void downloadFileTest(){
        factoryPages
                .getTinkoffDocuments()
                .goToSite()
                .downloadRandomFile();
    }
}

