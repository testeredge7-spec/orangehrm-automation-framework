package pages;

import java.time.Duration;
import java.util.ArrayList;

import org.openqa.selenium.StaleElementReferenceException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdminPage {

	private WebDriver driver;
	private WebDriverWait wait;
	private WebDriverWait shortWait;
	private By nextPageButton = By
			.xpath("//nav[contains(@class,'oxd-pagination-nav')]//button[.//i[contains(@class,'bi-chevron-right')]]");

	public AdminPage(WebDriver driver) {

		this.driver = driver;

		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
	}

	private By adminHeader = By.xpath("//h6[text()='Admin']");

	private By usernameField = By.xpath("//label[text()='Username']/parent::div/following-sibling::div//input");

	private By searchButton = By.xpath("//button[normalize-space()='Search']");

	private By resetButton = By.xpath("//button[normalize-space()='Reset']");

	private By addButton = By.xpath("//button[normalize-space()='Add']");

	private By noRecordsFound = By.xpath("//span[normalize-space()='No Records Found']");

	private By loadingSpinner = By.cssSelector(".oxd-loading-spinner");

	private By recordsFoundText = By.xpath("//span[contains(text(),'Records Found')]");

	private By tableRows = By.xpath("//div[@class='oxd-table-body']//div[@role='row']");

	private By deleteConfirmButton = By.xpath("//button[contains(normalize-space(),'Yes, Delete')]");

	private By deleteCancelButton = By.xpath("//button[contains(normalize-space(),'No, Cancel')]");

	private By successToast = By.xpath("//div[contains(@class,'oxd-toast--success')]");

	private By userRow(String username) {

		return By.xpath(
				"//div[@class='oxd-table-body']//div[@role='row'][.//div[normalize-space()='" + username + "']]");
	}

	public void waitForSpinnerToDisappear() {

		try {

			new WebDriverWait(driver, Duration.ofSeconds(2))
					.until(ExpectedConditions.visibilityOfElementLocated(loadingSpinner));

		} catch (TimeoutException e) {

			// spinner already gone or never shown
		}

		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
	}

	public boolean isAdminPageDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(adminHeader)).isDisplayed();
	}

	public String getAdminHeaderText() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(adminHeader)).getText();
	}

	public boolean isUsernameFieldDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).isDisplayed();
	}

	public boolean isSearchButtonDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(searchButton)).isDisplayed();
	}

	public boolean isResetButtonDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(resetButton)).isDisplayed();
	}

	public boolean isAddButtonDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(addButton)).isDisplayed();
	}

	public void enterUsername(String username) {

		wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));

		driver.findElement(usernameField).clear();

		driver.findElement(usernameField).sendKeys(username);
	}

	public void clickSearch() {

		wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();

		waitForSpinnerToDisappear();
	}

	public void clickReset() {

		wait.until(ExpectedConditions.elementToBeClickable(resetButton)).click();

		waitForSpinnerToDisappear();
	}

	public void clickAddButton() {

		wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
	}

	public String getUsernameFieldValue() {

		return driver.findElement(usernameField).getAttribute("value");
	}

	public boolean isNoRecordsFoundDisplayed() {

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(noRecordsFound));

			return true;

		} catch (TimeoutException e) {

			return false;
		}
	}

	public boolean isUserDisplayed(String username) {

		try {

			shortWait.until(ExpectedConditions.presenceOfElementLocated(userRow(username)));

			return true;

		} catch (TimeoutException e) {

			return false;
		}
	}

	public int getRecordsCount() {

		waitForSpinnerToDisappear();

		List<WebElement> found = driver.findElements(recordsFoundText);

		if (found.isEmpty()) {

			return 0;
		}

		String text = found.get(0).getText().replaceAll("[^0-9]", "");

		return text.isEmpty() ? 0 : Integer.parseInt(text);
	}

	public int getRowCount() {

		return driver.findElements(tableRows).size();
	}

	public String getUserStatus(String username) {

		By statusCell = By.xpath("//div[@class='oxd-table-body']//div[@role='row'][.//div[normalize-space()='"
				+ username + "']]//div[@role='cell'][5]/div");

		return wait.until(ExpectedConditions.visibilityOfElementLocated(statusCell)).getText();
	}

	public void clickEditUser(String username) {

		By editIcon = By.xpath("//div[@class='oxd-table-body']//div[@role='row'][.//div[normalize-space()='" + username
				+ "']]//button[.//i[contains(@class,'bi-pencil-fill')]]");

		wait.until(ExpectedConditions.elementToBeClickable(editIcon)).click();
	}

	public void clickDeleteUser(String username) {

		By deleteIcon = By.xpath("//div[@class='oxd-table-body']//div[@role='row'][.//div[normalize-space()='"
				+ username + "']]//button[.//i[contains(@class,'bi-trash')]]");

		wait.until(ExpectedConditions.elementToBeClickable(deleteIcon)).click();
	}

	public boolean isDeleteConfirmationDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(deleteConfirmButton)).isDisplayed();
	}

	public void confirmDelete() {

		wait.until(ExpectedConditions.elementToBeClickable(deleteConfirmButton)).click();

		waitForSpinnerToDisappear();
	}

	public void cancelDelete() {

		wait.until(ExpectedConditions.elementToBeClickable(deleteCancelButton)).click();
	}

	public boolean isSuccessToastDisplayed() {

		try {

			shortWait.until(ExpectedConditions.visibilityOfElementLocated(successToast));

			return true;

		} catch (TimeoutException e) {

			return false;
		}
	}

	private List<String[]> readCurrentPageRecords() {

		for (int attempt = 0; attempt < 3; attempt++) {

			try {

				List<String[]> pageRecords = new ArrayList<>();

				for (WebElement row : driver.findElements(tableRows)) {

					List<WebElement> cells = row.findElements(By.xpath(".//div[@role='cell']"));

					int size = cells.size();

					if (size >= 5) {

						pageRecords.add(new String[] { cells.get(size - 5).getText().replace("\n", " ").trim(),
								cells.get(size - 4).getText().replace("\n", " ").trim(),
								cells.get(size - 3).getText().replace("\n", " ").trim(),
								cells.get(size - 2).getText().replace("\n", " ").trim() });
					}
				}

				return pageRecords;

			} catch (StaleElementReferenceException e) {

				// table re-rendered, read again
			}
		}

		return new ArrayList<>();
	}

	public List<String[]> getAllUserRecords(int totalRecords) {

		List<String[]> records = new ArrayList<>();

		wait.until(ExpectedConditions.presenceOfElementLocated(tableRows));

		for (int page = 0; page < 100; page++) {

			waitForSpinnerToDisappear();

			records.addAll(readCurrentPageRecords());

			if (records.size() >= totalRecords) {

				break;
			}

			List<WebElement> next = driver.findElements(nextPageButton);

			if (next.isEmpty()) {

				break;
			}

			WebElement firstRow = driver.findElements(tableRows).get(0);

			next.get(0).click();

			try {

				wait.until(ExpectedConditions.stalenessOf(firstRow));

			} catch (TimeoutException e) {

				// page already refreshed
			}
		}

		return records;
	}
}