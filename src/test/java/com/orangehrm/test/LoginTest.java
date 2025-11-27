package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		this.loginPage = new LoginPage(getDriver());
		this.homePage = new HomePage(getDriver());
	}

	@Test(dataProvider="loginTestData", dataProviderClass = DataProviders.class)
	public void verifyLogin(String username, String password) {
		loginPage.loginDemoHRM(username, password);
		getActionDriver().waitToPageLoad(20);
		Assert.assertTrue(homePage.verifyOrangeHRMIsVisible());
		Assert.assertTrue(homePage.verifyAdminTabIsVisible());
		ExtentManager.logStep("Login Validated successfully");
		homePage.logout();
		ExtentManager.logStep("Logout Successfully");
	}

}
