package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.DashboardPage;

public class DashboardTest extends BaseTest {

	@Test(priority = 1)
	public void verifyDashboardLoaded() {

		DashboardPage dashboard = new DashboardPage(driver);

		Assert.assertTrue(dashboard.isDashboardDisplayed(), "Dashboard is not displayed");
	}

	@Test(priority = 2)
	public void verifyDashboardHeader() {

		DashboardPage dashboard = new DashboardPage(driver);

		Assert.assertEquals(dashboard.getDashboardHeaderText(), "Dashboard", "Dashboard header mismatch");
	}

	@Test(priority = 3)
	public void verifyDashboardMenuCount() {

		DashboardPage dashboard = new DashboardPage(driver);

		dashboard.printAllMenus();

		Assert.assertEquals(dashboard.getMenuCount(), 12, "Menu count mismatch");
	}

	@Test(priority = 4)
	public void verifyAdminMenuDisplayed() {

		DashboardPage dashboard = new DashboardPage(driver);

		Assert.assertTrue(dashboard.isAdminMenuDisplayed(), "Admin Menu not displayed");
	}

	@Test(priority = 5)
	public void verifyPimMenuDisplayed() {

		DashboardPage dashboard = new DashboardPage(driver);

		Assert.assertTrue(dashboard.isPimMenuDisplayed(), "PIM Menu not displayed");
	}

	@Test(priority = 6)
	public void verifyLeaveMenuDisplayed() {

		DashboardPage dashboard = new DashboardPage(driver);

		Assert.assertTrue(dashboard.isLeaveMenuDisplayed(), "Leave Menu not displayed");
	}

	@Test(priority = 7)
	public void verifyTimeMenuDisplayed() {

		DashboardPage dashboard = new DashboardPage(driver);

		Assert.assertTrue(dashboard.isTimeMenuDisplayed(), "Time Menu not displayed");
	}

	@Test(priority = 8)
	public void verifyRecruitmentMenuDisplayed() {

		DashboardPage dashboard = new DashboardPage(driver);

		Assert.assertTrue(dashboard.isRecruitmentMenuDisplayed(), "Recruitment Menu not displayed");
	}

	@Test(priority = 9)
	public void verifyDirectoryMenuDisplayed() {

		DashboardPage dashboard = new DashboardPage(driver);

		Assert.assertTrue(dashboard.isDirectoryMenuDisplayed(), "Directory Menu not displayed");
	}

	@Test(priority = 10)
	public void verifyAdminMenuNavigation() {

		DashboardPage dashboard = new DashboardPage(driver);

		dashboard.clickAdmin();

		Assert.assertTrue(driver.getCurrentUrl().contains("admin"), "Admin page not opened");
	}
}