package tests;

import org.junit.Test;
import utils.Runner;
import Pages.TinkoffMobilePage;

public class MobileOperator extends Runner {
    @Test
    public void clickMainForm() {
        TinkoffMobilePage page = picker.tinkoffMobilePage;
        page.toSite();
        page.clickMainForms();
        page.clickNationality();
        page.checkUnvalidValueByClickMainForms();
    }
    @Test
    public void fillUnvalidValueMainForm() {
        TinkoffMobilePage page = picker.tinkoffMobilePage;
        page.toSite();
        page.fillUnvalidValueMainForms();
        page.fillNationality();
        page.checkUnvalidValueByFillkMainForms();
    }
}

