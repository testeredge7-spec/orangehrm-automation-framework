package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddUserPage {

	private WebDriver driver;
	private WebDriverWait wait;
	private WebDriverWait longWait;

	public AddUserPage(WebDriver driver) {

		this.driver = driver;

		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		this.longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
	}

	private By addUserHeader = By.xpath("//h6[text()='Add User']");

	private By editUserHeader = By.xpath("//h6[text()='Edit User']");

	private By userRoleDropdown = By.xpath(
			"//label[text()='User Role']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text-input')]");

	private By statusDropdown = By.xpath(
			"//label[text()='Status']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text-input')]");

	private By employeeNameField = By
			.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div//input");

	private By usernameField = By.xpath("//label[text()='Username']/parent::div/following-sibling::div//input");

	private By passwordField = By.xpath("//label[text()='Password']/parent::div/following-sibling::div//input");

	private By confirmPasswordField = By
			.xpath("//label[text()='Confirm Password']/parent::div/following-sibling::div//input");

	private By saveButton = By.xpath("//button[@type='submit']");

	private By cancelButton = By.xpath("//button[normalize-space()='Cancel']");

	private By employeeOptions = By.xpath(
			"//label[text()='Employee Name']/ancestor::div[contains(@class,'oxd-input-field-bottom-space')][1]//div[@role='option']");

	private By requiredErrors = By.xpath("//span[normalize-space()='Required']");

	private By passwordMismatchError = By.xpath("//span[normalize-space()='Passwords do not match']");

	private By usernameExistsError = By.xpath("//span[normalize-space()='Already exists']");

	private By passwordErrors = By.xpath(
			"//label[text()='Password']/ancestor::div[contains(@class,'oxd-input-field-bottom-space')][1]//span[contains(@class,'oxd-input-field-error-message')]");

	private By dropdownOption(String text) {

		return By.xpath("//div[@role='listbox']//span[normalize-space()='" + text + "']");
	}

	public boolean isAddUserHeaderDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(addUserHeader)).isDisplayed();
	}

	public boolean isEditUserHeaderDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(editUserHeader)).isDisplayed();
	}

	public boolean isUserRoleDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(userRoleDropdown)).isDisplayed();
	}

	public boolean isStatusDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(statusDropdown)).isDisplayed();
	}

	public boolean isEmployeeNameDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(employeeNameField)).isDisplayed();
	}

	public boolean isUsernameDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).isDisplayed();
	}

	public boolean isPasswordDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).isDisplayed();
	}

	public boolean isConfirmPasswordDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordField)).isDisplayed();
	}

	public boolean isSaveDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(saveButton)).isDisplayed();
	}

	public boolean isCancelDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(cancelButton)).isDisplayed();
	}

	public void selectUserRole(String role) {

		wait.until(ExpectedConditions.elementToBeClickable(userRoleDropdown)).click();

		wait.until(ExpectedConditions.elementToBeClickable(dropdownOption(role))).click();
	}

	public void selectStatus(String status) {

		wait.until(ExpectedConditions.elementToBeClickable(statusDropdown)).click();

		wait.until(ExpectedConditions.elementToBeClickable(dropdownOption(status))).click();
	}

	public void selectEmployee(String hint) {

		String letter = hint.trim().substring(0, 1);

		wait.until(ExpectedConditions.visibilityOfElementLocated(employeeNameField));

		driver.findElement(employeeNameField).clear();

		driver.findElement(employeeNameField).sendKeys(letter);

		for (int i = 0; i < 3; i++) {

			try {

				WebElement option = longWait.until(d -> {

					List<WebElement> options = d.findElements(employeeOptions);

					for (WebElement o : options) {

						String text = o.getAttribute("textContent").trim();

						if (!text.isEmpty() && !text.contains("Searching") && !text.contains("No Records")) {

							return o;
						}
					}

					return null;
				});

				try {

					option.click();

				} catch (Exception e) {

					((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
				}

				longWait.until(d -> d.findElement(employeeNameField).getAttribute("value").length() > 1);

				return;

			} catch (StaleElementReferenceException e) {

				// options re-rendered, try again
			}
		}
	}
	
	public void enterUsername(String username) {

		wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));

		driver.findElement(usernameField).clear();

		driver.findElement(usernameField).sendKeys(username);
	}

	public void enterPassword(String password) {

		wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));

		driver.findElement(passwordField).clear();

		driver.findElement(passwordField).sendKeys(password);
	}

	public void enterConfirmPassword(String confirmPassword) {

		wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordField));

		driver.findElement(confirmPasswordField).clear();

		driver.findElement(confirmPasswordField).sendKeys(confirmPassword);
	}

	public void fillForm(String role, String employeeHint, String status, String username, String password,
			String confirmPassword) {

		selectUserRole(role);

		selectEmployee(employeeHint);

		selectStatus(status);

		enterUsername(username);

		enterPassword(password);

		enterConfirmPassword(confirmPassword);
	}

	public void clickSave() {

		wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
	}

	public void clickCancel() {

		wait.until(ExpectedConditions.elementToBeClickable(cancelButton)).click();
	}

	public int getRequiredErrorCount() {

		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(requiredErrors)).size();
	}

	public boolean isPasswordMismatchDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordMismatchError)).isDisplayed();
	}

	public boolean isUsernameExistsDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameExistsError)).isDisplayed();
	}

	public String getPasswordErrorText() {

		wait.until(ExpectedConditions.visibilityOfElementLocated(passwordErrors));

		StringBuilder text = new StringBuilder();

		for (WebElement error : driver.findElements(passwordErrors)) {

			text.append(error.getText()).append(" | ");
		}

		return text.toString();
	}

	public void waitForUserListPage() {

		wait.until(ExpectedConditions.urlContains("viewSystemUsers"));
	}

	public void waitForEditPage() {

		wait.until(ExpectedConditions.urlContains("saveSystemUser"));

		wait.until(ExpectedConditions.visibilityOfElementLocated(editUserHeader));
	}
}