package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ForgotPasswordPage {

	private WebDriver driver;
	private WebDriverWait wait;

	public ForgotPasswordPage(WebDriver driver) {

		this.driver = driver;

		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	private By resetPasswordHeader = By.xpath("//h6[text()='Reset Password']");

	private By usernameField = By.name("username");

	private By resetButton = By.xpath("//button[@type='submit']");

	public boolean isResetPasswordPageDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(resetPasswordHeader)).isDisplayed();
	}

	public boolean isUsernameFieldDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).isDisplayed();
	}

	public boolean isResetButtonDisplayed() {

		return wait.until(ExpectedConditions.visibilityOfElementLocated(resetButton)).isDisplayed();
	}
}