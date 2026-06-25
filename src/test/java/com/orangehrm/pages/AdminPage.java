package com.orangehrm.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdminPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private WebDriverWait shortWait;

    // Sidebar
    private By adminMenuLink = By.xpath("//span[text()='Admin']/ancestor::a");

    // Page header
    private By pageHeader = By.xpath("//h5[text()='System Users']");
    private By topBarHeader = By.xpath("//h6[text()='Admin']");

    // Search form
    private By usernameSearchField = By.xpath(
            "//label[text()='Username']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By searchButton = By.xpath("//button[@type='submit']");
    private By resetButton = By.xpath("//button[normalize-space()='Reset']");

    // Results table
    private By tableRows = By.xpath("//div[@class='oxd-table-body']//div[@role='row']");
    private By noRecordsMessage = By.xpath("//span[text()='No Records Found']");
    private By recordCount = By.xpath("//span[contains(@class,'oxd-text') and contains(text(),'Record')]");

    // Add user
    private By addButton = By.xpath("//button[normalize-space()='Add']");
    private By userRoleDropdown = By.xpath(
            "//label[text()='User Role']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");
    private By statusDropdown = By.xpath(
            "//label[text()='Status']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");
    private By employeeNameField = By.xpath(
            "//label[text()='Employee Name']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By usernameField = By.xpath(
            "//label[text()='Username']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By passwordField = By.xpath(
            "//label[text()='Password']/ancestor::div[contains(@class,'oxd-input-group')]//input[@type='password']");
    private By confirmPasswordField = By.xpath(
            "//label[text()='Confirm Password']/ancestor::div[contains(@class,'oxd-input-group')]//input[@type='password']");
    private By saveButton = By.xpath("//button[@type='submit']");
    private By dropdownOptions = By.xpath("//div[@role='listbox']//div[@role='option']");
    private By autocompleteOptions = By.xpath("//div[contains(@class,'oxd-autocomplete-option')]");

    // Validation & toasts
    private By requiredErrors = By.xpath("//span[contains(@class,'oxd-input-field-error-message')]");
    private By successToast = By.cssSelector(".oxd-toast");
    private By alreadyExistsError = By.xpath(
            "//label[text()='Username']/ancestor::div[contains(@class,'oxd-input-group')]//span[contains(@class,'oxd-input-field-error-message')]");
    private By passwordMismatchError = By.xpath(
            "//label[text()='Confirm Password']/ancestor::div[contains(@class,'oxd-input-group')]//span[contains(@class,'oxd-input-field-error-message')]");

    // Delete
    private By deleteCheckbox = By.xpath(
            "//div[@class='oxd-table-body']//div[@role='row'][1]//div[contains(@class,'oxd-table-cell-actions')]//i[contains(@class,'bi-trash')]");
    private By confirmDeleteButton = By.xpath("//button[normalize-space()='Yes, Delete']");

    // Job Titles
    private By topBarJobMenu = By.xpath("//li[contains(@class,'oxd-topbar-body-nav-tab')]//span[normalize-space()='Job']");
    private By jobTitlesLink = By.xpath("//a[normalize-space()='Job Titles']");
    private By jobTitlesHeader = By.xpath("//*[self::h5 or self::h6][normalize-space()='Job Titles']");
    private By jobTitlesTable = By.xpath("//div[@class='oxd-table-body']");

    public AdminPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(7));
    }

    public void navigateToAdmin() {
        wait.until(ExpectedConditions.elementToBeClickable(adminMenuLink)).click();
    }

    public boolean isAdminPageDisplayed() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(topBarHeader));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeader)).isDisplayed();
    }

    // --- Search ---

    public void searchByUsername(String username) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameSearchField));
        field.clear();
        field.sendKeys(username);
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
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

    public boolean isRecordCountDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(recordCount)).isDisplayed();
    }

    public void clickReset() {
        wait.until(ExpectedConditions.elementToBeClickable(resetButton)).click();
    }

    public String getSearchFieldValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameSearchField))
                .getAttribute("value");
    }

    // --- Add User ---

    public void clickAdd() {
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[text()='Add User']")));
    }

    public void selectUserRole(String role) {
        wait.until(ExpectedConditions.elementToBeClickable(userRoleDropdown)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptions));
        List<WebElement> options = driver.findElements(dropdownOptions);
        for (WebElement option : options) {
            if (option.getText().equals(role)) {
                option.click();
                break;
            }
        }
    }

    public void selectStatus(String status) {
        wait.until(ExpectedConditions.elementToBeClickable(statusDropdown)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptions));
        List<WebElement> options = driver.findElements(dropdownOptions);
        for (WebElement option : options) {
            if (option.getText().equals(status)) {
                option.click();
                break;
            }
        }
    }

    public void enterEmployeeName(String name) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeNameField));
        field.click();
        field.clear();
        field.sendKeys(name);

        // Wait for autocomplete dropdown with actual employee names
        By validOption = By.xpath(
                "//div[contains(@class,'oxd-autocomplete-option')]//span");
        try {
            shortWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(validOption, 0));
        } catch (Exception e) {
            // Retry with broader search term
            field.clear();
            field.sendKeys("an");
            shortWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(validOption, 0));
        }

        // Small pause to let the dropdown fully render
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        List<WebElement> suggestions = driver.findElements(autocompleteOptions);
        for (WebElement suggestion : suggestions) {
            String text = suggestion.getText().trim();
            if (!text.isEmpty() && !text.equalsIgnoreCase("No Records Found")
                    && !text.equalsIgnoreCase("Searching...")) {
                suggestion.click();
                // Wait for dropdown to close (confirms selection registered)
                shortWait.until(ExpectedConditions.invisibilityOfElementLocated(autocompleteOptions));
                return;
            }
        }
    }

    public void enterUsername(String username) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        field.clear();
        field.sendKeys(username);
        field.sendKeys(Keys.TAB);
    }

    public void enterPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        field.clear();
        field.sendKeys(password);
    }

    public void enterConfirmPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordField));
        field.clear();
        field.sendKeys(password);
        field.sendKeys(Keys.TAB);
    }

    public void clickSave() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
        btn.click();
    }

    public void fillAddUserForm(String role, String status, String employeeName,
                                String username, String password) {
        selectUserRole(role);
        selectStatus(status);
        enterEmployeeName(employeeName);
        enterUsername(username);
        enterPassword(password);
        enterConfirmPassword(password);
    }

    // --- Validation ---

    public int getRequiredErrorCount() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(requiredErrors, 0));
        return driver.findElements(requiredErrors).size();
    }

    public boolean isSuccessToastDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successToast)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSaveSuccessful() {
        try {
            return wait.until(ExpectedConditions.urlContains("viewSystemUsers"));
        } catch (Exception e) {
            return isSuccessToastDisplayed();
        }
    }

    public boolean isAlreadyExistsErrorDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(alreadyExistsError)).isDisplayed();
    }

    public boolean isPasswordMismatchErrorDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordMismatchError))
                .isDisplayed();
    }

    // --- Delete ---

    public void deleteFirstUserByTrashIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(deleteCheckbox)).click();
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButton)).click();
    }

    // --- Job Titles ---

    public void navigateToJobTitles() {
        wait.until(ExpectedConditions.elementToBeClickable(topBarJobMenu)).click();
        wait.until(ExpectedConditions.elementToBeClickable(jobTitlesLink)).click();
    }

    public boolean isJobTitlesPageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(jobTitlesHeader)).isDisplayed();
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("jobTitle");
        }
    }

    public boolean isJobTitlesTableDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(jobTitlesTable)).isDisplayed();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
