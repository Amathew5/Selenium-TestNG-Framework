package com.orangehrm.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.orangehrm.base.BaseClass;

public class ExcelReaderUtility {
	public static final Logger logger = BaseClass.logger;
	
	// Method to get the test data from xls sheet
	public static List<String[]> getSheetData(String filePath, String sheetName){
		List<String[]> data = new ArrayList<>();
		
		try(FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)){
			
			Sheet sheet = workbook.getSheet(sheetName);
			if(sheet==null) {
				logger.error("❌ Sheet not found: " + sheetName);
                return data; // return empty list safely
			}
			
			for(Row row:sheet) {
				if(row.getRowNum() == 0)
					continue;
				List<String> rowData = new ArrayList<>();
				for(Cell cell:row)
					rowData.add(getCellValue(cell));
				data.add(rowData.toArray(new String[0]));
			}
		}
		catch(IOException e) {
			logger.error("❌ Error reading Excel file: " + e.getMessage());
            return new ArrayList<>(); // safe fallback
		}
		return data;
	}

	// Method to get the value from Cell
	private static String getCellValue(Cell cell) {
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toString();
			}
			return String.valueOf(cell.getNumericCellValue());
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		default:
			return "";
		}
	}
}
