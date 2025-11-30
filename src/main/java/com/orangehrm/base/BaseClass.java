package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	private static ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

	@BeforeSuite
	public void loadConfig() throws IOException {
		// load the configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\config.properties");
		prop.load(fis);
		logger.info("config.properties are loaded");
	}

	// Method for initial setup
	@BeforeMethod
	@Parameters("browser")
	public synchronized void setup(String browser) throws IOException {
		logger.info("Setting up webdriver:" + this.getClass().getSimpleName());
		launchBrowser(browser);
		navigateToUrl();
		staticWait(3);

		// Initialize the actionDriver only once
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver instance created");
	}

	// Method to launch browser
	private void launchBrowser(String browser) {
		//String browser = prop.getProperty("browser");
		
		boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
		String gridUrl = prop.getProperty("hubUrl");
		
		if(seleniumGrid) {
			try {
				if(browser.equalsIgnoreCase("chrome")) {
					ChromeOptions options = new ChromeOptions();
					options.addArguments("--headless=new");
					options.addArguments("--window-size=1920,1080");  // REQUIRED
					options.addArguments("--force-device-scale-factor=1");
					options.addArguments("--disable-notification");
					options.addArguments("--disable-gpu");
					options.addArguments("--disable-dev-shm-usage");
					options.addArguments("--no-sandbox");
					driver.set(new RemoteWebDriver(new URL(gridUrl),options));
				}
				else if (browser.equalsIgnoreCase("firefox")) {
					FirefoxOptions options = new FirefoxOptions();
					options.addArguments("--headless");// Run firefox in headless mode
					options.addArguments("--disable-gpu"); // Disable GPU for headless mode
					options.addArguments("--disable-notification");
					options.addArguments("--no-sandbox");
					options.addArguments("--disable-dev-shm-usage");
					driver.set(new RemoteWebDriver(new URL(gridUrl),options));
				}			
				else if (browser.equalsIgnoreCase("edge")) {
					EdgeOptions options = new EdgeOptions();
					options.addArguments("--headless");// Run Edge in headless mode
					options.addArguments("--disable-gpu"); // Disable GPU for headless mode
					options.addArguments("--disable-notification");
					options.addArguments("--no-sandbox");
					options.addArguments("--disable-dev-shm-usage");
					driver.set(new RemoteWebDriver(new URL(gridUrl),options));
				}
				else {
					throw new IllegalArgumentException("Browser: "+browser+" Not Supported.");
				}
			}catch (MalformedURLException e) {
					throw new RuntimeException("Invalide grid url", e);
			}
			
		} else {
			
			if (browser.equalsIgnoreCase("chrome")) {
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--headless=new");
				options.addArguments("--window-size=1920,1080");  // REQUIRED
				options.addArguments("--force-device-scale-factor=1");
				options.addArguments("--disable-notification");
				options.addArguments("--disable-gpu");
				options.addArguments("--disable-dev-shm-usage");
				options.addArguments("--no-sandbox");
				driver.set(new ChromeDriver(options));
			}			
			else if (browser.equalsIgnoreCase("firefox")) {
				FirefoxOptions options = new FirefoxOptions();
				options.addArguments("--headless");// Run firefox in headless mode
				options.addArguments("--disable-gpu"); // Disable GPU for headless mode
				options.addArguments("--disable-notification");
				options.addArguments("--no-sandbox");
				options.addArguments("--disable-dev-shm-usage");
				driver.set(new FirefoxDriver(options));
			}			
			else if (browser.equalsIgnoreCase("edge")) {
				EdgeOptions options = new EdgeOptions();
				options.addArguments("--headless");// Run Edge in headless mode
				options.addArguments("--disable-gpu"); // Disable GPU for headless mode
				options.addArguments("--disable-notification");
				options.addArguments("--no-sandbox");
				options.addArguments("--disable-dev-shm-usage");
				driver.set(new EdgeDriver(options));
			}
			else {
				throw new IllegalArgumentException("Browser not supported:" + browser);
			}
			
		}
		
		ExtentManager.registerDriver(getDriver());
	}

	// Method to Navigate to required Url
	private void navigateToUrl() {
		int implicitwait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitwait));

		// Maximize the driver
		//getDriver().manage().window().maximize();--> not correctly working in headless mode
		getDriver().manage().window().setSize(new Dimension(1920, 1080));

		try {
			getDriver().get(prop.getProperty("url"));
			logger.info("Navigate to url");
		} catch (Exception e) {
			logger.error("Navigate to url");
		}
	}

	// Method to tear down driver and action class
	@AfterMethod
	public void tearDown() {
		if (getDriver() != null)
			getDriver().quit();

		logger.info("WebDriver instanced is closed");
		
		driver.remove();
		actionDriver.remove();
	}

	// Method to get the driver
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			logger.error("WebDriver is not initilized");
		}
		return driver.get();
	}

	// Method to get the Action Driver
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			logger.error("Action Driver is not initilized");
			throw new IllegalStateException("Action Driver is not initilized");
		}
		return actionDriver.get();
	}
	
	// Method to get Properties
	public static Properties getProp() {
		return prop;
	}
	
	// Method for static wait
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
	
	// Method to get SoftAssert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}
}
