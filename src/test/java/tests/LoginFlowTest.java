package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.DashboardPage;
import pages.LoginPage;

public class LoginFlowTest extends BaseTest {

	@Test(priority = 1)
	public void verifyValidLogin() {

		driver.get(prop.getProperty("url"));

		LoginPage login = new LoginPage(driver);

		login.login(prop.getProperty("username"), prop.getProperty("password"));

		DashboardPage dashboard = new DashboardPage(driver);

		Assert.assertTrue(dashboard.isDashboardDisplayed(), "Dashboard not displayed after login");
	}
}
