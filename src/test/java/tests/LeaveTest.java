package tests;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.DashboardPage;
import pages.LeavePage;



public class LeaveTest extends BaseTest {

	private static String leaveType;

	private static String comment;

	private String pickWorkingDate(String pattern) {

		Random random = new Random();

		LocalDate today = LocalDate.now();

		int maxOffset = (int) ChronoUnit.DAYS.between(today, LocalDate.of(today.getYear(), 12, 31));

		for (int i = 0; i < 20; i++) {

			LocalDate date = today.plusDays(1 + random.nextInt(Math.max(1, Math.min(40, maxOffset))));

			boolean weekend = date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;

			if (!weekend && date.getYear() == today.getYear()) {

				return date.format(DateTimeFormatter.ofPattern(pattern));
			}
		}

		return today.plusDays(1).format(DateTimeFormatter.ofPattern(pattern));
	}

	@Test(priority = 1)
	public void verifyLeaveMenuAndTabs() {

		DashboardPage dashboard = new DashboardPage(driver);

		dashboard.clickLeave();

		LeavePage leave = new LeavePage(driver);

		leave.waitForUrl("viewLeaveList");

		Assert.assertTrue(leave.isHeaderDisplayed("Leave List"), "Leave List header not displayed");

		String[] tabs = { "Apply", "My Leave", "Entitlements", "Reports", "Configure", "Leave List", "Assign Leave" };

		for (String tab : tabs) {

			Assert.assertTrue(leave.isTabDisplayed(tab), tab + " tab not displayed");
		}
	}

	@Test(priority = 2)
	public void verifyApplyLeavePageFields() {

		LeavePage leave = new LeavePage(driver);

		leave.clickTab("Apply");

		leave.waitForUrl("applyLeave");

		Assert.assertTrue(leave.isHeaderDisplayed("Apply Leave"), "Apply Leave header not displayed");

		String[] fields = { "Leave Type", "Leave Balance", "From Date", "To Date", "Comments" };

		for (String field : fields) {

			Assert.assertTrue(leave.isFieldDisplayed(field), field + " not displayed");
		}

		Assert.assertTrue(leave.isApplyButtonDisplayed(), "Apply button not displayed");
	}

	@Test(priority = 3)
	public void verifyApplyLeaveMandatoryErrors() {

		LeavePage leave = new LeavePage(driver);

		leave.clickTab("Apply");
		leave.waitForUrl("applyLeave");

		leave.clickApply();

		Assert.assertTrue(leave.getRequiredErrorCount() >= 3, "Required errors not displayed for all mandatory fields");
	}

	@Test(priority = 4)
	public void verifyLeaveBalanceForLeaveType() {

		LeavePage leave = new LeavePage(driver);

		leave.clickTab("Apply");
		leave.waitForUrl("applyLeave");

		leaveType = leave.selectLeaveTypeWithBalance(1.0);

		Assert.assertNotNull(leaveType, "No leave type with a balance of at least 1 day is available");

		Assert.assertTrue(leave.getLeaveBalanceText().contains("Day(s)"), "Leave balance text not displayed");

		Assert.assertTrue(leave.getLeaveBalance() >= 1.0, "Leave balance is less than 1 day");
	}
	
	@Test(priority = 5, dependsOnMethods = "verifyLeaveBalanceForLeaveType")
	public void verifyApplyLeave() {

		LeavePage leave = new LeavePage(driver);

		String pattern = leave.getDatePattern();

		comment = "AutoLeave_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

		boolean applied = false;

		for (int attempt = 0; attempt < 6 && !applied; attempt++) {

			String date = pickWorkingDate(pattern);

			leave.enterFromDate(date);

			leave.enterToDate(date);

			leave.enterComments(comment);

			applied = leave.clickApplyAndCheckSuccess();
		}

		Assert.assertTrue(applied, "Leave could not be applied (success message not displayed)");
	}

	@Test(priority = 6, dependsOnMethods = "verifyApplyLeave")
	public void verifyAppliedLeaveInMyLeave() {

		LeavePage leave = new LeavePage(driver);

		leave.clickTab("My Leave");

		leave.waitForUrl("viewMyLeaveList");

		leave.clickSearch();

		String row = leave.getRowTextByComment(comment);

		Assert.assertNotNull(row, "Applied leave not found in My Leave");

		Assert.assertTrue(row.contains("Pending Approval"), "Leave status is not Pending Approval : " + row);

		Assert.assertTrue(row.contains(leaveType), "Leave type mismatch : " + row);
	}

	@Test(priority = 7, dependsOnMethods = "verifyApplyLeave")
	public void verifyAppliedLeaveInLeaveList() {

		LeavePage leave = new LeavePage(driver);

		leave.clickTab("Leave List");

		leave.waitForUrl("viewLeaveList");

		leave.clickSearch();

		String row = leave.getRowTextByComment(comment);

		Assert.assertNotNull(row, "Applied leave not found in Leave List");

		Assert.assertTrue(row.contains("Pending Approval"), "Leave status is not Pending Approval : " + row);
	}

	@Test(priority = 8, dependsOnMethods = "verifyApplyLeave")
	public void verifyLeaveListFilterByLeaveType() {

		LeavePage leave = new LeavePage(driver);

		leave.clickTab("Leave List");

		leave.waitForUrl("viewLeaveList");

		leave.selectLeaveType(leaveType);

		leave.clickSearch();

		Assert.assertNotNull(leave.getRowTextByComment(comment),
				"Applied leave not found after filtering by leave type");

		for (String row : leave.getRowTexts()) {

			Assert.assertTrue(row.contains(leaveType), "Row does not match leave type " + leaveType + " : " + row);
		}
	}
	@Test(priority = 9, dependsOnMethods = "verifyApplyLeave")
	public void verifyCancelLeave() {

		LeavePage leave = new LeavePage(driver);

		leave.clickTab("My Leave");

		leave.waitForUrl("viewMyLeaveList");

		leave.clickSearch();

		Assert.assertNotNull(leave.getRowTextByComment(comment), "Leave to cancel not found in My Leave");

		Assert.assertTrue(leave.cancelLeaveByComment(comment), "Cancel success message not displayed");

		leave.clickSearch();

		String row = leave.getRowTextByComment(comment);

		Assert.assertNotNull(row, "Cancelled leave not found in My Leave");

		Assert.assertTrue(row.contains("Cancelled"), "Leave status is not Cancelled : " + row);
	}

	@Test(priority = 10, dependsOnMethods = "verifyApplyLeave")
	public void verifyCancelledLeaveNotInPendingList() {

		LeavePage leave = new LeavePage(driver);

		leave.clickTab("Leave List");

		leave.waitForUrl("viewLeaveList");

		leave.clickSearch();

		Assert.assertFalse(leave.isRowPresent(comment), "Cancelled leave is still shown in Pending Approval list");
	}
}