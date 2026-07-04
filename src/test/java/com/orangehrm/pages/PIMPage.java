package com.orangehrm.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PIMPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private WebDriverWait shortWait;

    // Sidebar
    private By pimMenuLink = By.xpath("//span[text()='PIM']/ancestor::a");

    // Page headers
    private By topBarHeader = By.xpath("//h6[text()='PIM']");
    private By employeeListHeader = By.xpath("//h5[text()='Employee Information']");

    // Employee search form
    private By employeeNameSearch = By.xpath(
            "//label[text()='Employee Name']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By employeeIdSearch = By.xpath(
            "//label[text()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By employmentStatusDropdown = By.xpath(
            "//label[text()='Employment Status']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");
    private By jobTitleDropdown = By.xpath(
            "//label[text()='Job Title']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");
    private By subUnitDropdown = By.xpath(
            "//label[text()='Sub Unit']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");
    private By includeDropdown = By.xpath(
            "//label[text()='Include']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");
    private By supervisorNameSearch = By.xpath(
            "//label[text()='Supervisor Name']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By searchButton = By.xpath("//button[@type='submit']");
    private By resetButton = By.xpath("//button[normalize-space()='Reset']");

    // Results table
    private By tableRows = By.xpath("//div[@class='oxd-table-body']//div[@role='row']");
    private By noRecordsMessage = By.xpath("//span[text()='No Records Found']");
    private By dropdownOptions = By.xpath("//div[@role='listbox']//div[@role='option']");
    private By autocompleteOptions = By.xpath("//div[contains(@class,'oxd-autocomplete-option')]");

    // Add employee form
    private By addButton = By.xpath("//button[normalize-space()='Add']");
    private By firstNameField = By.xpath("//input[@name='firstName']");
    private By middleNameField = By.xpath("//input[@name='middleName']");
    private By lastNameField = By.xpath("//input[@name='lastName']");
    private By employeeIdField = By.xpath("//div[contains(@class,'emp-number')]//input");
    private By saveButton = By.xpath("//button[@type='submit']");
    private By personalDetailsHeader = By.xpath("//h6[text()='Personal Details']");

    // Edit/Delete employee actions (first row)
    private By editEmployeeIcon = By.xpath(
            "//div[@class='oxd-table-body']//div[@role='row'][1]//div[contains(@class,'oxd-table-cell-actions')]//i[contains(@class,'bi-pencil')]");
    private By deleteEmployeeIcon = By.xpath(
            "//div[@class='oxd-table-body']//div[@role='row'][1]//div[contains(@class,'oxd-table-cell-actions')]//i[contains(@class,'bi-trash')]");
    private By confirmDeleteButton = By.xpath("//button[normalize-space()='Yes, Delete']");
    private By successToast = By.cssSelector(".oxd-toast");

    // Validation errors
    private By requiredErrors = By.xpath("//span[contains(@class,'oxd-input-field-error-message')]");

    // Reports navigation (PIM topbar)
    private By reportsMenuLink = By.xpath(
            "//nav[contains(@class,'oxd-topbar-body-nav')]//a[normalize-space()='Reports']");
    private By reportsPageHeader = By.xpath("//h5[text()='Employee Reports']");

    // Report search
    private By reportNameSearch = By.xpath(
            "//label[text()='Report Name']/ancestor::div[contains(@class,'oxd-input-group')]//input");

    // Add/Edit report form
    private By reportNameField = By.xpath(
            "//label[text()='Report Name']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By addReportSaveButton = By.xpath("//button[@type='submit']");

    // Report - Selection Criteria section
    // The "Selection Criteria" dropdown (-- Select --)
    private By selectionCriteriaDropdown = By.xpath(
            "(//div[contains(@class,'oxd-select-wrapper')]//div[contains(@class,'oxd-select-text')])[1]");
    // The + button next to the criteria dropdown
    private By addCriteriaButton = By.xpath(
            "(//button[.//i[contains(@class,'bi-plus')]])[1]");

    // Report - Display Fields section
    // "Select Display Field Group" dropdown
    private By displayFieldGroupDropdown = By.xpath(
            "//label[normalize-space()='Select Display Field Group']"
            + "/ancestor::div[contains(@class,'oxd-input-group')]"
            + "//div[contains(@class,'oxd-select-text')]");
    // "Select Display Field" dropdown
    private By displayFieldDropdown = By.xpath(
            "//label[normalize-space()='Select Display Field']"
            + "/ancestor::div[contains(@class,'oxd-input-group')]"
            + "//div[contains(@class,'oxd-select-text')]");
    // + button next to the Display Field dropdown (last + on the page)
    private By addDisplayFieldButton = By.xpath(
            "(//button[.//i[contains(@class,'bi-plus')]])[last()]");

    // Report table actions (first row)
    private By viewReportIcon = By.xpath(
            "//div[@class='oxd-table-body']//div[@role='row'][1]//div[contains(@class,'oxd-table-cell-actions')]//i[contains(@class,'bi-eye')]");
    private By editReportIcon = By.xpath(
            "//div[@class='oxd-table-body']//div[@role='row'][1]//div[contains(@class,'oxd-table-cell-actions')]//i[contains(@class,'bi-pencil')]");
    private By deleteReportIcon = By.xpath(
            "//div[@class='oxd-table-body']//div[@role='row'][1]//div[contains(@class,'oxd-table-cell-actions')]//i[contains(@class,'bi-trash')]");

    public PIMPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(7));
    }

    // --- Navigation ---

    public void navigateToPIM() {
        wait.until(ExpectedConditions.elementToBeClickable(pimMenuLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(employeeListHeader));
    }

    public boolean isPIMPageDisplayed() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(topBarHeader));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(employeeListHeader)).isDisplayed();
    }

    public void navigateToEmployeeReports() {
        wait.until(ExpectedConditions.elementToBeClickable(reportsMenuLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(reportsPageHeader));
    }

    public boolean isReportsPageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(reportsPageHeader)).isDisplayed();
    }

    // --- Employee Search ---

    public void enterSearchEmployeeName(String name) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeNameSearch));
        field.click();
        field.clear();
        field.sendKeys(name);

        By validOption = By.xpath("//div[contains(@class,'oxd-autocomplete-option')]//span");
        try {
            shortWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(validOption, 0));
        } catch (Exception e) {
            field.clear();
            field.sendKeys("an");
            shortWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(validOption, 0));
        }

        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        List<WebElement> suggestions = driver.findElements(autocompleteOptions);
        for (WebElement suggestion : suggestions) {
            String text = suggestion.getText().trim();
            if (!text.isEmpty() && !text.equalsIgnoreCase("No Records Found")
                    && !text.equalsIgnoreCase("Searching...")) {
                suggestion.click();
                shortWait.until(ExpectedConditions.invisibilityOfElementLocated(autocompleteOptions));
                return;
            }
        }
    }

    public void enterSearchEmployeeId(String id) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeIdSearch));
        field.clear();
        field.sendKeys(id);
    }

    public void searchSelectEmploymentStatus(String status) {
        wait.until(ExpectedConditions.elementToBeClickable(employmentStatusDropdown)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptions));
        List<WebElement> options = driver.findElements(dropdownOptions);
        for (WebElement option : options) {
            if (option.getText().equals(status)) {
                option.click();
                break;
            }
        }
    }

    public void searchSelectJobTitle(String title) {
        wait.until(ExpectedConditions.elementToBeClickable(jobTitleDropdown)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptions));
        List<WebElement> options = driver.findElements(dropdownOptions);
        for (WebElement option : options) {
            if (option.getText().equals(title)) {
                option.click();
                break;
            }
        }
    }

    public void searchSelectSubUnit(String subUnit) {
        wait.until(ExpectedConditions.elementToBeClickable(subUnitDropdown)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptions));
        List<WebElement> options = driver.findElements(dropdownOptions);
        for (WebElement option : options) {
            if (option.getText().equals(subUnit)) {
                option.click();
                break;
            }
        }
    }

    public void searchSelectInclude(String include) {
        wait.until(ExpectedConditions.elementToBeClickable(includeDropdown)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptions));
        List<WebElement> options = driver.findElements(dropdownOptions);
        for (WebElement option : options) {
            if (option.getText().equals(include)) {
                option.click();
                break;
            }
        }
    }

    public void enterSearchSupervisorName(String name) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(supervisorNameSearch));
        field.click();
        field.clear();
        field.sendKeys(name);

        By validOption = By.xpath("//div[contains(@class,'oxd-autocomplete-option')]//span");
        try {
            shortWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(validOption, 0));
        } catch (Exception e) {
            field.clear();
            field.sendKeys("an");
            shortWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(validOption, 0));
        }

        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        List<WebElement> suggestions = driver.findElements(autocompleteOptions);
        for (WebElement suggestion : suggestions) {
            String text = suggestion.getText().trim();
            if (!text.isEmpty() && !text.equalsIgnoreCase("No Records Found")
                    && !text.equalsIgnoreCase("Searching...")) {
                suggestion.click();
                shortWait.until(ExpectedConditions.invisibilityOfElementLocated(autocompleteOptions));
                return;
            }
        }
    }

    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    public void clickReset() {
        wait.until(ExpectedConditions.elementToBeClickable(resetButton)).click();
    }

    public int getTableRowCount() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='oxd-table-body']")));
            List<WebElement> rows = driver.findElements(tableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isNoRecordsMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(noRecordsMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmployeeIdSearchFieldValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(employeeIdSearch))
                .getAttribute("value");
    }

    // --- Add Employee ---

    public void clickAddEmployee() {
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
    }

    public void enterFirstName(String firstName) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        field.clear();
        field.sendKeys(firstName);
    }

    public void enterMiddleName(String middleName) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(middleNameField));
        field.clear();
        field.sendKeys(middleName);
    }

    public void enterLastName(String lastName) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField));
        field.clear();
        field.sendKeys(lastName);
    }

    public void enterEmployeeId(String id) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeIdField));
        field.clear();
        field.sendKeys(id);
        field.sendKeys(Keys.TAB);
    }

    public void clickSave() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector(".oxd-form-loader")));
        } catch (Exception ignored) {}
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
        btn.click();
    }

    public boolean isAddEmployeeSuccessful() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader)).isDisplayed();
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("viewPersonalDetails");
        }
    }

    public int getRequiredErrorCount() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(requiredErrors, 0));
        return driver.findElements(requiredErrors).size();
    }

    // --- Edit Employee ---

    public void clickEditFirstEmployee() {
        wait.until(ExpectedConditions.elementToBeClickable(editEmployeeIcon)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader));
    }

    public boolean isEditEmployeePageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader)).isDisplayed();
    }

    public void editFirstName(String firstName) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        field.clear();
        field.sendKeys(firstName);
    }

    public void editLastName(String lastName) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField));
        field.clear();
        field.sendKeys(lastName);
    }

    // --- Delete Employee ---

    public void deleteFirstEmployee() {
        wait.until(ExpectedConditions.elementToBeClickable(deleteEmployeeIcon)).click();
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButton)).click();
    }

    public boolean isSuccessToastDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successToast)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // --- Employee Reports Search ---

    public void enterReportNameSearch(String name) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(reportNameSearch));
        field.click();
        field.clear();
        field.sendKeys(name);

        By validOption = By.xpath("//div[contains(@class,'oxd-autocomplete-option')]//span");
        try {
            shortWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(validOption, 0));
            try { Thread.sleep(400); } catch (InterruptedException ignored) {}
            List<WebElement> suggestions = driver.findElements(autocompleteOptions);
            for (WebElement suggestion : suggestions) {
                String text = suggestion.getText().trim();
                if (!text.isEmpty() && !text.equalsIgnoreCase("No Records Found")
                        && !text.equalsIgnoreCase("Searching...")) {
                    suggestion.click();
                    shortWait.until(ExpectedConditions.invisibilityOfElementLocated(autocompleteOptions));
                    return;
                }
            }
        } catch (Exception ignored) {}
    }

    public void clickReportSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    public void clickReportReset() {
        wait.until(ExpectedConditions.elementToBeClickable(resetButton)).click();
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    public int getReportTableRowCount() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='oxd-table-body']")));
            List<WebElement> rows = driver.findElements(tableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isNoReportsMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(noRecordsMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getReportNameSearchFieldValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(reportNameSearch))
                .getAttribute("value");
    }

    // --- Add Report ---

    public void clickAddReport() {
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(reportNameField));
    }

    public void enterReportName(String name) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(reportNameField));
        field.clear();
        field.sendKeys(name);
    }

    // Step 1: Select from the Selection Criteria dropdown; falls back to first non-default option
    public void selectSelectionCriteria(String criteriaName) {
        wait.until(ExpectedConditions.elementToBeClickable(selectionCriteriaDropdown)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptions));
        List<WebElement> options = driver.findElements(dropdownOptions);
        for (WebElement option : options) {
            if (option.getText().trim().equals(criteriaName)) {
                option.click();
                return;
            }
        }
        // Fallback: pick first non-placeholder option
        for (WebElement option : options) {
            String text = option.getText().trim();
            if (!text.isEmpty() && !text.equals("-- Select --")) {
                option.click();
                return;
            }
        }
    }

    // Step 2: Click the + button to add the criteria row and wait for input to appear
    public void clickAddCriteriaButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addCriteriaButton)).click();
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
    }

    // Step 3: Find the criteria value input using multiple strategies, type hint and select
    public void enterCriteriaEmployeeHint(String hint) {
        WebElement field = findCriteriaInput();
        if (field == null) return; // input not required — criteria row already added

        field.click();
        field.clear();
        field.sendKeys(hint);

        By validOption = By.xpath("//div[contains(@class,'oxd-autocomplete-option')]//span");
        try {
            shortWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(validOption, 0));
            try { Thread.sleep(400); } catch (InterruptedException ignored) {}
            List<WebElement> suggestions = driver.findElements(autocompleteOptions);
            for (WebElement suggestion : suggestions) {
                String text = suggestion.getText().trim();
                if (!text.isEmpty() && !text.equalsIgnoreCase("No Records Found")
                        && !text.equalsIgnoreCase("Searching...")) {
                    suggestion.click();
                    shortWait.until(ExpectedConditions.invisibilityOfElementLocated(autocompleteOptions));
                    return;
                }
            }
        } catch (Exception ignored) {}
    }

    private WebElement findCriteriaInput() {
        // Try strategies in order; return first visible input found
        By[] strategies = {
            By.xpath("//div[contains(@class,'oxd-autocomplete-wrapper')]//input[@type='text']"),
            By.cssSelector("input.oxd-autocomplete-text-input"),
            By.xpath("//div[contains(@class,'oxd-autocomplete')]//input"),
            // Any text input that is NOT the Report Name field (first input on the page)
            By.xpath("(//div[contains(@class,'oxd-form-row') or contains(@class,'oxd-input-group')]//input[@type='text'])[2]")
        };
        for (By locator : strategies) {
            try {
                WebElement el = shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                if (el != null && el.isDisplayed()) return el;
            } catch (Exception ignored) {}
        }
        return null;
    }

    // Step 4: Select Display Field Group dropdown; falls back to first non-default option
    public void selectDisplayFieldGroup(String groupName) {
        wait.until(ExpectedConditions.elementToBeClickable(displayFieldGroupDropdown)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptions));
        List<WebElement> options = driver.findElements(dropdownOptions);
        for (WebElement option : options) {
            if (option.getText().trim().equals(groupName)) {
                option.click();
                try { Thread.sleep(400); } catch (InterruptedException ignored) {}
                return;
            }
        }
        for (WebElement option : options) {
            String text = option.getText().trim();
            if (!text.isEmpty() && !text.equals("-- Select --")) {
                option.click();
                try { Thread.sleep(400); } catch (InterruptedException ignored) {}
                return;
            }
        }
    }

    // Step 5: Select Display Field dropdown; falls back to first non-default option
    public void selectDisplayField(String fieldName) {
        wait.until(ExpectedConditions.elementToBeClickable(displayFieldDropdown)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptions));
        List<WebElement> options = driver.findElements(dropdownOptions);
        for (WebElement option : options) {
            if (option.getText().trim().equals(fieldName)) {
                option.click();
                return;
            }
        }
        for (WebElement option : options) {
            String text = option.getText().trim();
            if (!text.isEmpty() && !text.equals("-- Select --")) {
                option.click();
                return;
            }
        }
    }

    // Step 6: Click + to add the selected display field
    public void clickAddDisplayFieldButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addDisplayFieldButton)).click();
        try { Thread.sleep(400); } catch (InterruptedException ignored) {}
    }

    // Full Add Report flow:
    // 1. Selection Criteria: pick type → click + → fill in value (autocomplete)
    // 2. Display Fields: pick group → pick field → click +
    public void addMinimumReportFields() {
        selectSelectionCriteria("Employee Name");
        clickAddCriteriaButton();
        try {
            enterCriteriaEmployeeHint("a");
        } catch (Exception ignored) {}

        selectDisplayFieldGroup("Personal Details");
        selectDisplayField("Employee Id");
        clickAddDisplayFieldButton();
    }

    public void clickReportSave() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(addReportSaveButton));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public boolean isReportSaveSuccessful() {
        try {
            return wait.until(ExpectedConditions.urlContains("viewDefinedReports"));
        } catch (Exception e) {
            return isSuccessToastDisplayed();
        }
    }

    // --- Edit Report ---

    public void clickEditFirstReport() {
        wait.until(ExpectedConditions.elementToBeClickable(editReportIcon)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(reportNameField));
    }

    public boolean isEditReportPageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(reportNameField)).isDisplayed();
    }

    public void editReportName(String name) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(reportNameField));
        field.clear();
        field.sendKeys(name);
    }

    // --- Delete Report ---

    public void deleteFirstReport() {
        wait.until(ExpectedConditions.elementToBeClickable(deleteReportIcon)).click();
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButton)).click();
    }

    // --- View Report ---

    public void clickViewFirstReport() {
        wait.until(ExpectedConditions.elementToBeClickable(viewReportIcon)).click();
    }

    public boolean isViewReportDisplayed() {
        try {
            return wait.until(ExpectedConditions.urlContains("displayReport"));
        } catch (Exception e) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h5[contains(@class,'oxd-text')]"))).isDisplayed();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
