package com.orangehrm.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.orangehrm.base.BaseClass;

public class DBConnection {
	
	private static final String DB_URL="jdbc:mysql://localhost:3306/orangehrm";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "";
	public static final Logger logger = BaseClass.logger;
	
	// Method to connect the DB
	public static Connection getDbConnection() {
		try {
			logger.info("Starting DB connection...");
			Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			logger.info("DB connection successfull");
			return conn;
		} catch (SQLException e) {
			logger.error("Error while connecting DB Connection");
			e.printStackTrace();
			return null;
		}
	}
	
	// Get the employee details form Db and store in a Map
	public static Map<String, String> getEmployeeDetails(String employeeId){
		String query = "SELECT employee_id,emp_firstname,emp_middle_name,emp_lastname FROM hs_hr_employee WHERE employee_id = '"+employeeId+"'";
		
		Map<String, String> employeeDetails = new HashMap<>();
		
		try(Connection conn = getDbConnection(); 
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(query)){
			logger.info("Execuing query : "+query);
			if(rs.next()) {
				employeeDetails.put("id", rs.getString("employee_id"));
				employeeDetails.put("firstName", rs.getString("emp_firstname"));
				employeeDetails.put("middleName", rs.getString("emp_middle_name"));
				employeeDetails.put("lastName", rs.getString("emp_lastname"));
				logger.info("Query executed successfully");
			}
			else {
				logger.info("Employees details not found");
			}
			
		}
		catch(Exception e) {
			logger.error("Error while execuing query : "+query);
			e.printStackTrace();
		}
		return employeeDetails;
	}
}
