import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Arild on 12/30/2016.
 */
class MainPageTest {

    WebDriver driver;

    @BeforeEach
    void setUp() {
        System.setProperty("webdriver.gecko.driver","E:\\geckodriver\\geckodriver.exe");
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        String baseUrl = "https://www.games-workshop.com/en-US/Home";
        driver.get(baseUrl);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void searchForFlashGitz(){
        driver.findElement(By.xpath(".//input[@class='test-search-term search-term']")).sendKeys("flash gitz");
        driver.findElement(By.xpath(".//button[@class='test-search']")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        int searchResults = Integer.parseInt(driver.findElement(By.xpath(".//h6[@class='count']/em")).getText());
        List<WebElement> productListing = driver.findElements(By.xpath(".//*[contains(@id,'item-prod')]"));
        System.out.println("products displayed on the page: " + productListing.size());
        System.out.println("number of products found on the page: " + searchResults);

        //Verify that the number of items displayed on the page matches the number of items returned in the search
        Assert.assertEquals(productListing.size(),searchResults);
    }

    @Test
    void verifySearchQueryMatchesReturnedResults(){
        String searchQuery = "flash gitz";
        driver.findElement(By.xpath(".//input[@class='test-search-term search-term']")).sendKeys(searchQuery);
        driver.findElement(By.xpath(".//button[@class='test-search']")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        List<WebElement> results = driver.findElements(By.xpath(".//*[contains(@id,'item-prod')]//a[contains(text(),'Gitz')]"));

        //Verify that each product returned has the search query in the text, and is a valid search result
        for(int i = 0; i < results.size(); i++){
            System.out.println("found \"" + results.get(i).getText().toLowerCase() + "\" in the element. Comparing with the search for \"" + searchQuery.toLowerCase() + "\"");
            Assert.assertTrue("the result page string does not match the search term",results.get(i).getText().toLowerCase().contains(searchQuery.toLowerCase()));
        }
    }

    @Test
    void addAProductToTheCart(){
        driver.findElement(By.cssSelector(".test-nav-logo-cat440130a-flat")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.findElement(By.xpath(".//label[@class='test-subnav-link']//span[text()='Space Marines']")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='product-listing']//li[@id='item-prod3030259-99120101153']//button[contains(@class,'addtocart')]")));
        driver.findElement(By.xpath(".//*[@id='product-listing']//li[@id='item-prod3030259-99120101153']//button[contains(@class,'addtocart')]")).click();
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[@id='container']//*[@class='login-basket-box']//*[@id='header-infobar-checkout']//span")));
        String cartValue = driver.findElement(By.xpath(".//div[@id='container']//*[@class='login-basket-box']//*[@id='header-infobar-checkout']//span")).getText();

        //Verify that the cart object contains a dollar value
        System.out.println("We have added products worth " + cartValue);
        Assert.assertTrue("the cart does not contain a dollar value",cartValue.contains("$"));

    }

    @Test
    void verifyNavBarLinks(){
         List<WebElement> navBarLinks = driver.findElements(By.xpath(".//*[@id='header-navbar']//img"));
         int unknown = 0;
         for(int i = 0; i < navBarLinks.size();i++){
             String titleText = navBarLinks.get(i).getAttribute("title");

             switch (titleText){

                 case "New & Exclusive":
                     break;
                 case "Warhammer Age of Sigmar":
                     break;
                 case "Warhammer 40,000":
                     break;
                 case "The Hobbit":
                     break;
                 case "Painting & Modelling":
                     break;
                 case "Boxed Games":
                     break;
                 default:
                    unknown++;
             }
         }

         //Verify the Navigation bar does not have any unknown or distorted header links
         Assert.assertTrue("unknown element is present in the nvarbar header",unknown == 0);

    }

    @Test
    void verifyHobbitMenuAfterHobbitLink(){
        driver.findElement(By.cssSelector("img[title='The Hobbit']")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        List<WebElement> menuBar = driver.findElements(By.xpath(".//h3[contains(@id,'dimension')]//button"));
        int menuCounter = 0;
        for(int i = 0; i < menuBar.size();i++){
            String menuText = menuBar.get(i).getText().toLowerCase();
            if (menuText.contains("hobbit")) {
                menuCounter++;
            }
        }

        Assert.assertTrue("The menu options does not contain the word Hobbit",menuCounter > 0);

    }


}