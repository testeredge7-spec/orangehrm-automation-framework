package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.apache.logging.log4j.Logger;

import utilities.LoggerUtil;

public class LoginPage {

	private WebDriver driver;
	private WebDriverWait wait;

	private static final Logger log = LoggerUtil.getLogger(LoginPage.class);

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

		log.info("Verifying Login Page display");

		return wait.until(ExpectedConditions.visibilityOfElementLocated(username)).isDisplayed();
	}

	public void enterUsername(String userName) {

		log.info("Entering Username");

		wait.until(ExpectedConditions.visibilityOfElementLocated(username));

		driver.findElement(username).clear();

		driver.findElement(username).sendKeys(userName);
	}

	public void enterPassword(String passWord) {

		log.info("Entering Password");

		wait.until(ExpectedConditions.visibilityOfElementLocated(password));

		driver.findElement(password).clear();

		driver.findElement(password).sendKeys(passWord);
	}

	public void clickLogin() {

		log.info("Clicking Login Button");

		wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
	}

	public void login(String userName, String passWord) {

		log.info("Login attempt started");

		enterUsername(userName);

		enterPassword(passWord);

		clickLogin();

		log.info("Login request submitted");
	}

	public String getInvalidCredentialsMessage() {

		String message = wait.until(ExpectedConditions.visibilityOfElementLocated(invalidCredentialsMessage)).getText();

		log.warn("Invalid Credentials Message: {}", message);

		return message;
	}

	public String getRequiredMessage() {

		String message = wait.until(ExpectedConditions.visibilityOfElementLocated(requiredMessage)).getText();

		log.warn("Required Field Message: {}", message);

		return message;
	}

	public void clickForgotPassword() {

		log.info("Navigating to Forgot Password page");

		wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink)).click();
	}
}