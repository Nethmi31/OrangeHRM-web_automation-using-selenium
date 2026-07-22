package com.orangehrm.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;



public class PerformancePage {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private WebDriverWait shortWait;

    //performance menu link
    private By performanceMenuLink = By.xpath("//span[text()='Performance']/ancestor::a");

    //Page Header
    private By topBarHeader = By.xpath("//h6[text()='Performance']");
    private By employeereviewHeader = By.xpath("//h5[text()='Employee Reviews']");

    public PerformancePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(7));
    }


    //------Navigation -----------

    public void navigateToPerformancePage() {
        wait.until(ExpectedConditions.elementToBeClickable(performanceMenuLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(employeereviewHeader));
    }

    public boolean isPerformancePageDisplayed() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(topBarHeader));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(employeereviewHeader)).isDisplayed();
    }
}
