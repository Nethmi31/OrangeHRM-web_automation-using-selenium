package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.RecruitmentPage;

public class RecruitmentTest extends BaseTest {

    private RecruitmentPage recruitmentPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToRecruitment() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.Login("Admin", "admin123");
        loginPage.isDashboardVisible();
        recruitmentPage = new RecruitmentPage(driver);
        recruitmentPage.navigateToRecruitment();
    }

    @Test(description = "TC_RC_01 - Navigate to Recruitment module", groups = {"Recruitment Module"})
    public void testNavigateToRecruitmentModule() {
        Assert.assertTrue(recruitmentPage.isRecruitmentPageDisplayed(),
                "Candidates page should load after clicking Recruitment in the sidebar");
    }

    @Test(description = "TC_RC_02 - Search candidate by Job Title", groups = {"Recruitment Module"})
    public void testSearchCandidateByJobTitle() {
        recruitmentPage.selectJobTitle(null);
        recruitmentPage.clickSearch();
        boolean hasRecords = recruitmentPage.getTableRowCount() > 0;
        boolean noRecords = recruitmentPage.isNoRecordsMessageDisplayed();
        Assert.assertTrue(hasRecords || noRecords,
                "Candidate list should show matching records or 'No Records Found' after filtering by Job Title");
    }

    @Test(description = "TC_RC_03 - Search candidate by Vacancy", groups = {"Recruitment Module"})
    public void testSearchCandidateByVacancy() {
        recruitmentPage.selectVacancy(null);
        recruitmentPage.clickSearch();
        boolean hasRecords = recruitmentPage.getTableRowCount() > 0;
        boolean noRecords = recruitmentPage.isNoRecordsMessageDisplayed();
        Assert.assertTrue(hasRecords || noRecords,
                "Candidate list should show matching records or 'No Records Found' after filtering by Vacancy");
    }

    @Test(description = "TC_RC_04 - Search candidate by Hiring Manager", groups = {"Recruitment Module"})
    public void testSearchCandidateByHiringManager() {
        recruitmentPage.selectHiringManager(null);
        recruitmentPage.clickSearch();
        boolean hasRecords = recruitmentPage.getTableRowCount() > 0;
        boolean noRecords = recruitmentPage.isNoRecordsMessageDisplayed();
        Assert.assertTrue(hasRecords || noRecords,
                "Candidate list should show matching records or 'No Records Found' after filtering by Hiring Manager");
    }

    @Test(description = "TC_RC_05 - Search candidate by Status", groups = {"Recruitment Module"})
    public void testSearchCandidateByStatus() {
        recruitmentPage.selectStatus(null);
        recruitmentPage.clickSearch();
        boolean hasRecords = recruitmentPage.getTableRowCount() > 0;
        boolean noRecords = recruitmentPage.isNoRecordsMessageDisplayed();
        Assert.assertTrue(hasRecords || noRecords,
                "Candidate list should show matching records or 'No Records Found' after filtering by Status");
    }

    @Test(description = "TC_RC_06 - Search candidate by Candidate Name", groups = {"Recruitment Module"})
    public void testSearchCandidateByCandidateName() {
        recruitmentPage.enterCandidateName("a");
        recruitmentPage.clickSearch();
        boolean hasRecords = recruitmentPage.getTableRowCount() > 0;
        boolean noRecords = recruitmentPage.isNoRecordsMessageDisplayed();
        Assert.assertTrue(hasRecords || noRecords,
                "Candidate list should show matching records or 'No Records Found' after filtering by Candidate Name");
    }

    @Test(description = "TC_RC_07 - Search candidate by Keywords", groups = {"Recruitment Module"})
    public void testSearchCandidateByKeywords() {
        recruitmentPage.enterKeywords("java");
        recruitmentPage.clickSearch();
        boolean hasRecords = recruitmentPage.getTableRowCount() > 0;
        boolean noRecords = recruitmentPage.isNoRecordsMessageDisplayed();
        Assert.assertTrue(hasRecords || noRecords,
                "Candidate list should show matching records or 'No Records Found' after filtering by Keywords");
    }

    @Test(description = "TC_RC_08 - Search candidate by Date of Application range", groups = {"Recruitment Module"})
    public void testSearchCandidateByDateOfApplicationRange() {
        recruitmentPage.enterFromDate(RecruitmentPage.getPastDate(90));
        recruitmentPage.enterToDate(RecruitmentPage.getFutureDate(1));
        recruitmentPage.clickSearch();
        boolean hasRecords = recruitmentPage.getTableRowCount() > 0;
        boolean noRecords = recruitmentPage.isNoRecordsMessageDisplayed();
        Assert.assertTrue(hasRecords || noRecords,
                "Candidate list should show records or 'No Records Found' for the specified Date of Application range");
    }

    @Test(description = "TC_RC_09 - Search candidate by Method of Application", groups = {"Recruitment Module"})
    public void testSearchCandidateByMethodOfApplication() {
        recruitmentPage.selectMethodOfApplication(null);
        recruitmentPage.clickSearch();
        boolean hasRecords = recruitmentPage.getTableRowCount() > 0;
        boolean noRecords = recruitmentPage.isNoRecordsMessageDisplayed();
        Assert.assertTrue(hasRecords || noRecords,
                "Candidate list should show matching records or 'No Records Found' after filtering by Method of Application");
    }

    @Test(description = "TC_RC_10 - Search candidate using a combination of all filters", groups = {"Recruitment Module"})
    public void testSearchCandidateUsingAllFilters() {
        recruitmentPage.selectJobTitle(null);
        recruitmentPage.selectVacancy(null);
        recruitmentPage.selectHiringManager(null);
        recruitmentPage.selectStatus(null);
        recruitmentPage.enterCandidateName("a");
        recruitmentPage.enterKeywords("java");
        recruitmentPage.enterFromDate(RecruitmentPage.getPastDate(90));
        recruitmentPage.enterToDate(RecruitmentPage.getFutureDate(1));
        recruitmentPage.selectMethodOfApplication(null);
        recruitmentPage.clickSearch();
        boolean hasRecords = recruitmentPage.getTableRowCount() > 0;
        boolean noRecords = recruitmentPage.isNoRecordsMessageDisplayed();
        Assert.assertTrue(hasRecords || noRecords,
                "Candidate list should show matching records or 'No Records Found' after filtering by all available options combined");
    }

    @Test(description = "TC_RC_11 - Reset search filters", groups = {"Recruitment Module"})
    public void testResetSearchFilters() {
        recruitmentPage.enterKeywords("java");
        recruitmentPage.clickSearch();
        recruitmentPage.clickReset();
        boolean hasRecords = recruitmentPage.getTableRowCount() > 0;
        boolean noRecords = recruitmentPage.isNoRecordsMessageDisplayed();
        Assert.assertTrue(hasRecords || noRecords,
                "Candidate list should reload after Reset is clicked");
    }
}
