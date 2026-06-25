package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.AdminPage;
import com.orangehrm.pages.LoginPage;

public class AdminTest extends BaseTest {

    private AdminPage adminPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToAdmin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.Login("Admin", "admin123");
        adminPage = new AdminPage(driver);
        adminPage.navigateToAdmin();
    }

    @Test(description = "TC_A_01 - Verify navigation to Admin module", groups = {"Admin Module"})
    public void testNavigateToAdminModule() {
        Assert.assertTrue(adminPage.isAdminPageDisplayed(),
                "Admin page should load with System Users header displayed");
    }

    @Test(description = "TC_A_02 - Search system user by username", groups = {"Admin Module"})
    public void testSearchUserByUsername() {
        adminPage.searchByUsername("Admin");
        Assert.assertTrue(adminPage.getTableRowCount() > 0,
                "Table should show matching user records for username 'Admin'");
    }

    @Test(description = "TC_A_03 - Search system user with no results", groups = {"Admin Module"})
    public void testSearchUserNoResults() {
        adminPage.searchByUsername("NonExistentUser999");
        Assert.assertTrue(adminPage.isNoRecordsMessageDisplayed(),
                "'No Records Found' message should be displayed for non-existent user");
    }

    @Test(description = "TC_A_04 - Reset search filters", groups = {"Admin Module"})
    public void testResetSearchFilters() {
        adminPage.searchByUsername("Admin");
        adminPage.clickReset();
        String fieldValue = adminPage.getSearchFieldValue();
        Assert.assertTrue(fieldValue == null || fieldValue.isEmpty(),
                "Username search field should be cleared after reset");
    }

    @Test(description = "TC_A_05 - Add new system user", groups = {"Admin Module"})
    public void testAddNewSystemUser() {
        String uniqueUsername = "TestUser" + System.currentTimeMillis();
        adminPage.clickAdd();
        adminPage.fillAddUserForm("ESS", "Enabled", "a", uniqueUsername, "Test@12345");
        adminPage.clickSave();
        Assert.assertTrue(adminPage.isSaveSuccessful(),
                "Should redirect to System Users list after saving new user");
    }

    @Test(description = "TC_A_06 - Add user with duplicate username validation", groups = {"Admin Module"})
    public void testAddUserDuplicateUsername() {
        adminPage.clickAdd();
        adminPage.selectUserRole("Admin");
        adminPage.selectStatus("Enabled");
        adminPage.enterEmployeeName("a");
        adminPage.enterUsername("Admin");
        adminPage.enterPassword("Admin@12345");
        adminPage.enterConfirmPassword("Admin@12345");
        Assert.assertTrue(adminPage.isAlreadyExistsErrorDisplayed(),
                "'Already exists' error should be displayed for duplicate username");
    }

    @Test(description = "TC_A_07 - Add user required field validation", groups = {"Admin Module"})
    public void testAddUserRequiredFieldValidation() {
        adminPage.clickAdd();
        adminPage.clickSave();
        Assert.assertTrue(adminPage.getRequiredErrorCount() >= 1,
                "Required field errors should appear when submitting empty form");
    }

    @Test(description = "TC_A_08 - Add user password mismatch validation", groups = {"Admin Module"})
    public void testAddUserPasswordMismatch() {
        adminPage.clickAdd();
        adminPage.selectUserRole("ESS");
        adminPage.selectStatus("Enabled");
        adminPage.enterEmployeeName("a");
        adminPage.enterUsername("MismatchUser" + System.currentTimeMillis());
        adminPage.enterPassword("Pass123!");
        adminPage.enterConfirmPassword("Different1!");
        adminPage.clickSave();
        Assert.assertTrue(adminPage.isPasswordMismatchErrorDisplayed(),
                "'Passwords do not match' error should be displayed");
    }

    @Test(description = "TC_A_09 - Delete system user", groups = {"Admin Module"})
    public void testDeleteSystemUser() {
        String uniqueUsername = "DelUser" + System.currentTimeMillis();
        adminPage.clickAdd();
        adminPage.fillAddUserForm("ESS", "Enabled", "a", uniqueUsername, "Test@12345");
        adminPage.clickSave();
        Assert.assertTrue(adminPage.isSaveSuccessful(),
                "User should be created before deletion");

        adminPage.navigateToAdmin();
        adminPage.searchByUsername(uniqueUsername);
        adminPage.deleteFirstUserByTrashIcon();
        Assert.assertTrue(adminPage.isSuccessToastDisplayed(),
                "Success toast should appear after deleting a user");
    }

    @Test(description = "TC_A_10 - View Job Titles list", groups = {"Admin Module"})
    public void testViewJobTitlesList() {
        adminPage.navigateToJobTitles();
        Assert.assertTrue(adminPage.isJobTitlesPageDisplayed(),
                "Job Titles page should be displayed");
        Assert.assertTrue(adminPage.isJobTitlesTableDisplayed(),
                "Job Titles table should be displayed with records");
    }
}
