package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TinkoffDocuments extends Page {

    public TinkoffDocuments(WebDriver driver) {
        super(driver);
    }

    public String downloadRandomFileAndGetHisName(){
        List<WebElement> documents = driver.findElements(By.xpath("//a[@target='_blank']"));
        WebElement file = documents.get((int)(Math.random()*documents.size()));
        String fileName = file.getAttribute("href").replace("https://static.tinkoff.ru/documents/mvno_documents/promo/","");
        String pathName = (System.getProperty("user.dir") + "\\src\\test\\resources\\"+fileName).replace("\\",File.separator);
        file.click();
        return pathName;
    }
    public boolean checkDownloadFile(String path){
        try {
            Path filePath = Paths.get(path).toAbsolutePath();
            Files.deleteIfExists(filePath);
            File filePdf = new File(filePath.toString());
            for (int loop = 0; !filePdf.exists(); loop++) {
                if (loop > 20)
                    throw new FileNotFoundException("Не скачался файл");
                Thread.sleep(500);
            }
            logger.info(String.format("Файл скачался по пути %s", Paths.get(path).toAbsolutePath()));
            return true;
        } catch (IOException | InterruptedException ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public TinkoffDocuments goToSite(){
        goToPage("https://www.tinkoff.ru/mobile-operator/documents/");
        logger.info("Перешли на сайт тинькофф документы мобильного оператора");
        return this;
    }
}
