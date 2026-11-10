package utilities;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import base.BaseTest;

public class TestListener implements ITestListener {

	private ExtentReports extent = ExtentManager.getInstance();

	private ExtentTest test;

	@Override
	public void onTestStart(ITestResult result) {

		test = extent.createTest(result.getMethod().getMethodName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {

		test.pass("Test Passed");

		ExcelUtil.writeResult(result.getMethod().getMethodName(), "PASS");
	}

	@Override
	public void onTestFailure(ITestResult result) {

		test.fail(result.getThrowable());

		String screenshotPath = ScreenshotUtil.captureScreenshot(BaseTest.driver, result.getMethod().getMethodName());

		if (screenshotPath != null) {
			test.addScreenCaptureFromPath(screenshotPath);
		}

		ExcelUtil.writeResult(result.getMethod().getMethodName(), "FAIL");
	}

	@Override
	public void onTestSkipped(ITestResult result) {

		test.skip("Test Skipped");

		ExcelUtil.writeResult(result.getMethod().getMethodName(), "SKIP");
	}

	@Override
	public void onFinish(ITestContext context) {

		extent.flush();
	}
}