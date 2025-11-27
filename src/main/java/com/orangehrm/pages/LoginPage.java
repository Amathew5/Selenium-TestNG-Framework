package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class LoginPage {
	
	private ActionDriver actionDriver;
	
	private By username = By.name("username");
	private By password = By.cssSelector("input[type='password']");
	private By loginbutton = By.xpath("//button[text() = ' Login ']");
	private By errorContent = By.xpath("//div[@class='oxd-alert-content oxd-alert-content--error']/p");
	
	public LoginPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	// Method to Login orangeHRM with username and password
	public void loginDemoHRM(String username, String password) {
		ExtentManager.logStep("Navigate to login page and enter username and password");
		actionDriver.enterText(this.username,"Username", username);
		actionDriver.enterText(this.password,"Password", password);
		actionDriver.click(loginbutton, "Login Button");
	}
	
	// Method to verify the Admin tab is displayed of not
	public boolean verifyErrorMessage() {
		return actionDriver.compareText(errorContent, "Invalid credentials");
	}



}
