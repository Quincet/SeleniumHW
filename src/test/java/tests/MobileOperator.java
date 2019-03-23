package tests;

import org.junit.Test;
import utils.Runner;
import utils.TinkoffMobilePage;

public class MobileOperator extends Runner {
    @Test
    public void testMobileOperatorTestCase() {
        TinkoffMobilePage page = picker.tinkoffMobilePage;
        page.toSite();
        page.clickMainForms();
        page.clickNatyonalyti();
        page.assertClickgForm();
    }
    @Test
    public void testMobileOperatorTestCase2() {
        TinkoffMobilePage page = picker.tinkoffMobilePage;
        page.toSite();
        page.fillMainForms();
        page.fillNationalyti();
        page.assertFillingForm();
    }
}

