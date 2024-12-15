package com.vikram.TestComponents;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.vikram.resources.ExtentReporterNG;

public class Listeners extends BaseTest implements ITestListener{
	
	ExtentTest test;
	ExtentReports extent = ExtentReporterNG.getReportObject();
	ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();  //Thread safe
	
    @Override
    public void onTestStart(ITestResult result) {
        	
        test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);  //unique thread id  -> test
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        //System.out.println("Test Passed: " + result.getName());
    	extentTest.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        //System.out.println("Test Failed: " + result.getName());
    	extentTest.get().fail(result.getThrowable());
        
        
        try {
			driver = (WebDriver) result.getTestClass().getRealClass().getField("driver")
					.get(result.getInstance());
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        String filePath = null;        
		try {
			filePath = getScreenshot(result.getMethod().getMethodName(),driver);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		extentTest.get().addScreenCaptureFromPath(filePath, result.getMethod().getMethodName());
         
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        //System.out.println("Test Skipped: " + result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        //System.out.println("Test Partially Passed: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        //System.out.println("Test Suite Started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        //System.out.println("Test Suite Finished: " + context.getName());
    	extent.flush();
    }

}
