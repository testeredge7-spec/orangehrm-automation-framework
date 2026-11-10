package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.apache.logging.log4j.Logger;

import utilities.LoggerUtil;

public class DashboardPage {

	private WebDriver driver;
	private WebDriverWait wait;

	private static final Logger log = LoggerUtil.getLogger(DashboardPage.class);

	public DashboardPage(WebDriver driver) {

		this.driver = driver;

		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	private By dashboardHeader = By.xpath("//h6[text()='Dashboard']");

	private By menuItems = By.xpath("//ul[@class='oxd-main-menu']//li");

	private By adminMenu = By.xpath("//span[text()='Admin']");

	private By pimMenu = By.xpath("//span[text()='PIM']");

	private By leaveMenu = By.xpath("//span[text()='Leave']");

	private By timeMenu = By.xpath("//span[text()='Time']");

	private By recruitmentMenu = By.xpath("//span[text()='Recruitment']");

	private By directoryMenu = By.xpath("//span[text()='Directory']");

	public boolean isDashboardDisplayed() {

		log.info("Verifying Dashboard visibility");

		return wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeader)).isDisplayed();
	}

	public String getDashboardHeaderText() {

		String header = wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeader)).getText();

		log.info("Dashboard Header: {}", header);

		return header;
	}

	public int getMenuCount() {

		int count = driver.findElements(menuItems).size();

		log.info("Total menu count: {}", count);

		return count;
	}

	public void printAllMenus() {

		List<WebElement> menus = driver.findElements(menuItems);

		log.info("Printing all dashboard menus");

		for (WebElement menu : menus) {

			log.info(menu.getText());
		}
	}

	public boolean isAdminMenuDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(adminMenu)).isDisplayed();
	}

	public boolean isPimMenuDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(pimMenu)).isDisplayed();
	}

	public boolean isLeaveMenuDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(leaveMenu)).isDisplayed();
	}

	public boolean isTimeMenuDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(timeMenu)).isDisplayed();
	}

	public boolean isRecruitmentMenuDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(recruitmentMenu)).isDisplayed();
	}

	public boolean isDirectoryMenuDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(directoryMenu)).isDisplayed();
	}

	public void clickAdmin() {

		log.info("Clicking Admin menu");

		wait.until(ExpectedConditions.elementToBeClickable(adminMenu)).click();
	}

	public void clickLeave() {

		log.info("Clicking Leave menu");

		wait.until(ExpectedConditions.elementToBeClickable(leaveMenu)).click();
	}
}