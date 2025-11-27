package com.orangehrm.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

public class TestListener implements ITestListener{

	// Triggered when a test starts
	@Override
	public void onTestStart(ITestResult result) {
		ExtentManager.startTest(testName(result));
		ExtentManager.logStep("Test Started: "+testName(result));
	}

	// Triggered when a test succeeds
	@Override
	public void onTestSuccess(ITestResult result) {
		ExtentManager.logPass("✔ Test Passed : "+testName(result));
	}

	// Triggered when a Test Failed
	@Override
	public void onTestFailure(ITestResult result) {
		if(!result.getTestClass().getName().toLowerCase().contains("api"))
			ExtentManager.logFailureWithScreenshot(BaseClass.getDriver(),"❌ Test Failed : "+testName(result));
		else
			ExtentManager.logFailure("❌ Test Failed : "+testName(result));
	}

	// Triggered when a Test skipped
	@Override
	public void onTestSkipped(ITestResult result) {
		ExtentManager.logSkip("⚠ Test Skipped: "+testName(result));
	}

	// Triggered when the test started
	@Override
	public void onStart(ITestContext context) {
		ExtentManager.getReporter();
	}
	
	// Triggered when the test ends
	@Override
	public void onFinish(ITestContext context) {
		ExtentManager.endTest();
	}
	
	// To get the test name
	private String testName(ITestResult result) {
		return result.getMethod().getMethodName();
	}
}
