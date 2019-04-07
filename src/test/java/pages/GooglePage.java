package pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class GooglePage extends Page {
    @FindBy(name = "q")
    @Getter
    private WebElement searchField;

    public GooglePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver,this);
    }
    public GooglePage searchOurRequest(String toWriteInSearchField,String toFindInSearchField){
        searchField.sendKeys(toWriteInSearchField);
        ignoringAnimation(x -> {
            List<WebElement> elements = driver.findElements(By.xpath("//ul[@role='listbox']/li[@role='presentation' and //*[@role='option']]"));
            for(WebElement element: elements){
                if(element.getText().toLowerCase().equals(toFindInSearchField)){
                    element.click();
                    break;
                }
            }
            return x.getTitle().contains(String.format("%s - Поиск в Google",toFindInSearchField));
        });
        return this;
    }
    public GooglePage findOurLinkAndClick(String link){
        String xPathFindLink = String.format("//cite[contains(text(),'%s')]",link);
        if(!hasElement(By.xpath(xPathFindLink))) {
            logger.error("Нет на странцие поисковый выдачи нашего линка");
            return this;
        }
        String titleSite = driver.findElement(By.xpath(xPathFindLink + "/ancestor::a/h3")).getText();
        driver.findElement(By.xpath(xPathFindLink)).click();
        switchToWindow(titleSite);
        return this;
    }
    public GooglePage goToGoogle(){
        goToPage("https://www.google.ru/");
        return this;
    }
}
