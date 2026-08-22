package com.vikram.reporting;

import java.nio.file.Paths;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReporterNG {


	public static ExtentReports getReportObject() {
		
		// Written under target/ so reports are a build artefact, not tracked source.
		String path = Paths.get("target", "reports", "index.html").toAbsolutePath().toString();

		ExtentSparkReporter reporter = new ExtentSparkReporter(path);
		reporter.config().setReportName("UI + API Automation Results");
		reporter.config().setDocumentTitle("Test Results");

		ExtentReports extent = new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("Tester", "Vikram Singh Gurjar");
		return extent;

	}

}
