package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.ForgotPasswordPage;
import pages.LoginPage;

public class LoginValidationTest extends BaseTest {

	@Test(priority = 1)
	public void verifyLoginPageLoads() {

		LoginPage login = new LoginPage(driver);

		Assert.assertTrue(login.isLoginPageDisplayed(), "Login page is not displayed");
	}

	@Test(priority = 2)
	public void verifyInvalidUsername() {

		driver.get(prop.getProperty("url"));

		LoginPage login = new LoginPage(driver);

		login.login("WrongUser", "admin123");

		Assert.assertEquals(login.getInvalidCredentialsMessage(), "Invalid credentials");
	}

	@Test(priority = 3)
	public void verifyInvalidPassword() {

		driver.get(prop.getProperty("url"));

		LoginPage login = new LoginPage(driver);

		login.login("Admin", "WrongPass");

		Assert.assertEquals(login.getInvalidCredentialsMessage(), "Invalid credentials");
	}

	@Test(priority = 4)
	public void verifyInvalidUsernameAndPassword() {

		driver.get(prop.getProperty("url"));

		LoginPage login = new LoginPage(driver);

		login.login("WrongUser", "WrongPass");

		Assert.assertEquals(login.getInvalidCredentialsMessage(), "Invalid credentials");
	}

	@Test(priority = 5)
	public void verifyEmptyUsername() {

		driver.get(prop.getProperty("url"));

		LoginPage login = new LoginPage(driver);

		login.login("", "admin123");

		Assert.assertEquals(login.getRequiredMessage(), "Required");
	}

	@Test(priority = 6)
	public void verifyEmptyPassword() {

		driver.get(prop.getProperty("url"));

		LoginPage login = new LoginPage(driver);

		login.login("Admin", "");

		Assert.assertEquals(login.getRequiredMessage(), "Required");
	}

	@Test(priority = 7)
	public void verifyEmptyUsernameAndPassword() {

		driver.get(prop.getProperty("url"));

		LoginPage login = new LoginPage(driver);

		login.login("", "");

		Assert.assertEquals(login.getRequiredMessage(), "Required");
	}

	@Test(priority = 8)
	public void verifyForgotPasswordNavigation() {

		driver.get(prop.getProperty("url"));

		LoginPage login = new LoginPage(driver);

		login.clickForgotPassword();

		Assert.assertTrue(driver.getCurrentUrl().contains("requestPasswordResetCode"));
	}

	@Test(priority = 9)
	public void verifyResetPasswordPageDisplayed() {

		driver.get(prop.getProperty("url"));

		LoginPage login = new LoginPage(driver);

		login.clickForgotPassword();

		ForgotPasswordPage forgot = new ForgotPasswordPage(driver);

		Assert.assertTrue(forgot.isResetPasswordPageDisplayed());
	}

	@Test(priority = 10)
	public void verifyResetPasswordPageComponents() {

		driver.get(prop.getProperty("url"));

		LoginPage login = new LoginPage(driver);

		login.clickForgotPassword();

		ForgotPasswordPage forgot = new ForgotPasswordPage(driver);

		Assert.assertTrue(forgot.isUsernameFieldDisplayed());

		Assert.assertTrue(forgot.isResetButtonDisplayed());
	}
}