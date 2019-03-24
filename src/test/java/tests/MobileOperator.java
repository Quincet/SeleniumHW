package tests;

import org.junit.Test;
import utils.Runner;
import Pages.TinkoffMobilePage;

public class MobileOperator extends Runner {
    @Test
    public void testMobileOperatorTestCase() {
        TinkoffMobilePage page = picker.tinkoffMobilePage;
        page.toSite();
        page.clickMainForms();
        page.clickNationality();
        page.assertClickgForm();
    }
    @Test
    public void testMobileOperatorTestCase2() {
        TinkoffMobilePage page = picker.tinkoffMobilePage;
        page.toSite();
        page.fillMainForms();
        page.fillNationality();
        page.assertFillingForm();
    }
}

