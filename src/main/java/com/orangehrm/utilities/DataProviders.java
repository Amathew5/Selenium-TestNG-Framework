package com.orangehrm.utilities;

import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {
	
	private static final String FILE_PATH = System.getProperty("user.dir")+"\\src\\test\\resources\\testdata\\TestData.xlsx";

	@DataProvider(name="loginTestData")
	public static Object[][] loginTestData(){
		return getSheetData("loginTestData");
	}
	
	@DataProvider(name="invalidLoginTestData")
	public static Object[][] invalidLoginTestData(){
		return getSheetData("invalidLoginTestData");
	}
	
	@DataProvider(name="verifyEmployeeDetailFromDB")
	public static Object[][] verifyEmployeeDetailFromDB(){
		return getSheetData("verifyEmployeeDetailFromDB");
	}
	
	private static Object[][] getSheetData(String sheetName) {
		List<String[]> sheetData = ExcelReaderUtility.getSheetData(FILE_PATH, sheetName);
		
		Object[][] data = new Object[sheetData.size()][sheetData.get(0).length];
		
		for(int i = 0; i<sheetData.size();i++)
			data[i] = sheetData.get(i);
		
		return data;
	}
}
