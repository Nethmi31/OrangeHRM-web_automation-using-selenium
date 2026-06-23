package com.orangehrm.test;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.LogoutPage;

public class LogoutTest extends BaseTest {

    private LogoutPage logoutPage;

    @BeforeMethod
    public void loginFirst() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.Login("Admin", "admin123");
        logoutPage = new LogoutPage(driver);
    }

    @Test(description = "TC_LO_01 - Verify successful logout from dashboard", groups = {"Logout Module"})
    public void testSuccessfulLogout() {
        logoutPage.clickLogout();
        Assert.assertTrue(logoutPage.getCurrentUrl().contains("login"),
                "Should redirect to login page after logout");
    }

    @Test(description = "TC_LO_02 - Verify session invalidation after logout (back button)", groups = {"Logout Module"})
    public void testSessionInvalidationAfterLogout() {
        logoutPage.clickLogout();
        driver.navigate().back();
        Assert.assertTrue(logoutPage.getCurrentUrl().contains("login"),
                "Should remain on login page, not cached dashboard");
    }

    @Test(description = "TC_LO_03 - Verify user dropdown menu options", groups = {"Logout Module"})
    public void testDropdownMenuOptions() {
        List<String> options = logoutPage.getDropdownMenuOptions();
        Assert.assertTrue(options.contains("About"), "Dropdown should contain 'About'");
        Assert.assertTrue(options.contains("Support"), "Dropdown should contain 'Support'");
        Assert.assertTrue(options.contains("Change Password"), "Dropdown should contain 'Change Password'");
        Assert.assertTrue(options.contains("Logout"), "Dropdown should contain 'Logout'");
    }

    @Test(description = "TC_LO_04 - Verify logged-in username is displayed", groups = {"Logout Module"})
    public void testLoggedInUsernameDisplay() {
        String displayedName = logoutPage.getLoggedInUsername();
        Assert.assertFalse(displayedName.isEmpty(),
                "Username should be displayed in the top-right corner");
    }
}
