package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.AdminPage;

public class AdminTest extends BaseTest {

	@Test(priority = 1)
	public void verifyAdminPageDisplayed() {

		AdminPage admin = new AdminPage(driver);

		Assert.assertTrue(admin.isAdminPageDisplayed(), "Admin page is not displayed");
	}

	@Test(priority = 2)
	public void verifyAdminHeader() {

		AdminPage admin = new AdminPage(driver);

		Assert.assertEquals(admin.getAdminHeaderText(), "Admin", "Admin header mismatch");
	}

	@Test(priority = 3)
	public void verifyUsernameFieldDisplayed() {

		AdminPage admin = new AdminPage(driver);

		Assert.assertTrue(admin.isUsernameFieldDisplayed(), "Username field not displayed");
	}

	@Test(priority = 4)
	public void verifySearchButtonDisplayed() {

		AdminPage admin = new AdminPage(driver);

		Assert.assertTrue(admin.isSearchButtonDisplayed(), "Search button not displayed");
	}

	@Test(priority = 5)
	public void verifyResetButtonDisplayed() {

		AdminPage admin = new AdminPage(driver);

		Assert.assertTrue(admin.isResetButtonDisplayed(), "Reset button not displayed");
	}

	@Test(priority = 6)
	public void verifyAddButtonDisplayed() {

		AdminPage admin = new AdminPage(driver);

		Assert.assertTrue(admin.isAddButtonDisplayed(), "Add button not displayed");
	}

	@Test(priority = 7)
	public void verifySearchExistingUser() {

		AdminPage admin = new AdminPage(driver);

		admin.enterUsername("Admin");

		admin.clickSearch();

		Assert.assertTrue(admin.isUserDisplayed("Admin"), "Admin user not found in table");
	}

	@Test(priority = 8)
	public void verifySearchInvalidUser() {

		AdminPage admin = new AdminPage(driver);

		admin.clickReset();

		admin.enterUsername("InvalidUser123456");

		admin.clickSearch();

		Assert.assertTrue(admin.isNoRecordsFoundDisplayed(), "No Records Found message not displayed");
	}

	@Test(priority = 9)
	public void verifyResetFunctionality() {

		AdminPage admin = new AdminPage(driver);

		admin.enterUsername("Admin");

		admin.clickReset();

		Assert.assertEquals(admin.getUsernameFieldValue(), "", "Username field was not cleared");
	}

	@Test(priority = 10)
	public void verifyAddUserNavigation() {

		AdminPage admin = new AdminPage(driver);

		admin.clickAddButton();

		Assert.assertTrue(driver.getCurrentUrl().contains("saveSystemUser"), "Add User page not opened");
	}
}