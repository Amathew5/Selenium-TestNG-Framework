package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long, WebDriver> driverMap = new HashMap<>();

	// Initialize Extent report
	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "\\src\\test\\resources\\ExtentReport\\ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("AutomationReport");
			spark.config().setDocumentTitle("DemoHRM Report");
			spark.config().setTheme(Theme.DARK);

			extent = new ExtentReports();
			extent.attachReporter(spark);
			// Adding System information
			extent.setSystemInfo("Operation System", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));
		}
		return extent;
	}

	// Start the test
	public synchronized static ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}

	// End the test
	public synchronized static void endTest() {
		getReporter().flush();
	}
	
	// Get Current Threads test
	public synchronized static ExtentTest getTest() {
		return test.get();
	}
	
	// Method to get the name Of the current thread
	public static String getTestname() {
		ExtentTest currentTest = getTest();
		if(currentTest!=null) {
			return currentTest.getModel().getName();
		}
		else {
			return "No test is current active for this thread";
		}
	}
	
	// Log a step
	public static void logStep(String logMessage) {
		getTest().info(logMessage);
	}
	
	// Log a step validation with Screenshot
		public static void logStepWithScreenshot(WebDriver driver, String logMessage) {
			getTest().pass(logMessage);
			attachScreenshot(driver, logMessage);
		}
	
	// Log a Pass
	public static void logPass(String logMessage) {
		String colorMessage = String.format("<span style='color:green;'>%s</span>", logMessage);
		getTest().pass(colorMessage);
	}
	
	// Log a failure
	public static void logFailure(String logMessage) {
		String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
		getTest().fail(colorMessage);
	}
	
	// Log a pass or fail
		public static void logPassOrFail(boolean pass, String logMessage) {
			if(pass)
				logPass(logMessage);
			else
				logFailure(logMessage);
		}
	
	// Log a error
	public static void logFailureWithScreenshot(WebDriver driver, String logMessage) {
		String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
		getTest().fail(colorMessage);
		attachScreenshot(driver, logMessage);
	}
	
	// log a skip
	public static void logSkip(String logMessage) {
		String colorMessage = String.format("<span style='color:yellow;'>%s</span>", logMessage);
		getTest().skip(colorMessage);
	}
	
	//Take screenshot with date and time in the file
	public synchronized static String takeScreenshot(WebDriver driver, String screenshotName) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		//Format date and time for filename
		String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		// Save the screenshot to path
		String destPath = System.getProperty("user.dir")+"\\src\\test\\resources\\screenshot\\"+screenshotName+"_"+timestamp+".png";
		File finalPath = new File(destPath);
		try {
			FileUtils.copyFile(src, finalPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Convert the screenshot to base 64 for embedding in the report
		return ts.getScreenshotAs(OutputType.BASE64);
	}
	
	//Attach the screenshot to report using Base64
	public synchronized static void attachScreenshot(WebDriver driver, String message) {
		try {
			String screenshotBase64 = takeScreenshot(driver, getTestname());
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
		} catch (Exception e) {
			getTest().fail("Fail to attach screenshot to report");
			e.printStackTrace();
		}
	}

	// Register Web Driver for current thread
	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
	}
}
