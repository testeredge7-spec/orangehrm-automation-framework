package pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LeavePage {

	private WebDriver driver;
	private WebDriverWait wait;

	public LeavePage(WebDriver driver) {

		this.driver = driver;

		this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	}

	private By searchButton = By.xpath("//button[normalize-space()='Search']");

	private By resetButton = By.xpath("//button[normalize-space()='Reset']");

	private By applyButton = By.xpath("//button[normalize-space()='Apply']");

	private By loadingSpinner = By.cssSelector(".oxd-loading-spinner");

	private By recordsText = By.xpath("//*[contains(text(),'Records Found')]");

	private By tableRows = By.xpath("//div[@class='oxd-table-body']//div[@role='row']");

	private By dropdownOptions = By.xpath("//div[@role='listbox']//div[@role='option']");

	private By leaveTypeDropdown = By.xpath(
			"//label[text()='Leave Type']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text-input')]");

	private By leaveBalance = By.xpath(
			"//label[normalize-space()='Leave Balance']/ancestor::div[contains(concat(' ',normalize-space(@class),' '),' oxd-input-group ') or contains(concat(' ',normalize-space(@class),' '),' oxd-grid-item ')][1]");

	private By fromDateField = By.xpath("//label[text()='From Date']/parent::div/following-sibling::div//input");

	private By toDateField = By.xpath("//label[text()='To Date']/parent::div/following-sibling::div//input");

	private By commentsField = By.xpath("//label[text()='Comments']/parent::div/following-sibling::div//textarea");

	private By requiredErrors = By.xpath("//span[normalize-space()='Required']");

	private By successToast = By.xpath("//div[contains(@class,'oxd-toast--success')]");

	private By errorToast = By.xpath("//div[contains(@class,'oxd-toast--error') or contains(@class,'oxd-toast--warn') or contains(@class,'oxd-toast--info')]");

	private By tab(String name) {

		return By.xpath("//nav[@aria-label='Topbar Menu']//*[self::a or self::span][normalize-space()='" + name + "']");
	}

	private By header(String name) {

		return By.xpath("//*[self::h5 or self::h6][normalize-space()='" + name + "']");
	}

	private By field(String name) {

		return By.xpath("//*[(self::label or self::p or self::span) and normalize-space()='" + name + "']");
	}

	private By leaveTypeOption(String text) {

		return By.xpath("//div[@role='listbox']//span[normalize-space()='" + text + "']");
	}

	public void waitForSpinnerToDisappear() {

		try {

			new WebDriverWait(driver, Duration.ofSeconds(2))
					.until(ExpectedConditions.visibilityOfElementLocated(loadingSpinner));

		} catch (TimeoutException e) {

		}

		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
	}

	public void waitForUrl(String part) {

		wait.until(ExpectedConditions.urlContains(part));
	}

	public boolean isHeaderDisplayed(String name) {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(header(name))).isDisplayed();
	}

	public boolean isTabDisplayed(String name) {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(tab(name))).isDisplayed();
	}

	public void clickTab(String name) {

		wait.until(ExpectedConditions.elementToBeClickable(tab(name))).click();

		waitForSpinnerToDisappear();
	}

	public boolean isFieldDisplayed(String name) {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(field(name))).isDisplayed();
	}

	public boolean isApplyButtonDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(applyButton)).isDisplayed();
	}

	public boolean isSearchButtonDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(searchButton)).isDisplayed();
	}

	public boolean isResetButtonDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(resetButton)).isDisplayed();
	}

	public void clickSearch() {

		wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();

		waitForSpinnerToDisappear();

		try {

			wait.until(ExpectedConditions.or(

					ExpectedConditions.presenceOfAllElementsLocatedBy(tableRows),

					ExpectedConditions.visibilityOfElementLocated(recordsText)

			));

		} catch (TimeoutException e) {

			System.out.println("Search completed but no rows were found.");
		}
	}

	public void clickApply() {

		wait.until(ExpectedConditions.elementToBeClickable(applyButton)).click();
	}

	public int getRequiredErrorCount() {

		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(requiredErrors)).size();
	}

	public List<String> getLeaveTypeOptions() {

		wait.until(ExpectedConditions.elementToBeClickable(leaveTypeDropdown)).click();

		List<WebElement> options = wait.until(d -> {

			List<WebElement> found = d.findElements(dropdownOptions);

			return found.size() > 1 ? found : null;
		});

		List<String> types = new ArrayList<>();

		for (WebElement option : options) {

			String text = option.getAttribute("textContent").trim();

			if (!text.isEmpty() && !text.startsWith("--")) {

				types.add(text);
			}
		}

		options.get(1).click();

		return types;
	}

	public void selectLeaveType(String leaveType) {

		WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(leaveTypeDropdown));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", dropdown);

		wait.until(ExpectedConditions.elementToBeClickable(dropdown)).click();

		wait.until(ExpectedConditions.elementToBeClickable(leaveTypeOption(leaveType))).click();
	}

	public String getSelectedLeaveType() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(leaveTypeDropdown)).getText().trim();
	}

	public String getLeaveBalanceText() {

		WebElement balance = wait.until(ExpectedConditions.presenceOfElementLocated(leaveBalance));

		return balance.getAttribute("textContent").replace("Leave Balance", "").replaceAll("\\s+", " ").trim();
	}

	public double getLeaveBalance() {

		Matcher matcher = Pattern.compile("-?\\d+(\\.\\d+)?").matcher(getLeaveBalanceText());

		return matcher.find() ? Double.parseDouble(matcher.group()) : 0;
	}

	public String selectLeaveTypeWithBalance(double minBalance) {

		List<String> types = getLeaveTypeOptions();

		for (String type : types) {

			String previous = getLeaveBalanceText();

			selectLeaveType(type);

			try {

				new WebDriverWait(driver, Duration.ofSeconds(3)).until(d -> !getLeaveBalanceText().equals(previous));

			} catch (TimeoutException e) {

				// balance may be the same as the previous type
			}

			if (getLeaveBalance() >= minBalance) {

				return type;
			}
		}

		return null;
	}

	public String getDatePattern() {

		String placeholder = wait.until(ExpectedConditions.visibilityOfElementLocated(fromDateField))
				.getAttribute("placeholder");

		if (placeholder == null || placeholder.trim().isEmpty()) {

			return "yyyy-dd-MM";
		}

		return placeholder.trim().toLowerCase().replace("mm", "MM");
	}

	private void typeDate(By locator, String date) {

		WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

		input.click();

		input.sendKeys(Keys.chord(Keys.CONTROL, "a"), date);

		input.sendKeys(Keys.TAB);
	}

	public void enterFromDate(String date) {

		typeDate(fromDateField, date);
	}

	public void enterToDate(String date) {

		typeDate(toDateField, date);
	}

	public void enterComments(String comments) {

		WebElement area = wait.until(ExpectedConditions.visibilityOfElementLocated(commentsField));

		area.clear();

		area.sendKeys(comments);
	}

	public boolean clickApplyAndCheckSuccess() {

		clickApply();

		try {

			return new WebDriverWait(driver, Duration.ofSeconds(15)).until(d -> {

				if (!d.findElements(successToast).isEmpty()) {

					return Boolean.TRUE;
				}

				if (!d.findElements(errorToast).isEmpty()) {

					return Boolean.FALSE;
				}

				return null;
			});

		} catch (TimeoutException e) {

			return false;
		}
	}

	public List<String> getRowTexts() {

		for (int attempt = 0; attempt < 3; attempt++) {

			try {

				List<String> rows = new ArrayList<>();

				for (WebElement row : driver.findElements(tableRows)) {

					rows.add(row.getText().replace("\n", " ").trim());
				}

				return rows;

			} catch (StaleElementReferenceException e) {

				// table re-rendered, read again
			}
		}

		return new ArrayList<>();
	}

	public String getRowTextByComment(String comment) {

		try {

			return new WebDriverWait(driver, Duration.ofSeconds(15)).until(d -> {

				for (String row : getRowTexts()) {

					if (row.contains(comment)) {

						return row;
					}
				}

				return null;
			});

		} catch (TimeoutException e) {

			return null;
		}
	}

	public boolean isRowPresent(String comment) {

		for (String row : getRowTexts()) {

			if (row.contains(comment)) {

				return true;
			}
		}

		return false;
	}

	public boolean cancelLeaveByComment(String comment) {

		By cancelButton = By.xpath("//div[@class='oxd-table-body']//div[@role='row'][contains(.,'" + comment
				+ "')]//button[normalize-space()='Cancel']");

		wait.until(ExpectedConditions.elementToBeClickable(cancelButton)).click();

		try {

			new WebDriverWait(driver, Duration.ofSeconds(15))
					.until(ExpectedConditions.visibilityOfElementLocated(successToast));

			return true;

		} catch (TimeoutException e) {

			return false;
		}
	}
}