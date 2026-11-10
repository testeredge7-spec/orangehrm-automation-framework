package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

	private WebDriver driver;
	private WebDriverWait wait;

	public LoginPage(WebDriver driver) {

		this.driver = driver;

		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	private By username = By.name("username");

	private By password = By.name("password");

	private By loginButton = By.xpath("//button[@type='submit']");

	private By forgotPasswordLink = By.xpath("//p[text()='Forgot your password? ']");

	private By invalidCredentialsMessage = By.xpath("//p[contains(@class,'oxd-alert-content-text')]");

	private By requiredMessage = By.xpath("//span[text()='Required']");

	public boolean isLoginPageDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(username)).isDisplayed();
	}

	public void enterUsername(String userName) {

		wait.until(ExpectedConditions.visibilityOfElementLocated(username));

		driver.findElement(username).clear();

		driver.findElement(username).sendKeys(userName);
	}

	public void enterPassword(String passWord) {

		wait.until(ExpectedConditions.visibilityOfElementLocated(password));

		driver.findElement(password).clear();

		driver.findElement(password).sendKeys(passWord);
	}

	public void clickLogin() {

		wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
	}

	public void login(String userName, String passWord) {

		enterUsername(userName);

		enterPassword(passWord);

		clickLogin();
	}

	public String getInvalidCredentialsMessage() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(invalidCredentialsMessage)).getText();
	}

	public String getRequiredMessage() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(requiredMessage)).getText();
	}

	public void clickForgotPassword() {

		wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink)).click();
	}
}