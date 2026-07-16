package com.orangehrm.test;

import java.io.File;

import org.testng.Assert;
import org.testng.SkipException;
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
            Assert.assertTrue(myInfoPage.isTabActive(tabName),
                    tabName + " tab should become active after clicking it");
        }
    }

    @Test(description = "TC_MI_03 - Update every field in Personal Details section", groups = {"My Info Module"})
    public void testUpdatePersonalDetailsFields() {
        String unique = String.valueOf(System.currentTimeMillis());

        myInfoPage.editFirstName("First" + unique);
        myInfoPage.editMiddleName("Middle" + unique);
        myInfoPage.editLastName("Last" + unique);
        myInfoPage.editOtherId("OID" + unique);
        myInfoPage.editDriverLicenseNumber("DL" + unique);
        myInfoPage.editDateOfBirth("1990-15-01");
        myInfoPage.selectNationality(null);
        myInfoPage.selectMaritalStatus(null);
        myInfoPage.selectGenderMale();

        myInfoPage.clickSave();

        Assert.assertTrue(myInfoPage.isSuccessToastDisplayed(),
                "Success toast should appear after updating all Personal Details fields");
    }

    @Test(description = "TC_MI_04 - Update Custom Fields section", groups = {"My Info Module"})
    public void testUpdateCustomFields() {
        if (!myInfoPage.isCustomFieldsSectionDisplayed()) {
            throw new SkipException("No Custom Fields are configured on this OrangeHRM instance");
        }

        String unique = String.valueOf(System.currentTimeMillis());
        int updatedCount = myInfoPage.updateAllCustomFields("Custom" + unique + "_");
        if (updatedCount == 0) {
            throw new SkipException("Custom Fields section is displayed but no fields are currently configured "
                    + "(this shared public demo's admin-configured fields can change between runs)");
        }

        myInfoPage.clickSave();
        Assert.assertTrue(myInfoPage.isSuccessToastDisplayed(),
                "Success toast should appear after updating custom fields");
    }

    @Test(description = "TC_MI_05 - Add attachment in Personal Details section", groups = {"My Info Module"})
    public void testAddAttachment() {
        Assert.assertTrue(myInfoPage.isAttachmentsSectionDisplayed(),
                "Attachments section should be visible on the Personal Details page");

        myInfoPage.clickAddAttachment();

        File attachmentFile = new File("src/test/resources/testfiles/sample-attachment.txt");
        myInfoPage.uploadAttachmentFile(attachmentFile.getAbsolutePath());
        myInfoPage.enterAttachmentComment("Uploaded by automated test " + System.currentTimeMillis());
        myInfoPage.clickAttachmentSave();

        Assert.assertTrue(myInfoPage.isSuccessToastDisplayed(),
                "Success toast should appear after uploading an attachment");
    }
}
