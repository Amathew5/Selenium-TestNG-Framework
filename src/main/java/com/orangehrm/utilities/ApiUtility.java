package com.orangehrm.utilities;

import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiUtility {

	// Method to send the GET Request
	public static Response sendGetRequest(String endPoint) {
		return RestAssured.get(endPoint);
	}
	
	// Method to send the POST request
	public static Response sendPostRequest(String endPoint, String payload) {
		return RestAssured.given().header("Content-type","application/json")
			.body(payload)
			.post(endPoint);
			
	}
	
	// method to validate the response status
	public static void validateStatusCode(SoftAssert softAssert, Response response, int statusCode) {
		int actualStatusCode = response.getStatusCode();
		softAssert.assertEquals(actualStatusCode, statusCode, "Status code is not as expected");
		
		if(actualStatusCode==statusCode)
			ExtentManager.logPass("Status code validation passed!");
		else
			ExtentManager.logFailure("Status code validation failed! :: Expected: "+statusCode+" but Actual: "+actualStatusCode);
	}
	
	// method to validate the response status
	public static void validateResponseBodyAttributes(SoftAssert softAssert, Response response, String attributeName, String expectedValue) {
		String actualValue = getJsonValue(response, attributeName);
		softAssert.assertEquals(actualValue, expectedValue, "Response body attribute : '" + attributeName + "' is not as expected");
		
		if (expectedValue.equals(actualValue)) 
			ExtentManager.logPass("Response body attribute : '" + attributeName + "' validation passed!");
		else
			ExtentManager.logFailure("Response body attribute : '" + attributeName + "' validation failed! :: Expected: "+expectedValue+" but Actual: "+actualValue);
	}

	// Method to fetch value form Json response
	private static String getJsonValue(Response response, String value) {
		return response.jsonPath().getString(value);
	}
}
