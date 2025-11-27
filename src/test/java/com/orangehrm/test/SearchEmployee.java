package com.orangehrm.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.PimPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class SearchEmployee extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;
	private PimPage pimPage;


	@BeforeMethod
	public void setupPages() {
		this.loginPage = new LoginPage(getDriver());
		this.homePage = new HomePage(getDriver());
		this.pimPage = new PimPage(getDriver());
	}
	
	@Test(dataProvider = "verifyEmployeeDetailFromDB", dataProviderClass = DataProviders.class)
	public void verifyEmployeeDetailFromDB(String username, String password, String employeeId) {
		loginPage.loginDemoHRM(username, password);
		Assert.assertTrue(homePage.verifyOrangeHRMIsVisible());
		
		homePage.clickOnPimTab(); 
		pimPage.verifyPimPageDisplayed();
		
		Map<String, String> employeDetails = DBConnection.getEmployeeDetails(employeeId);
		String id = employeDetails.get("id");
		String firstAndMiddleName = (employeDetails.get("firstName")+" "+employeDetails.get("middleName")).trim();
		String lastname = employeDetails.get("lastName");
		
		SoftAssert softAssert = getSoftAssert();
		pimPage.searchEmployeeWithId(employeeId);
		softAssert.assertTrue(pimPage.verifyEmployeeId(id));
		softAssert.assertTrue(pimPage.verifyFirstAndMiddlename(firstAndMiddleName));
		softAssert.assertTrue(pimPage.verifyLastName(lastname));
		softAssert.assertAll();
		
		ExtentManager.logStep("Login Validated successfully");
		homePage.logout();
		ExtentManager.logStep("Logout Successfully");
	}
}
