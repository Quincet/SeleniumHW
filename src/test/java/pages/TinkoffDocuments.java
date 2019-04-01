package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TinkoffDocuments extends Page {

    public TinkoffDocuments(WebDriver driver) {
        super(driver);
    }

    public void downloadRandomFile(){
        try {
            List<WebElement> documents = driver.findElements(By.xpath("//a[@target='_blank']"));
            WebElement file = documents.get((int)(Math.random()*documents.size()));
            String fileName = file.getAttribute("href").replace("https://static.tinkoff.ru/documents/mvno_documents/promo/","");
            Path filePath = Paths.get(System.getProperty("user.dir") + "/src/test/resources/"+fileName).toAbsolutePath();
            Files.deleteIfExists(filePath);
            file.click();
            File filePdf = new File(filePath.toString());
            while (!filePdf.exists()){
                Thread.sleep(250);
            }
            logger.info(String.format("Файл скачался по пути %s", Paths.get(System.getProperty("user.dir") + "/src/test/resources/").toAbsolutePath()));
        } catch (IOException |InterruptedException ex){
            ex.printStackTrace();
        }
    }
    public void goToSite(){
        goToPage("https://www.tinkoff.ru/mobile-operator/documents/");
    }
}
