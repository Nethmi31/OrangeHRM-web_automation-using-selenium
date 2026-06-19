package com.orangehrm.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;

import java.time.Duration;

public class LoginTest extends BaseTest {

    // @Test
    // public void testValidLogin(){
    //     LoginPage loginPage = new LoginPage(driver);
    //     loginPage.Login("Admin","admin123");
    //     WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
    //     boolean isDashboardVisible = wait
    //         .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[text()='Dashboard']"))).isDisplayed();
    //     Assert.assertTrue(isDashboardVisible, "Dashboard should be visible after successful login");
       
        
    // }
}
