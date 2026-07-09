package com.orangehrm.pages;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LeavePage {

    private WebDriver driver;
    private WebDriverWait wait;
    private WebDriverWait shortWait;

    // Sidebar
    private By leaveMenuLink = By.xpath("//span[text()='Leave']/ancestor::a");

    // Leave module topbar navigation
    private By leaveListMenuLink = By.xpath(
            "//nav[contains(@class,'oxd-topbar-body-nav')]//a[normalize-space()='Leave List']");
    private By applyMenuLink = By.xpath(
            "//nav[contains(@class,'oxd-topbar-body-nav')]//a[normalize-space()='Apply']");
    private By myLeaveMenuLink = By.xpath(
            "//nav[contains(@class,'oxd-topbar-body-nav')]//a[normalize-space()='My Leave']");
    private By assignLeaveMenuLink = By.xpath(
            "//nav[contains(@class,'oxd-topbar-body-nav')]//a[normalize-space()='Assign Leave']");

    // Page headers
    private By leaveListHeader = By.xpath("//h5[text()='Leave List']");
    private By applyLeaveHeader = By.xpath(
            "//*[self::h5 or self::h6][normalize-space()='Apply Leave']");
    private By myLeaveHeader = By.xpath("//h5[text()='My Leave']");
    private By assignLeaveHeader = By.xpath(
            "//*[self::h5 or self::h6][normalize-space()='Assign Leave']");

    // Apply / Assign Leave form
    private By leaveTypeDropdown = By.xpath(
            "//label[text()='Leave Type']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");
    private By fromDateInput = By.xpath(
            "//label[text()='From Date']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By toDateInput = By.xpath(
            "//label[text()='To Date']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By submitButton = By.xpath("//button[@type='submit']");

    // Assign Leave — Employee Name autocomplete
    private By employeeNameField = By.xpath(
            "//label[text()='Employee Name']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By autocompleteOptions = By.xpath(
            "//div[contains(@class,'oxd-autocomplete-option')]");

    // Leave List search form
    private By leaveStatusDropdown = By.xpath(
            "//label[text()='Leave Status']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");
    private By searchButton = By.xpath("//button[@type='submit']");
    private By resetButton = By.xpath("//button[normalize-space()='Reset']");

    // Dropdown options (standard select)
    private By dropdownOptions = By.xpath("//div[@role='listbox']//div[@role='option']");

    // Async form loading overlay (blocks clicks while leave types/data are fetched)
    private By formLoader = By.cssSelector(".oxd-form-loader");

    // Shown instead of the Apply/Assign Leave form when the employee has no
    // leave type configured with an available balance
    private By noLeaveBalanceMessage = By.xpath(
            "//p[contains(text(),'No Leave Types with Leave Balance')]");

    // Table
    private By tableBody = By.xpath("//div[@class='oxd-table-body']");
    private By tableRows = By.xpath("//div[@class='oxd-table-body']//div[@role='row']");
    private By noRecordsMessage = By.xpath("//span[text()='No Records Found']");

    // Validation / toasts
    private By requiredErrors = By.xpath(
            "//span[contains(@class,'oxd-input-field-error-message')]");
    private By successToast = By.cssSelector(".oxd-toast--success, .oxd-toast");
    private By errorToast = By.cssSelector(".oxd-toast--error");

    public LeavePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(7));
    }

    // ─── Navigation ────────────────────────────────────────────────────────────

    public void navigateToLeave() {
        wait.until(ExpectedConditions.elementToBeClickable(leaveMenuLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(leaveListHeader));
    }

    public boolean isLeavePageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(leaveListHeader)).isDisplayed();
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("leave");
        }
    }

    public void navigateToApplyLeave() {
        wait.until(ExpectedConditions.elementToBeClickable(applyMenuLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(applyLeaveHeader));
    }

    // False when OrangeHRM shows "No Leave Types with Leave Balance" instead
    // of the Apply Leave form (depends on the logged-in user's leave entitlements).
    public boolean isApplyLeaveFormAvailable() {
        try {
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(leaveTypeDropdown)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNoLeaveBalanceMessageDisplayed() {
        try {
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(noLeaveBalanceMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void navigateToLeaveList() {
        wait.until(ExpectedConditions.elementToBeClickable(leaveListMenuLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(leaveListHeader));
    }

    public void navigateToMyLeave() {
        wait.until(ExpectedConditions.elementToBeClickable(myLeaveMenuLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(myLeaveHeader));
    }

    public void navigateToAssignLeave() {
        wait.until(ExpectedConditions.elementToBeClickable(assignLeaveMenuLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(assignLeaveHeader));
    }

    // ─── Apply / Assign Leave form ─────────────────────────────────────────────

    // Waits out the .oxd-form-loader overlay (shown while OrangeHRM fetches leave
    // types/data asynchronously) and retries the click if it still gets intercepted.
    private void waitAndClick(By locator) {
        for (int attempt = 1; ; attempt++) {
            try {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));
                wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
                return;
            } catch (ElementClickInterceptedException e) {
                if (attempt >= 3) throw e;
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }
    }

    public void selectLeaveType(String leaveTypeName) {
        waitAndClick(leaveTypeDropdown);
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptions));
        List<WebElement> options = driver.findElements(dropdownOptions);
        if (leaveTypeName != null && !leaveTypeName.isEmpty()) {
            for (WebElement option : options) {
                if (option.getText().trim().equals(leaveTypeName)) {
                    option.click();
                    return;
                }
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

    public void enterFromDate(String date) {
        enterDate(fromDateInput, date);
    }

    public void enterToDate(String date) {
        enterDate(toDateInput, date);
    }

    private void enterDate(By locator, String date) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        field.click();
        field.sendKeys(Keys.CONTROL + "a");
        field.sendKeys(date);
        field.sendKeys(Keys.TAB);
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
    }

    public void clickApply() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", btn);
        btn.click();
    }

    // ─── Assign Leave — employee autocomplete ──────────────────────────────────

    public void enterEmployeeName(String hint) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeNameField));
        field.click();
        field.clear();
        field.sendKeys(hint);

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

    // ─── Leave List search ─────────────────────────────────────────────────────

    public void selectLeaveStatus(String status) {
        waitAndClick(leaveStatusDropdown);
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptions));
        List<WebElement> options = driver.findElements(dropdownOptions);
        for (WebElement option : options) {
            if (option.getText().trim().equals(status)) {
                option.click();
                return;
            }
        }
    }

    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    public void clickReset() {
        wait.until(ExpectedConditions.elementToBeClickable(resetButton)).click();
    }

    // ─── Assertions ────────────────────────────────────────────────────────────

    public boolean isSuccessToastDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successToast)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidationErrorDisplayed() {
        // 1. Inline required-field errors
        try {
            shortWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(requiredErrors, 0));
            return true;
        } catch (Exception ignored) {}
        // 2. Error toast
        try {
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(errorToast)).isDisplayed();
        } catch (Exception ignored) {}
        // 3. Inline span warning (e.g. "To date should be after From date")
        try {
            By inlineWarn = By.xpath(
                    "//span[contains(@class,'oxd-input-field-error-message') or contains(@class,'error')]");
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(inlineWarn)).isDisplayed();
        } catch (Exception ignored) {}
        return false;
    }

    public int getRequiredErrorCount() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(requiredErrors, 0));
        return driver.findElements(requiredErrors).size();
    }

    public boolean isLeaveListTableDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(tableBody)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getTableRowCount() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(tableBody));
            return driver.findElements(tableRows).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isNoRecordsMessageDisplayed() {
        try {
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(noRecordsMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMyLeavePageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(myLeaveHeader)).isDisplayed();
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("viewMyLeaveList");
        }
    }

    // ─── Date helpers ──────────────────────────────────────────────────────────

    public static String getFutureDate(int daysFromNow) {
        return LocalDate.now().plusDays(daysFromNow)
                .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    public static String getPastDate(int daysAgo) {
        return LocalDate.now().minusDays(daysAgo)
                .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }
}
