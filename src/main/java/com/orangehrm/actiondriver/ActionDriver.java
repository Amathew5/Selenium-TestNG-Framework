package com.orangehrm.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;
	
	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explictWait = Integer.parseInt(BaseClass.getProp().getProperty("expliciteWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explictWait));
		logger.info("WebDriver instance is created");
	}
	
	// method to wait till the element is clickable
	public void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Unable to element is not clickable: "+by+" : "+e.getMessage());
		}
	}
	
	// Method to click on an element
	public void click(By by, String elementName) {
		try {
			waitForElementToBeClickable(by);
			applyBorder(by,"green");
			driver.findElement(by).click();
			ExtentManager.logStep("Clicked on "+elementName);
			logger.info("Clicked on "+elementName);
		} catch (Exception e) {
			ExtentManager.logFailureWithScreenshot(BaseClass.getDriver(),"Unable to click on "+elementName);
			logger.error("Unable to click on "+elementName+" : "+e.getMessage());
		}
	}
	
	// method to enter value in the element
	public void enterText(By by,String elementName,  String value) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by,"green");
			driver.findElement(by).clear();
			driver.findElement(by).sendKeys(value);
			ExtentManager.logStep("Entered value in "+elementName+" field");
			logger.info("Entered value in "+elementName+" field");
		} catch (Exception e) {
			logger.info("Unable to enter value in the "+elementName+" field : "+e.getMessage());
			
		}
	}
	
	// Method to get text from the element
	public String getText(By by, String elementName) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by,"green");
			String text = driver.findElement(by).getText();
			ExtentManager.logStep("Get text from "+elementName);
			logger.info("Get text from "+elementName);
			return text;
		} catch (Exception e) {
			logger.info("Unable get text from "+elementName+" : "+e.getMessage());
			return "";
		}
	}
	
	// Method to compare text
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by,"green");
			String actualText = driver.findElement(by).getText();
			if(expectedText.equals(actualText)) {
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Text verified successfully! "+actualText+" equals "+expectedText);
				logger.info("Text are matching: "+actualText+" equals "+expectedText);
				return true;
			}
			else {
				applyBorder(by,"red");
				ExtentManager.logFailureWithScreenshot(BaseClass.getDriver(),"Test verification failed! "+actualText+" not equals to "+expectedText);
				logger.info("Text are not matching: "+actualText+" not equals "+expectedText);
				return false;
			}
			
		} catch (Exception e) {
			logger.error("Unable to compare Text: "+by+" : "+e.getMessage());
		}
		return false;
	}
	
	// Method to wait till the element is visible
	public void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element is not visible:"+by+" : "+e.getMessage());
		}
	}
	
	// Method to check the element is displayed
	public boolean isDisplayed(By by, String logMessage) {
		boolean displayed = false;
		try {
			waitForElementToBeVisible(by);
			applyBorder(by,"green");
			logger.info(logMessage);
			displayed = driver.findElement(by).isDisplayed();
			if(displayed)
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), logMessage);
			return displayed;
		} catch (Exception e) {
			logger.error("Element is not displayed: "+by+" : "+e.getMessage());
		}
		return displayed;
	}
	
	// Method to scroll to an element
	public void scrollToElement(By by) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			logger.error("Unable to scroll to the element"+e.getMessage());
		}
	}
	
	// Method to load the page completely
	public void waitToPageLoad(int timeoutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeoutInSec))
			.until(WebDriver -> ((JavascriptExecutor) WebDriver).executeScript("return document.readyState")
					.equals("complete"));
			logger.info("Page is loaded successfully");

		} catch (Exception e) {
			logger.error("Unable to load the pafe after the timeout period:"+e.getMessage());
		}
	}
	
	// Method to check string is not null or empty
	public boolean isNotEmpty(String name) {
		return name!=null && !name.isEmpty();
	}
	
	// Utility method to border an element
	public void applyBorder(By by, String color) {
		try {
			WebElement element = driver.findElement(by);
			String script = "arguments[0].style.border='3px solid "+color+"'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
		} catch (Exception e) {
			logger.warn("Fail to appy the border to an element: "+by);
		}
	}
	
	// Method to select a dropdown by visibility text
	public void selecteByVisibilityText(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByVisibleText(value);
			applyBorder(by, "green");
			logger.info("Selected dropdown value: "+value);
		} catch(Exception e) {
			logger.error("Unable to select dropdown value: "+value, e);
		}
	}
	
	// Method to select a dropdown by value
	public void selecteByValue(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByValue(value);
			applyBorder(by, "green");
			logger.info("Selected dropdown value: "+value);
		} catch(Exception e) {
			logger.error("Unable to select dropdown value: "+value, e);
		}
	}
	
	public void selecteByIndex(By by, int index) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByIndex(index);
			applyBorder(by, "green");
			logger.info("Selected dropdown value by index: "+index);
		} catch(Exception e) {
			logger.error("Unable to select dropdown value by index: "+index, e);
		}
	}
}
