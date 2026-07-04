package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.PIMPage;

public class PIMTest extends BaseTest {

    private PIMPage pimPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToPIM() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.Login("Admin", "admin123");
        loginPage.isDashboardVisible();
        pimPage = new PIMPage(driver);
        pimPage.navigateToPIM();
    }

    // ==================== Employee List Tests ====================

    @Test(description = "TC_P_01 - Verify navigation to PIM module", groups = {"PIM Module"})
    public void testNavigateToPIMModule() {
        Assert.assertTrue(pimPage.isPIMPageDisplayed(),
                "PIM page should load with Employee Information header displayed");
    }

    @Test(description = "TC_P_02 - Search employee by name", groups = {"PIM Module"})
    public void testSearchEmployeeByName() {
        pimPage.enterSearchEmployeeName("a");
        pimPage.clickSearch();
        Assert.assertTrue(pimPage.getTableRowCount() > 0,
                "Table should show matching employee records after searching by name");
    }

    @Test(description = "TC_P_03 - Search employee with no results", groups = {"PIM Module"})
    public void testSearchEmployeeNoResults() {
        pimPage.enterSearchEmployeeId("9999999");
        pimPage.clickSearch();
        Assert.assertTrue(pimPage.isNoRecordsMessageDisplayed(),
                "'No Records Found' message should be displayed for non-existent employee ID");
    }

    @Test(description = "TC_P_04 - Reset employee search filters", groups = {"PIM Module"})
    public void testResetEmployeeSearch() {
        pimPage.enterSearchEmployeeId("0001");
        pimPage.clickReset();
        String fieldValue = pimPage.getEmployeeIdSearchFieldValue();
        Assert.assertTrue(fieldValue == null || fieldValue.isEmpty(),
                "Employee ID search field should be cleared after reset");
    }

    @Test(description = "TC_P_05 - Add new employee", groups = {"PIM Module"})
    public void testAddNewEmployee() {
        String uniqueName = "Test" + System.currentTimeMillis();
        pimPage.clickAddEmployee();
        pimPage.enterFirstName(uniqueName);
        pimPage.enterLastName("AutoUser");
        pimPage.clickSave();
        Assert.assertTrue(pimPage.isAddEmployeeSuccessful(),
                "Should navigate to Personal Details page after saving new employee");
    }

    @Test(description = "TC_P_06 - Add employee required field validation", groups = {"PIM Module"})
    public void testAddEmployeeRequiredFieldValidation() {
        pimPage.clickAddEmployee();
        pimPage.clickSave();
        Assert.assertTrue(pimPage.getRequiredErrorCount() >= 1,
                "Required field errors should appear when submitting empty Add Employee form");
    }

    @Test(description = "TC_P_07 - Edit employee details", groups = {"PIM Module"})
    public void testEditEmployeeDetails() {
        String uniqueName = "Edit" + System.currentTimeMillis();
        pimPage.clickAddEmployee();
        pimPage.enterFirstName(uniqueName);
        pimPage.enterLastName("Before");
        pimPage.clickSave();
        Assert.assertTrue(pimPage.isAddEmployeeSuccessful(),
                "Employee should be created before editing");

        pimPage.navigateToPIM();
        pimPage.enterSearchEmployeeName(uniqueName);
        pimPage.clickSearch();
        pimPage.clickEditFirstEmployee();
        Assert.assertTrue(pimPage.isEditEmployeePageDisplayed(),
                "Personal Details edit page should be displayed");

        pimPage.editFirstName("Edited" + System.currentTimeMillis());
        pimPage.clickSave();
        Assert.assertTrue(pimPage.isSuccessToastDisplayed(),
                "Success toast should appear after editing employee details");
    }

    @Test(description = "TC_P_08 - Delete employee", groups = {"PIM Module"})
    public void testDeleteEmployee() {
        String uniqueName = "Del" + System.currentTimeMillis();
        pimPage.clickAddEmployee();
        pimPage.enterFirstName(uniqueName);
        pimPage.enterLastName("ToDelete");
        pimPage.clickSave();
        Assert.assertTrue(pimPage.isAddEmployeeSuccessful(),
                "Employee should be created before deletion");

        pimPage.navigateToPIM();
        pimPage.enterSearchEmployeeName(uniqueName);
        pimPage.clickSearch();
        pimPage.deleteFirstEmployee();
        Assert.assertTrue(pimPage.isSuccessToastDisplayed(),
                "Success toast should appear after deleting an employee");
    }

    // ==================== Employee Reports Tests ====================

    @Test(description = "TC_P_09 - Navigate to Employee Reports", groups = {"PIM Module"})
    public void testNavigateToEmployeeReports() {
        pimPage.navigateToEmployeeReports();
        Assert.assertTrue(pimPage.isReportsPageDisplayed(),
                "Employee Reports page should be displayed after clicking Reports in PIM menu");
    }

    @Test(description = "TC_P_10 - Search employee report by name", groups = {"PIM Module"})
    public void testSearchEmployeeReport() {
        pimPage.navigateToEmployeeReports();
        pimPage.enterReportNameSearch("Employee");
        pimPage.clickReportSearch();
        Assert.assertTrue(pimPage.getReportTableRowCount() > 0,
                "Table should show matching reports after selecting from autocomplete and searching");
    }

    @Test(description = "TC_P_11 - Reset employee report search", groups = {"PIM Module"})
    public void testResetReportSearch() {
        pimPage.navigateToEmployeeReports();
        pimPage.enterReportNameSearch("Employee");
        pimPage.clickReportSearch();
        Assert.assertTrue(pimPage.getReportTableRowCount() > 0,
                "Table should show results after searching before reset");
        pimPage.clickReportReset();
        Assert.assertTrue(pimPage.getReportTableRowCount() > 0,
                "Reports table should show all records after reset");
    }

//     @Test(description = "TC_P_12 - Add employee report", groups = {"PIM Module"})
//     public void testAddEmployeeReport() {
//         pimPage.navigateToEmployeeReports();
//         String uniqueReportName = "AutoReport" + System.currentTimeMillis();
//         pimPage.clickAddReport();
//         pimPage.enterReportName(uniqueReportName);
//         pimPage.addMinimumReportFields();
//         pimPage.clickReportSave();
//         Assert.assertTrue(pimPage.isReportSaveSuccessful(),
//                 "Should navigate to Employee Reports list after saving new report");
//     }

//     @Test(description = "TC_P_13 - Edit employee report", groups = {"PIM Module"})
//     public void testEditEmployeeReport() {
//         pimPage.navigateToEmployeeReports();
//         String uniqueReportName = "EditReport" + System.currentTimeMillis();
//         pimPage.clickAddReport();
//         pimPage.enterReportName(uniqueReportName);
//         pimPage.addMinimumReportFields();
//         pimPage.clickReportSave();
//         Assert.assertTrue(pimPage.isReportSaveSuccessful(),
//                 "Report should be created before editing");

//         pimPage.navigateToEmployeeReports();
//         pimPage.enterReportNameSearch(uniqueReportName);
//         pimPage.clickReportSearch();
//         pimPage.clickEditFirstReport();
//         Assert.assertTrue(pimPage.isEditReportPageDisplayed(),
//                 "Edit Report page should be displayed");

//         pimPage.editReportName("Updated" + System.currentTimeMillis());
//         pimPage.clickReportSave();
//         Assert.assertTrue(pimPage.isReportSaveSuccessful(),
//                 "Should return to reports list after saving edited report");
//     }

//     @Test(description = "TC_P_14 - Delete employee report", groups = {"PIM Module"})
//     public void testDeleteEmployeeReport() {
//         pimPage.navigateToEmployeeReports();
//         String uniqueReportName = "DelReport" + System.currentTimeMillis();
//         pimPage.clickAddReport();
//         pimPage.enterReportName(uniqueReportName);
//         pimPage.addMinimumReportFields();
//         pimPage.clickReportSave();
//         Assert.assertTrue(pimPage.isReportSaveSuccessful(),
//                 "Report should be created before deletion");

//         pimPage.navigateToEmployeeReports();
//         pimPage.enterReportNameSearch(uniqueReportName);
//         pimPage.clickReportSearch();
//         pimPage.deleteFirstReport();
//         Assert.assertTrue(pimPage.isSuccessToastDisplayed(),
//                 "Success toast should appear after deleting a report");
//     }

//     @Test(description = "TC_P_15 - View employee report", groups = {"PIM Module"})
//     public void testViewEmployeeReport() {
//         pimPage.navigateToEmployeeReports();
//         String uniqueReportName = "ViewReport" + System.currentTimeMillis();
//         pimPage.clickAddReport();
//         pimPage.enterReportName(uniqueReportName);
//         pimPage.addMinimumReportFields();
//         pimPage.clickReportSave();
//         Assert.assertTrue(pimPage.isReportSaveSuccessful(),
//                 "Report should be created before viewing");

//         pimPage.navigateToEmployeeReports();
//         pimPage.enterReportNameSearch(uniqueReportName);
//         pimPage.clickReportSearch();
//         pimPage.clickViewFirstReport();
//         Assert.assertTrue(pimPage.isViewReportDisplayed(),
//                 "Report results page should be displayed after clicking View");
//     }
}
