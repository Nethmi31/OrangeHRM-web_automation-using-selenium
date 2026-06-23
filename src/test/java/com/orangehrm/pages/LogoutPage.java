package com.orangehrm.pages;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LogoutPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By userDropdown = By.className("oxd-userdropdown-tab");
    private By dropdownMenu = By.className("oxd-dropdown-menu");
    private By dropdownMenuItems = By.xpath("//ul[@class='oxd-dropdown-menu']//a");
    private By logoutLink = By.xpath("//a[text()='Logout']");
    private By usernameDisplay = By.xpath("//p[@class='oxd-userdropdown-name']");

    public LogoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickUserDropdown() {
        wait.until(ExpectedConditions.elementToBeClickable(userDropdown)).click();
    }

    public void clickLogout() {
        clickUserDropdown();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }

    public List<String> getDropdownMenuOptions() {
        clickUserDropdown();
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownMenu));
        List<WebElement> items = driver.findElements(dropdownMenuItems);
        return items.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public String getLoggedInUsername() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameDisplay)).getText();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
