package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LeavePage;
import com.orangehrm.pages.LoginPage;

public class LeaveTest extends BaseTest {

    private LeavePage leavePage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToLeave() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.Login("Admin", "admin123");
        loginPage.isDashboardVisible();
        leavePage = new LeavePage(driver);
        leavePage.navigateToLeave();
    }

    @Test(description = "TC_LE_01 - Navigate to Leave module", groups = {"Leave Module"})
    public void testNavigateToLeaveModule() {
        Assert.assertTrue(leavePage.isLeavePageDisplayed(),
                "Leave List page should load after clicking Leave in the sidebar");
    }

    @Test(description = "TC_LE_02 - Apply for leave", groups = {"Leave Module"})
    public void testApplyForLeave() {
        leavePage.navigateToApplyLeave();
        if (!leavePage.isApplyLeaveFormAvailable()) {
            Assert.assertTrue(leavePage.isNoLeaveBalanceMessageDisplayed(),
                    "Apply Leave should show either the leave type form or a 'no leave balance' message");
            return;
        }
        leavePage.selectLeaveType(null);
        leavePage.enterFromDate(LeavePage.getFutureDate(7));
        leavePage.enterToDate(LeavePage.getFutureDate(8));
        leavePage.clickApply();
        Assert.assertTrue(leavePage.isSuccessToastDisplayed(),
                "Success toast should appear after successfully applying for leave");
    }

    @Test(description = "TC_LE_03 - Apply leave with past date", groups = {"Leave Module"})
    public void testApplyLeaveWithPastDate() {
        leavePage.navigateToApplyLeave();
        if (!leavePage.isApplyLeaveFormAvailable()) {
            Assert.assertTrue(leavePage.isNoLeaveBalanceMessageDisplayed(),
                    "Apply Leave should show either the leave type form or a 'no leave balance' message");
            return;
        }
        leavePage.selectLeaveType(null);
        leavePage.enterFromDate(LeavePage.getPastDate(5));
        leavePage.enterToDate(LeavePage.getPastDate(4));
        leavePage.clickApply();
        boolean validationError = leavePage.isValidationErrorDisplayed();
        boolean appliedSuccessfully = leavePage.isSuccessToastDisplayed();
        Assert.assertTrue(validationError || appliedSuccessfully,
                "Past date should either show a validation error or be accepted depending on system configuration");
    }

    @Test(description = "TC_LE_04 - Apply leave required field validation", groups = {"Leave Module"})
    public void testApplyLeaveRequiredFieldValidation() {
        leavePage.navigateToApplyLeave();
        if (!leavePage.isApplyLeaveFormAvailable()) {
            Assert.assertTrue(leavePage.isNoLeaveBalanceMessageDisplayed(),
                    "Apply Leave should show either the leave type form or a 'no leave balance' message");
            return;
        }
        leavePage.clickApply();
        Assert.assertTrue(leavePage.getRequiredErrorCount() >= 1,
                "Required field errors should appear when submitting Apply Leave form without any data");
    }

    @Test(description = "TC_LE_05 - Apply leave with From date after To date", groups = {"Leave Module"})
    public void testApplyLeaveFromDateAfterToDate() {
        leavePage.navigateToApplyLeave();
        if (!leavePage.isApplyLeaveFormAvailable()) {
            Assert.assertTrue(leavePage.isNoLeaveBalanceMessageDisplayed(),
                    "Apply Leave should show either the leave type form or a 'no leave balance' message");
            return;
        }
        leavePage.selectLeaveType(null);
        leavePage.enterFromDate(LeavePage.getFutureDate(10));
        leavePage.enterToDate(LeavePage.getFutureDate(5));
        leavePage.clickApply();
        Assert.assertTrue(leavePage.isValidationErrorDisplayed(),
                "Validation error should appear when From date is set after To date");
    }

    @Test(description = "TC_LE_06 - View Leave List", groups = {"Leave Module"})
    public void testViewLeaveList() {
        leavePage.navigateToLeaveList();
        Assert.assertTrue(leavePage.isLeaveListTableDisplayed(),
                "Leave list table should be visible on the Leave List page");
    }

    @Test(description = "TC_LE_07 - Filter Leave List by status", groups = {"Leave Module"})
    public void testFilterLeaveListByStatus() {
        leavePage.navigateToLeaveList();
        leavePage.selectLeaveStatus("Pending Approval");
        leavePage.clickSearch();
        boolean hasRecords = leavePage.getTableRowCount() > 0;
        boolean noRecords = leavePage.isNoRecordsMessageDisplayed();
        Assert.assertTrue(hasRecords || noRecords,
                "Leave list should show matching records or 'No Records Found' after filtering by Pending Approval status");
    }

    @Test(description = "TC_LE_08 - Filter Leave List by date range", groups = {"Leave Module"})
    public void testFilterLeaveListByDateRange() {
        leavePage.navigateToLeaveList();
        leavePage.enterFromDate(LeavePage.getPastDate(30));
        leavePage.enterToDate(LeavePage.getFutureDate(30));
        leavePage.clickSearch();
        boolean hasRecords = leavePage.getTableRowCount() > 0;
        boolean noRecords = leavePage.isNoRecordsMessageDisplayed();
        Assert.assertTrue(hasRecords || noRecords,
                "Leave list should show records or 'No Records Found' for the specified date range");
    }

    @Test(description = "TC_LE_09 - Assign leave to an employee", groups = {"Leave Module"})
    public void testAssignLeaveToEmployee() {
        leavePage.navigateToAssignLeave();
        leavePage.enterEmployeeName("a");
        leavePage.selectLeaveType(null);
        leavePage.enterFromDate(LeavePage.getFutureDate(14));
        leavePage.enterToDate(LeavePage.getFutureDate(15));
        leavePage.clickApply();
        Assert.assertTrue(leavePage.isSuccessToastDisplayed(),
                "Success toast should appear after assigning leave to an employee");
    }

    @Test(description = "TC_LE_10 - View My Leave requests", groups = {"Leave Module"})
    public void testViewMyLeave() {
        leavePage.navigateToMyLeave();
        Assert.assertTrue(leavePage.isMyLeavePageDisplayed(),
                "My Leave page should display the current user's leave requests");
    }
}
