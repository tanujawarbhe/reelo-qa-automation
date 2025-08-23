package com.reelo.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.UUID;

public class ReeloCampaignAutomation {

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().window().maximize();

        try {
            System.out.println("Navigating to signup page.");
            driver.get("https://dev.reelo.io/signup");

            String randomEmail = "twarbhe" + UUID.randomUUID().toString().substring(0, 5) + "@yahoo.com";
            String password = "Test@1234";

            System.out.println("Filling out signup form...");
            driver.findElement(By.id("name")).sendKeys("Tanuja");
            driver.findElement(By.id("email")).sendKeys(randomEmail);
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            System.out.println("Waiting for dashboard to load.");
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/dashboard"),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Campaigns')]"))
            ));

            System.out.println("Navigating to Campaigns.");
            WebElement campaignsTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(),'Campaigns')]")
            ));
            campaignsTab.click();

            System.out.println("Selecting a campaign template.");
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".campaign-template-card"))).click();

            System.out.println("Proceeding to channel selection.");
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Next')]"))).click();

            System.out.println("Selecting SMS as channel.");
            WebElement smsCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//label[contains(text(),'SMS')]/preceding-sibling::input")));
            if (!smsCheckbox.isSelected()) {
                smsCheckbox.click();
            }

            driver.findElement(By.xpath("//button[contains(text(),'Next')]")).click();

            System.out.println("Setting campaign title.");
            WebElement titleInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("campaignTitle")));
            titleInput.clear();
            titleInput.sendKeys("Test Campaign by Automation");
            driver.findElement(By.xpath("//button[contains(text(),'Next')]")).click();

            System.out.println("Entering contact number.");
            wait.until(ExpectedConditions.elementToBeClickable(By.name("phone"))).sendKeys("7420088200");
            driver.findElement(By.xpath("//button[contains(text(),'Next')]")).click();

            System.out.println("Skipping to SMS content.");
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Next')]"))).click();

            System.out.println("Writing SMS body.");
            WebElement smsBody = wait.until(ExpectedConditions.elementToBeClickable(By.name("smsBody")));
            smsBody.clear();

            smsBody.sendKeys("Hello! This is a test campaign from Tanuja using Selenium automation.");

            driver.findElement(By.xpath("//button[contains(text(),'Next')]")).click();

            System.out.println("Testing the campaign.");
            WebElement phoneInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("testPhoneNumber")));
            phoneInput.clear();
            phoneInput.sendKeys("7420088200");

            WebElement testBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Test') or contains(text(),'Send Test')]")));
            testBtn.click();

            System.out.println("Waiting for confirmation that test SMS was sent.");
            try {
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Test message sent')]")),
                    ExpectedConditions.invisibilityOf(testBtn)
                ));
                System.out.println("Test SMS sent. Please check your phone.");
            } catch (Exception ignore) {
                System.out.println("No visible confirmation, but continuing");
            }

            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Save and Exit')]")));
            saveBtn.click();

            System.out.println("Campaign saved successfully!");

        } catch (Exception e) {
            System.out.println("Error during automation:");
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
