package tests;

import java.text.SimpleDateFormat;
import java.util.List;

import pages.DashboardPage;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.AddUserPage;
import pages.AdminPage;
import utilities.ExcelUtil;

public class UserManagementTest extends BaseTest {

	private static String createdUsername;

	private void openAddUserPage() {

		driver.get(prop.getProperty("url").replace("/auth/login", "/admin/saveSystemUser"));
	}

	private void openUserListPage() {

		driver.get(prop.getProperty("url").replace("/auth/login", "/admin/viewSystemUsers"));
	}

	private String data(int row, String column) {

		return ExcelUtil.getUserData(row, column);
	}

	@Test(priority = 1)
	public void verifyAddUserPageFields() {

		openAddUserPage();

		AddUserPage addUser = new AddUserPage(driver);

		Assert.assertTrue(addUser.isAddUserHeaderDisplayed(), "Add User header not displayed");

		Assert.assertTrue(addUser.isUserRoleDisplayed(), "User Role not displayed");

		Assert.assertTrue(addUser.isEmployeeNameDisplayed(), "Employee Name not displayed");

		Assert.assertTrue(addUser.isStatusDisplayed(), "Status not displayed");

		Assert.assertTrue(addUser.isUsernameDisplayed(), "Username not displayed");

		Assert.assertTrue(addUser.isPasswordDisplayed(), "Password not displayed");

		Assert.assertTrue(addUser.isConfirmPasswordDisplayed(), "Confirm Password not displayed");

		Assert.assertTrue(addUser.isSaveDisplayed(), "Save button not displayed");

		Assert.assertTrue(addUser.isCancelDisplayed(), "Cancel button not displayed");
	}

	@Test(priority = 2)
	public void verifyMandatoryFieldErrors() {

		openAddUserPage();

		AddUserPage addUser = new AddUserPage(driver);

		addUser.isAddUserHeaderDisplayed();

		addUser.clickSave();

		Assert.assertTrue(addUser.getRequiredErrorCount() >= 5, "Required errors not displayed for all fields");
	}

	@Test(priority = 3)
	public void verifyPasswordMismatchError() {

		openAddUserPage();

		AddUserPage addUser = new AddUserPage(driver);

		addUser.isAddUserHeaderDisplayed();

		addUser.enterPassword(data(2, "Password"));

		addUser.enterConfirmPassword(data(2, "ConfirmPassword"));

		Assert.assertTrue(addUser.isPasswordMismatchDisplayed(), "Password mismatch error not displayed");
	}

	@Test(priority = 4)
	public void verifyWeakPasswordError() {

		openAddUserPage();

		AddUserPage addUser = new AddUserPage(driver);

		addUser.isAddUserHeaderDisplayed();

		addUser.enterPassword(data(3, "Password"));

		addUser.enterConfirmPassword(data(3, "ConfirmPassword"));

		Assert.assertTrue(addUser.getPasswordErrorText().contains("at least 7 characters"),
				"Weak password error not displayed");
	}

	@Test(priority = 5)
	public void verifyAddValidUser() {

		openAddUserPage();

		AddUserPage addUser = new AddUserPage(driver);

		addUser.isAddUserHeaderDisplayed();

		createdUsername = data(1, "Username") + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

		addUser.fillForm(data(1, "UserRole"), data(1, "EmployeeName"), data(1, "Status"), createdUsername,
				data(1, "Password"), data(1, "ConfirmPassword"));

		addUser.clickSave();

		addUser.waitForUserListPage();

		Assert.assertTrue(driver.getCurrentUrl().contains("viewSystemUsers"),
				"User list page not displayed after save");
	}

	@Test(priority = 6, dependsOnMethods = "verifyAddValidUser")
	public void verifyDuplicateUsernameError() {

		openAddUserPage();

		AddUserPage addUser = new AddUserPage(driver);

		addUser.isAddUserHeaderDisplayed();

		addUser.enterUsername(createdUsername);

		Assert.assertTrue(addUser.isUsernameExistsDisplayed(), "Duplicate username error not displayed");
	}

	@Test(priority = 7, dependsOnMethods = "verifyAddValidUser")
	public void verifySearchNewUser() {

		openUserListPage();

		AdminPage admin = new AdminPage(driver);

		admin.isAdminPageDisplayed();

		admin.enterUsername(createdUsername);

		admin.clickSearch();

		Assert.assertTrue(admin.isUserDisplayed(createdUsername), "New user not found in table");

		Assert.assertEquals(admin.getRowCount(), 1, "Search should return exactly one record");
	}

	@Test(priority = 8, dependsOnMethods = "verifyAddValidUser")
	public void verifyEditUser() {

		openUserListPage();

		AdminPage admin = new AdminPage(driver);

		admin.isAdminPageDisplayed();

		admin.enterUsername(createdUsername);

		admin.clickSearch();

		admin.clickEditUser(createdUsername);

		AddUserPage editUser = new AddUserPage(driver);

		Assert.assertTrue(editUser.isEditUserHeaderDisplayed(), "Edit User page not displayed");

		editUser.selectStatus(data(1, "EditedStatus"));

		editUser.clickSave();

		editUser.waitForUserListPage();

		Assert.assertTrue(driver.getCurrentUrl().contains("viewSystemUsers"),
				"User list page not displayed after edit");
	}

	@Test(priority = 9, dependsOnMethods = "verifyAddValidUser")
	public void verifyEditedUserStatus() {

		openUserListPage();

		AdminPage admin = new AdminPage(driver);

		admin.isAdminPageDisplayed();

		admin.enterUsername(createdUsername);

		admin.clickSearch();

		Assert.assertEquals(admin.getUserStatus(createdUsername), data(1, "EditedStatus"), "Edited status mismatch");
	}

	@Test(priority = 10, dependsOnMethods = "verifyAddValidUser")
	public void verifyDeleteUser() {

		openUserListPage();

		AdminPage admin = new AdminPage(driver);

		admin.isAdminPageDisplayed();

		admin.enterUsername(createdUsername);

		admin.clickSearch();

		admin.clickDeleteUser(createdUsername);

		Assert.assertTrue(admin.isDeleteConfirmationDisplayed(), "Delete confirmation not displayed");

		admin.confirmDelete();

		admin.clickSearch();

		Assert.assertTrue(admin.isNoRecordsFoundDisplayed(), "User still exists after delete");
	}
	
	}