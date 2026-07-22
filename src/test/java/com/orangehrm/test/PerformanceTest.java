package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.PerformancePage;

public class PerformanceTest extends BaseTest {

    private PerformancePage performancePage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToPerformancePage() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.Login("Admin", "admin123");
        loginPage.isDashboardVisible();
        performancePage = new PerformancePage(driver);
        performancePage.navigateToPerformancePage();
    }

    @Test(description = "TC_Performance_01 - Verify navigation to Performance module", groups = {"Performance Module"})
    public void testNavigationToPerformanceModule() {
        // Verify that the Performance page is displayed
        Assert.assertTrue(performancePage.isPerformancePageDisplayed(),"Performance page should load with Employee review header displayed");
    }    

}
