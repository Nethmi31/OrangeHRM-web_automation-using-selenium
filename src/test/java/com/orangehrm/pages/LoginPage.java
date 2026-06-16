package com.orangehrm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By usernameField = By.name("username");
    private By passwordField = By.name("password");
    private By LoginButton = By.xpath("//button[@type='submit']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
       // new WebDriverWait(driver, Duration.ofSeconds(10));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(50));
    }

    public void enterUsername(String username){
       WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
       usernameInput.clear();
       usernameInput.sendKeys(username);
    
    }

    public void enterPassword(String password){
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickLoginButton(){
        WebElement LoginBtn = wait.until(ExpectedConditions.elementToBeClickable(LoginButton));
        LoginBtn.click();
    }

    public void Login(String username, String password){
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }


    
}
