package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {
	
	private ActionDriver actionDriver;
	
	/*-----------------------------------Locators-----------------------------------------------------*/
	private By orangeHrm = By.xpath("//div[@class = 'oxd-brand-banner']/img");
	private By adminTab = By.xpath("//span[text() = 'Admin']");
	private By pimTab = By.xpath("//span[text() = 'PIM']");
	private By userIdButton = By.className("oxd-userdropdown-name");
	private By logout = By.xpath("//a[text() = 'Logout']");
	
	
	/*-----------------------------------Methods-----------------------------------------------------*/
	
	// Constructor
	public HomePage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	// Method to verify the Orange Hrm logo is displayed of not
	public boolean verifyOrangeHRMIsVisible() {
		return actionDriver.isDisplayed(orangeHrm, "OrangeHRM login is displayed");
	}
	
	// Method to verify the Admin tab is displayed of not
	public boolean verifyAdminTabIsVisible() {
		return actionDriver.isDisplayed(adminTab, "Admin tab is displayed");
	}
	
	// Method to logout OrangeHrm
	public void logout() {
		actionDriver.click(userIdButton, "User dropdown");
		actionDriver.click(logout, "Logout button");
	}
	
	// Method to click on PIM tab
	public void clickOnPimTab() {
		actionDriver.click(pimTab, "PIM tab");
	}


}
