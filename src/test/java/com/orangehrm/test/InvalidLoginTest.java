package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;

public class InvalidLoginTest extends BaseClass {
	private LoginPage loginPage;
	
	@BeforeMethod
	public void setupPages() {
		this.loginPage = new LoginPage(getDriver());
	}

	@Test(dataProvider="invalidLoginTestData", dataProviderClass = DataProviders.class)
	public void validInValidLoginTest(String username, String password) {
		loginPage.loginDemoHRM(username, password);
		Assert.assertTrue(loginPage.verifyErrorMessage());
	}
}
