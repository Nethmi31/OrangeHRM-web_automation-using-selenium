package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.MyInfoPage;

public class MyInfoTest extends BaseTest {

    private MyInfoPage myInfoPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToMyInfo() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.Login("Admin", "admin123");
        loginPage.isDashboardVisible();
        myInfoPage = new MyInfoPage(driver);
        myInfoPage.navigateToMyInfo();
    }

    @Test(description = "TC_MI_01 - Navigate to My Info", groups = {"My Info Module"})
    public void testNavigateToMyInfo() {
        Assert.assertTrue(myInfoPage.isPersonalDetailsDisplayed(),
                "Personal Details page should load for the logged-in user after clicking My Info");
    }

    @Test(description = "TC_MI_02 - Navigate through all My Info tabs", groups = {"My Info Module"})
    public void testNavigateToAllMyInfoTabs() {
        String[] tabNames = {
                "Personal Details", "Contact Details", "Emergency Contacts", "Dependents",
                "Immigration", "Job", "Salary", "Report-to", "Qualifications", "Memberships"
        };

        for (String tabName : tabNames) {
            myInfoPage.navigateToTab(tabName);
            Assert.assertTrue(myInfoPage.isTabHeaderDisplayed(tabName),
                    tabName + " section should be displayed after clicking its tab");
        }
    }
}
