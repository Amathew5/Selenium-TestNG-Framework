package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class PimPage {
	
	private ActionDriver actionDriver;
	
	private By pimHeader = By.xpath("//h6[text() = 'PIM']");
	private By employeeIdField = By.xpath("//label[text()='Employee Id']/parent::div/following-sibling::div//input");
	private By searchButton = By.xpath("//button[text() = ' Search ']");
	private By idInTable = By.xpath("//div[@class='oxd-table-card']/div/div[2]/div");
	private By firstAndMiddleNameinTable = By.xpath("//div[@class='oxd-table-card']/div/div[3]/div");
	private By lastNameInTable = By.xpath("//div[@class='oxd-table-card']/div/div[4]/div");
	
	public PimPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	// Method to Search employee with Id
	public void searchEmployeeWithId(String employeeId) {
		actionDriver.enterText(this.employeeIdField, "Employee Id", employeeId);
		actionDriver.click(this.searchButton, "Search button");
	}
	
	// Method to verify the pim page is displayed
	public boolean verifyPimPageDisplayed() {
		return actionDriver.isDisplayed(this.pimHeader, "PIm Header");
	}
	
	// Method to verify the EmployeeId
	public boolean verifyEmployeeId(String expectedId) {
		actionDriver.scrollToElement(idInTable);
		return actionDriver.compareText(this.idInTable, expectedId);
	}
	
	// Method to verify the First and middle name
	public boolean verifyFirstAndMiddlename(String expecteFirstAndMiddleName) {
		actionDriver.scrollToElement(firstAndMiddleNameinTable);
		return actionDriver.compareText(this.firstAndMiddleNameinTable, expecteFirstAndMiddleName);
	}
	
	// Method to verify the last name
	public boolean verifyLastName(String expectedLastName) {
		actionDriver.scrollToElement(lastNameInTable);
		return actionDriver.compareText(this.lastNameInTable, expectedLastName);
	}
	
}
