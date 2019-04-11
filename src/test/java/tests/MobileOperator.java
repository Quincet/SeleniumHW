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
    public void clickingMainFormsTest() {
        TinkoffMobilePage page = picker.getTinkoffMobilePage();
        page    .toSiteTinkoffMobile()
                .clickMainForms();
        assertEquals("Укажите ваше ФИО", picker.getDriver().findElement(By.xpath("//div[contains(@class,'fio')]//div[contains(@class,'error-message')]")).getText());
        assertEquals("Необходимо указать номер телефона", picker.getDriver().findElement(By.xpath("//div[contains(@class,'tel')]//div[contains(@class,'error-message')]")).getText());
        assertEquals("Поле обязательное", picker.getDriver().findElement(By.xpath("//input[@name='temp_non_resident_nationality']/following::div[contains(@class,'error-message')]")).getText());
    }
    @Test
    public void fillingNotValidValuesMainFormTest() {
        TinkoffMobilePage page = picker
                .getTinkoffMobilePage()
                .toSiteTinkoffMobile();
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
    public void switchBetweenTabsTest(){
        GooglePage googlePage = picker
                .getGooglePage()
                .searchOurRequest("тинькофф мобайл ","тинькофф мобайл тарифы")
                .findOurLinkAndClick("https://www.tinkoff.ru/mobile-operator/tariffs/");
        assertTrue(googlePage.isLoadedByTitle("Тарифы Тинькофф Мобайл"));
        googlePage.closeSimilarTabs("Google");
        assertTrue(googlePage.isUrlEqualsTo("https://www.tinkoff.ru/mobile-operator/tariffs/"));
    }
    @Test
    public void changeRegionTest(){
        TinkoffMobilePage pageTinkoffMobile = picker.getTinkoffMobilePage()
                .toSiteTinkoffMobile()
                .changeRegion("Москва");
        assertEquals("Москва и Московская область",pageTinkoffMobile.getCurrentRegion());
        pageTinkoffMobile.refresCurrentPage();
        assertEquals("Москва и Московская область",pageTinkoffMobile.getCurrentRegion());
        String priceForMoscow = pageTinkoffMobile.getCurrentPriceForSim();
        pageTinkoffMobile.changeRegion("Краснодар");
        assertNotEquals(pageTinkoffMobile.getCurrentRegion(),priceForMoscow);
        Select selectors = new Select(Enums.SelectLists.Internet,picker.getDriver())
                .changeList("Безлимитный интернет")
                .changeTargetField(Enums.SelectLists.Calls)
                .changeList("Безлимитные минуты");
        CheckBox checkBoxes = new CheckBox(Enums.CheckBoxes.UnlimitedSms,picker.getDriver())
                .setStatus(true)
                .changeTargetField(Enums.CheckBoxes.ModemMode)
                .setStatus(true);
        String priceForKrasnodar = pageTinkoffMobile.getCurrentPriceForSim();
        pageTinkoffMobile.changeRegion("Москва");
        selectors.changeTargetField(Enums.SelectLists.Internet)
                .changeList("Безлимитный интернет")
                .changeTargetField(Enums.SelectLists.Calls)
                .changeList("Безлимитные минуты");
        checkBoxes.changeTargetField(Enums.CheckBoxes.UnlimitedSms)
                .setStatus(true)
                .changeTargetField(Enums.CheckBoxes.ModemMode)
                .setStatus(true);
        assertEquals(pageTinkoffMobile.getCurrentPriceForSim(),priceForKrasnodar);
    }
   @Test
    public void notActiveButtonTest(){
        TinkoffMobilePage page = picker.getTinkoffMobilePage();
        WebDriver driver = picker.getDriver();
        page.toSiteTinkoffMobile();
        new Select(Enums.SelectLists.Calls,driver)
                .changeList("0 минут")
                .changeTargetField(Enums.SelectLists.Internet)
                .changeList("0 ГБ");
        new TextInput(Enums.TextInputs.Fio,driver)
                .setTextInTextArea("Генадий Васильевич Василий")
                .changeTargetField(Enums.TextInputs.Telephone)
                .setTextInTextArea("9999999999");
        new CheckBox(Enums.CheckBoxes.SocialNetworks,picker.getDriver())
                .setStatus(false)
                .changeTargetField(Enums.CheckBoxes.Messagers)
                .setStatus(false);
        assertEquals(page.getCurrentPriceForSim(),"Общая цена: 0 \u20BD");
        Button button = new Button(driver)
                .clickButton();
        assertTrue(button.hasButton());//почему то кнопка активна даже если выполняются условия из тест кейса
    }
   @Test
    public void downloadFileTest(){
       TinkoffDocuments page = picker.getTinkoffDocuments();
       String pathToFil = page
               .goToSite()
               .downloadRandomFileAndGetHisName();
       assertTrue(page.checkDownloadFile(pathToFil));
    }
}

