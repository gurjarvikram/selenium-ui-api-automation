package com.vikram.listeners;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.vikram.core.DriverManager;
import com.vikram.reporting.ExtentReporterNG;
import com.vikram.utils.ScreenshotUtils;

/**
 * Extent reporting hooks.
 *
 * The driver is read from {@link DriverManager} rather than reflected off the test
 * instance, so screenshots keep working under parallel execution and the API suite --
 * which has no browser -- simply skips the capture.
 */
public class Listeners implements ITestListener {

	private final ExtentReports extent = ExtentReporterNG.getReportObject();
	private final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

	@Override
	public void onTestStart(ITestResult result) {
		extentTest.set(extent.createTest(result.getMethod().getMethodName()));
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		extentTest.get().log(Status.PASS, "Test passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		ExtentTest test = extentTest.get();
		test.fail(result.getThrowable());

		if (!DriverManager.isSet()) {
			return;
		}
		try {
			String path = ScreenshotUtils.capture(DriverManager.get(), result.getMethod().getMethodName());
			test.addScreenCaptureFromPath(path, result.getMethod().getMethodName());
		} catch (IOException | RuntimeException e) {
			test.warning("Could not attach screenshot: " + e.getMessage());
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		ExtentTest test = extentTest.get();
		if (test != null) {
			test.log(Status.SKIP, "Test skipped");
		}
	}

	@Override
	public void onFinish(ITestContext context) {
		extent.flush();
	}
}
