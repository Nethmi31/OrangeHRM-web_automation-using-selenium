package com.orangehrm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyInfoPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private WebDriverWait shortWait;

    // Sidebar
    private By myInfoMenuLink = By.xpath("//span[text()='My Info']/ancestor::a");

    // Page headers
    private By personalDetailsHeader = By.xpath("//h6[text()='Personal Details']");

    public MyInfoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(7));
    }

    // --- Navigation ---

    public void navigateToMyInfo() {
        wait.until(ExpectedConditions.elementToBeClickable(myInfoMenuLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader));
    }

    public boolean isPersonalDetailsDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader)).isDisplayed();
    }

    // --- Tab navigation (Personal Details, Contact Details, Emergency Contacts,
    // Dependents, Immigration, Job, Salary, Report-to, Qualifications, Memberships) ---

    public void navigateToTab(String tabName) {
        By tabLink = By.xpath("//a[normalize-space()='" + tabName + "']");
        wait.until(ExpectedConditions.elementToBeClickable(tabLink)).click();
    }

    public boolean isTabHeaderDisplayed(String tabName) {
        By tabHeader = By.xpath("//h6[normalize-space()='" + tabName + "']");
        try {
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(tabHeader)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
