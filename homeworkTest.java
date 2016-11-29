package com.example.tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomeworkTest {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://www.debijenkorf.nl/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testHomework() throws Exception {
    // open page
    driver.get(baseUrl);
    // wait until the search field is visible
    WebDriverWait wait = new WebDriverWait(driver, 20);
    wait.until(
        ExpectedConditions.visibilityOfElementLocated(By.xpath('//form/input[@type="search"]')));
    // search for jeans
    driver.findElement(By.xpath('//form/input[@type="search"]')).sendKeys("jeans");
    driver.findElement(By.xpath('//form/input[@type="search"]')).sendKeys(Keys.ENTER);
    // select second result
    driver.findElement(By.cssSelector("li:nth-child(2) > div > a")).click();
    // select element based on visible text
    new Select(driver.findElement(By.cssSelector("select.dbk-form--input"))).selectByVisibleText("30");
    // driver.findElement(By.cssSelector("option[value=\"769501384800000\"]")).click();
    // store the product title
    String title = driver.findElement(By.xpath("//h1")).getText();
    // add the product to the cart
    driver.findElement(By.xpath('//div/button[contains(text(), "in winkelmand")]')).click();
    // wait for the notification message to disappear
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (!isElementPresent(By.xpath('//*[@class="dbk-notification--message"]'))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
    // click on the basket
    driver.findElement(By.xpath('//*[@class="hidden-xs"][@title="Shopping basket"]')).click();
    // wait for the slide-in to appear
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//h1/a"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
    // assert the product title in the cart is the same as in the page
    assertEquals(title, driver.findElement(By.xpath("//h1/a")).getText());
    // quit
    // driver.close();
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
