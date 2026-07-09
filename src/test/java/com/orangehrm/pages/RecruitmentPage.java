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

public class RecruitmentPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private WebDriverWait shortWait;

    // Sidebar
    private By recruitmentMenuLink = By.xpath("//span[text()='Recruitment']/ancestor::a");

    // Page header
    private By candidatesHeader = By.xpath("//h5[text()='Candidates']");

    // Search form — custom dropdowns
    private By jobTitleDropdown = By.xpath(
            "//label[text()='Job Title']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");
    private By vacancyDropdown = By.xpath(
            "//label[text()='Vacancy']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");
    private By hiringManagerDropdown = By.xpath(
            "//label[text()='Hiring Manager']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");
    private By statusDropdown = By.xpath(
            "//label[text()='Status']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");
    private By methodOfApplicationDropdown = By.xpath(
            "//label[text()='Method of Application']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");

    // Dropdown options (standard select)
    private By dropdownOptions = By.xpath("//div[@role='listbox']//div[@role='option']");

    // Candidate Name — autocomplete
    private By candidateNameField = By.xpath(
            "//label[text()='Candidate Name']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By autocompleteOptions = By.xpath(
            "//div[contains(@class,'oxd-autocomplete-option')]");

    // Keywords — plain text input
    private By keywordsField = By.xpath(
            "//label[text()='Keywords']/ancestor::div[contains(@class,'oxd-input-group')]//input");

    // Date of Application — the "To" field's label is a non-breaking space
    // (only "Date of Application" above "From" is a real label), so the two
    // date inputs are distinguished by their placeholder text instead.
    private By fromDateInput = By.xpath("//input[@placeholder='From']");
    private By toDateInput = By.xpath("//input[@placeholder='To']");

    private By searchButton = By.xpath("//button[@type='submit']");
    private By resetButton = By.xpath("//button[normalize-space()='Reset']");

    // Async form loading overlay (blocks clicks while dropdown data is fetched)
    private By formLoader = By.cssSelector(".oxd-form-loader");

    // Table
    private By tableBody = By.xpath("//div[@class='oxd-table-body']");
    private By tableRows = By.xpath("//div[@class='oxd-table-body']//div[@role='row']");
    private By noRecordsMessage = By.xpath("//span[text()='No Records Found']");

    // Validation
    private By requiredErrors = By.xpath(
            "//span[contains(@class,'oxd-input-field-error-message')]");

    public RecruitmentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(7));
    }

    // ─── Navigation ────────────────────────────────────────────────────────────

    public void navigateToRecruitment() {
        wait.until(ExpectedConditions.elementToBeClickable(recruitmentMenuLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(candidatesHeader));
    }

    public boolean isRecruitmentPageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(candidatesHeader)).isDisplayed();
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("recruitment");
        }
    }

    // ─── Search form — custom dropdowns ────────────────────────────────────────

    // Waits out the .oxd-form-loader overlay and retries the click if it still
    // gets intercepted (same pattern used across the other search screens).
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

    private void selectDropdownOption(By dropdownLocator, String optionText) {
        waitAndClick(dropdownLocator);
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptions));
        List<WebElement> options = driver.findElements(dropdownOptions);
        if (optionText != null && !optionText.isEmpty()) {
            for (WebElement option : options) {
                if (option.getText().trim().equals(optionText)) {
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

    public void selectJobTitle(String jobTitle) {
        selectDropdownOption(jobTitleDropdown, jobTitle);
    }

    public void selectVacancy(String vacancy) {
        selectDropdownOption(vacancyDropdown, vacancy);
    }

    public void selectHiringManager(String hiringManager) {
        selectDropdownOption(hiringManagerDropdown, hiringManager);
    }

    public void selectStatus(String status) {
        selectDropdownOption(statusDropdown, status);
    }

    public void selectMethodOfApplication(String method) {
        selectDropdownOption(methodOfApplicationDropdown, method);
    }

    // ─── Candidate Name — autocomplete ─────────────────────────────────────────

    public void enterCandidateName(String hint) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(candidateNameField));
        field.click();
        field.clear();
        field.sendKeys(hint);

        By validOption = By.xpath("//div[contains(@class,'oxd-autocomplete-option')]//span");
        try {
            shortWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(validOption, 0));
        } catch (Exception e) {
            field.clear();
            field.sendKeys("a");
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

    // ─── Keywords ───────────────────────────────────────────────────────────────

    public void enterKeywords(String text) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(keywordsField));
        field.click();
        field.sendKeys(Keys.CONTROL + "a");
        field.sendKeys(text);
    }

    // ─── Date of Application ───────────────────────────────────────────────────

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

    // ─── Search / Reset ─────────────────────────────────────────────────────────

    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    public void clickReset() {
        wait.until(ExpectedConditions.elementToBeClickable(resetButton)).click();
    }

    // ─── Results table / validation ────────────────────────────────────────────

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

    public int getRequiredErrorCount() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(requiredErrors, 0));
        return driver.findElements(requiredErrors).size();
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
