package com.orangehrm.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyInfoPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private WebDriverWait shortWait;

    // Sidebar
    private By myInfoMenuLink = By.xpath("//span[text()='My Info']/ancestor::a");

    // Page headers
    private By personalDetailsHeader = By.xpath("//h6[text()='Personal Details']");

    // Personal Details form fields
    private By firstNameField = By.xpath("//input[@name='firstName']");
    private By middleNameField = By.xpath("//input[@name='middleName']");
    private By lastNameField = By.xpath("//input[@name='lastName']");
    private By otherIdField = By.xpath(
            "//label[text()='Other Id']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By driverLicenseField = By.xpath(
            "//label[text()=\"Driver's License Number\"]/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By nationalityDropdown = By.xpath(
            "//label[text()='Nationality']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");
    private By maritalStatusDropdown = By.xpath(
            "//label[text()='Marital Status']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");
    private By dateOfBirthField = By.xpath(
            "//label[text()='Date of Birth']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By maleGenderLabel = By.xpath("//label[text()='Male']");
    private By dropdownOptions = By.xpath("//div[@role='listbox']//div[@role='option']");
    private By saveButton = By.xpath("//button[@type='submit']");
    private By successToast = By.cssSelector(".oxd-toast");
    private By formLoader = By.cssSelector(".oxd-form-loader");

    // Custom Fields section (admin-configurable, so fields are discovered at runtime)
    private By customFieldsHeader = By.xpath("//h6[text()='Custom Fields']");
    private By customFieldInputs = By.xpath("//h6[text()='Custom Fields']/following::input[@type='text']");

    // Attachments section (below Custom Fields on the Personal Details tab)
    private By attachmentsHeader = By.xpath("//h6[text()='Attachments']");
    private By addAttachmentButton = By.xpath("//button[normalize-space()='Add']");
    private By attachmentFileInput = By.xpath("//input[@type='file']");
    private By attachmentCommentField = By.xpath("//textarea");
    // The Attachments modal's own Save button is a second, later "Save" button
    // on the page (the Personal Details form's Save button is the first) —
    // targeting the last one avoids clicking the underlying form's button.
    private By attachmentSaveButton = By.xpath("(//button[@type='submit'])[last()]");

    public MyInfoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(7));
    }

    // --- Navigation ---

    public void navigateToMyInfo() {
        wait.until(ExpectedConditions.elementToBeClickable(myInfoMenuLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader));
    }

    public boolean isPersonalDetailsDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader)).isDisplayed();
    }

    // --- Tab navigation (Personal Details, Contact Details, Emergency Contacts,
    // Dependents, Immigration, Job, Salary, Report-to, Qualifications, Memberships) ---

    // Each section renders its own page title (e.g. Emergency Contacts' header
    // text is actually "Assigned Emergency Contacts"), so matching tab name to
    // header text isn't reliable. Instead, the clicked tab link itself gets
    // class="orangehrm-tabs-item --active" once its section has loaded — that's
    // used as the uniform navigation-succeeded signal for every tab.
    public void navigateToTab(String tabName) {
        By tabLink = By.xpath("//a[normalize-space()='" + tabName + "']");
        wait.until(ExpectedConditions.elementToBeClickable(tabLink)).click();
        By activeTabLink = By.xpath(
                "//a[normalize-space()='" + tabName + "' and contains(@class,'--active')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(activeTabLink));
    }

    // Waits out the .oxd-form-loader overlay (shown while OrangeHRM fetches
    // dropdown/section data asynchronously) and retries the click if it still
    // gets intercepted by leftover transient elements — same pattern LeavePage
    // already relies on for its Leave Type / Leave Status dropdowns.
    private void waitAndClick(By locator) {
        for (int attempt = 1; ; attempt++) {
            try {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));
                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
                ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
                el.click();
                return;
            } catch (ElementClickInterceptedException e) {
                if (attempt >= 3) throw e;
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }
    }

    public boolean isTabActive(String tabName) {
        By activeTabLink = By.xpath(
                "//a[normalize-space()='" + tabName + "' and contains(@class,'--active')]");
        try {
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(activeTabLink)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // --- Personal Details field updates ---

    // WebElement.clear() doesn't reliably reset these Vue-controlled inputs —
    // the framework's own model keeps the old value, so a plain clear()+sendKeys()
    // ends up appending instead of replacing. Selecting all text and typing over
    // it (same trick editDateOfBirth already used) actually replaces the value.
    // The .oxd-form-loader overlay (shown while dropdown data like Nationality/
    // Marital Status loads async) can still be covering the form when the very
    // first field is touched, so it's waited out here too, not just in waitAndClick.
    private void clearAndType(WebElement field, String value) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));
        field.click();
        field.sendKeys(Keys.CONTROL + "a");
        field.sendKeys(Keys.DELETE);
        field.sendKeys(value);
    }

    public void editFirstName(String firstName) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        clearAndType(field, firstName);
    }

    public void editMiddleName(String middleName) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(middleNameField));
        clearAndType(field, middleName);
    }

    public void editLastName(String lastName) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField));
        clearAndType(field, lastName);
    }

    public void editOtherId(String otherId) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(otherIdField));
        clearAndType(field, otherId);
    }

    public void editDriverLicenseNumber(String licenseNumber) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(driverLicenseField));
        clearAndType(field, licenseNumber);
    }

    public void editDateOfBirth(String date) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(dateOfBirthField));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", field);
        clearAndType(field, date);
        field.sendKeys(Keys.TAB);
    }

    public void selectNationality(String nationality) {
        selectDropdownOption(nationalityDropdown, nationality);
    }

    public void selectMaritalStatus(String maritalStatus) {
        selectDropdownOption(maritalStatusDropdown, maritalStatus);
    }

    // Opens the given dropdown, then picks the option matching optionText;
    // falls back to the first non-placeholder option when optionText is null/empty.
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
        for (WebElement option : options) {
            String text = option.getText().trim();
            if (!text.isEmpty() && !text.equals("-- Select --")) {
                option.click();
                return;
            }
        }
    }

    public void selectGenderMale() {
        wait.until(ExpectedConditions.elementToBeClickable(maleGenderLabel)).click();
    }

    public void clickSave() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        btn.click();
    }

    public boolean isSuccessToastDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successToast)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // --- Custom Fields ---

    public boolean isCustomFieldsSectionDisplayed() {
        try {
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(customFieldsHeader)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Fills every text input found under the Custom Fields header with
    // "<valuePrefix><index>" and returns how many fields were updated.
    // The fields render asynchronously slightly after the header itself, so a
    // short wait for at least one to appear avoids a race with an empty result;
    // if none ever appear (no custom fields configured), this just times out
    // quietly and the subsequent findElements() returns an empty list.
    public int updateAllCustomFields(String valuePrefix) {
        try {
            shortWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(customFieldInputs, 0));
        } catch (Exception ignored) {}

        List<WebElement> fields = driver.findElements(customFieldInputs);
        int count = 0;
        for (WebElement field : fields) {
            clearAndType(field, valuePrefix + count);
            count++;
        }
        return count;
    }

    // --- Attachments ---

    public boolean isAttachmentsSectionDisplayed() {
        try {
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(attachmentsHeader)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAddAttachment() {
        wait.until(ExpectedConditions.elementToBeClickable(addAttachmentButton)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(attachmentFileInput));
    }

    // File inputs accept sendKeys even while visually hidden behind a styled
    // "Browse" button, so presenceOfElementLocated is used instead of visibility.
    public void uploadAttachmentFile(String absoluteFilePath) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(attachmentFileInput));
        input.sendKeys(absoluteFilePath);
    }

    public void enterAttachmentComment(String comment) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(attachmentCommentField));
        field.clear();
        field.sendKeys(comment);
    }

    public void clickAttachmentSave() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(attachmentSaveButton));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        btn.click();
    }
}
