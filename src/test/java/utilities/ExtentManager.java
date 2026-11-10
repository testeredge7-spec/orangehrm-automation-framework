package utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

	private static ExtentReports extent;

	public static ExtentReports getInstance() {

		if (extent == null) {

			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

			ExtentSparkReporter sparkReporter = new ExtentSparkReporter(
					"ExtentReports/ExtentReport_" + timeStamp + ".html");

			sparkReporter.config().setReportName("OrangeHRM Automation Report");

			sparkReporter.config().setDocumentTitle("OrangeHRM Report");

			extent = new ExtentReports();

			extent.attachReporter(sparkReporter);

			extent.setSystemInfo("Framework", "Selenium + TestNG");

			extent.setSystemInfo("Execution", "Single Browser Session");
		}

		return extent;
	}
}