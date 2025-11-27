package com.orangehrm.test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.ApiUtility;
import com.orangehrm.utilities.ExtentManager;

import io.restassured.response.Response;

public class ApiTest {

	@Test
	public void verifyGetUserApi() {
		SoftAssert softAssert = new SoftAssert();
		
		// Step 1: Define Api Endpoint
		String endPoint = "https://jsonplaceholder.typicode.com/users/1";		
		ExtentManager.logStep("Api Endpoint : "+endPoint);		
		
		// Step 2: Sent GET request
		ExtentManager.logStep("Sending GET request");
		Response response = ApiUtility.sendGetRequest(endPoint);
		
		// Step 3 : Validate status code
		ExtentManager.logStep("Validating API Response staus code");
		ApiUtility.validateStatusCode(softAssert,response,200);
		
		// Step 4 : Validate response body attribute
		ExtentManager.logStep("Validating API response body attributes");		
		ApiUtility.validateResponseBodyAttributes(softAssert,response,"username", "Bret");
		ApiUtility.validateResponseBodyAttributes(softAssert,response,"email", "Sincere@april.biz");
		softAssert.assertAll();
	}
}
