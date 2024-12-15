package com.vikram.resources;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReporterNG {


	public static ExtentReports getReportObject() {
		
		// Extent Reports , ExtentSparkReporter
		//String path = System.getProperty("user.dir") + "\\reports\\index.html";
		String path = System.getProperty("user.dir") + "//reports/index.html";

		ExtentSparkReporter reporter = new ExtentSparkReporter(path);
		reporter.config().setReportName("We Automation Results");
		reporter.config().setDocumentTitle("Test Results");

		ExtentReports extent = new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("Tester", "Vikram Singh Gurjar");
		return extent;

	}

}
