package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test(description = "Verify successful login with valid credentials", groups = {"Login Module"})
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.Login("Admin", "admin123");
        Assert.assertTrue(loginPage.isDashboardVisible(), "Dashboard should be visible after successful login");
    }

    @Test(description = "Verify error message with invalid username", groups = {"Login Module"})
    public void testInvalidUsername() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.Login("InvalidUser", "admin123");
        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid credentials");
    }

    @Test(description = "Verify error message with invalid password", groups = {"Login Module"})
    public void testInvalidPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.Login("Admin", "wrongpassword");
        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid credentials");
    }

    @Test(description = "Verify error message with both invalid credentials", groups = {"Login Module"})
    public void testInvalidCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.Login("InvalidUser", "wrongpassword");
        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid credentials");
    }

    @Test(description = "Verify required field errors with empty credentials", groups = {"Login Module"})
    public void testEmptyCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickLoginButton();
        Assert.assertTrue(loginPage.getRequiredErrorCount() >= 1,
                "Required errors should appear when credentials are missing");
    }

    @Test(description = "Verify required field error with empty username", groups = {"Login Module"})
    public void testEmptyUsername() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterPassword("admin123");
        loginPage.clickLoginButton();
        Assert.assertTrue(loginPage.getRequiredErrorCount() >= 1,
                "Required error should appear for empty username field");
    }

    @Test(description = "Verify required field error with empty password", groups = {"Login Module"})
    public void testEmptyPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername("Admin");
        loginPage.clickLoginButton();
        Assert.assertTrue(loginPage.getRequiredErrorCount() >= 1,
                "Required error should appear for empty password field");
    }

    @Test(description = "Verify password field is masked", groups = {"Login Module"})
    public void testPasswordFieldIsMasked() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertEquals(loginPage.getPasswordFieldType(), "password",
                "Password field should be masked");
    }

    @Test(description = "Verify unauthenticated user cannot access dashboard directly", groups = {"Login Module"})
    public void testDashboardAccessWithoutLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToDashboardDirectly();
        Assert.assertTrue(loginPage.getCurrentUrl().contains("login"),
                "Unauthenticated user should be redirected to login page");
    }
}
