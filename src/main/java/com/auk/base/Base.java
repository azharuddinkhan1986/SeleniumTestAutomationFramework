package com.auk.base;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.ElementScrollBehavior;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Wait;
import com.auk.utilities.ExtentManager;
import com.auk.utilities.Xls_Reader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class Base {
	
	//#################################################################################################################################################
	//skip variable set to false into test data sheet
	public static boolean skip=false;
	//pass variable set to false into test data sheet
	public static boolean pass=false;
	//fail variable set to false into test data sheet
	public static boolean fail=false;
	//set result overall in test case sheet
	public static boolean isTestPass=true;
	public static WebDriver driver=null;
	public static WebElement element=null;
	public static Properties OR=null;
	public static Properties CONFIG=null;
	public static Properties URL=null;
	//Intializing LOG  using object of Logger at global level
	public static Logger APP_LOGS=null;
	//Intializaing object of Regression Suite  at Global level 
	public static Xls_Reader xlsReader=null;
	//Intilaizing Extent Report object for Starting Writing Report During Runtime at global level(Class level)
	public static ExtentReports report;
	//Initializing Extent Test Object for capturing all the steps into Extent report and Screenshots at global level(Class level)
	public static ExtentTest test;
	public static Wait<WebDriver> wait;
	public String proposalValue=""; 



	//#########################################################################################################################################
	/**
	 * Base Class Constructor to initialize all required Properties Files,webdriver instance,Extent Report instance,reading file(xls) instance.
	 */
	public Base()
	{
		if(driver==null){
			//Configuring OR Properties File	
			OR=new Properties();
			CONFIG=new Properties();
			URL=new Properties();
			FileInputStream fs=null;
			try {
				//Load Config Properties file
				fs=new FileInputStream(System.getProperty("user.dir")+"\\Config.properties");
				CONFIG.load(fs);
				//Application Url's
				fs=new FileInputStream(System.getProperty("user.dir")+"\\Config\\url.properties");
				URL.load(fs);

			} catch (Exception e) {
				e.printStackTrace();
				return ;
			}
			finally
			{
				try {
					if(fs!=null)
						fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//Using Reflection Concept to get the instance of Extent Manager Class	
			report=ExtentManager.getInstance();
			//Getting Log using getLogger method of Logger class
			APP_LOGS=Logger.getLogger("devpinoyLogger");
			//Load Config Config.properties file
			APP_LOGS.debug("Loding Properties File");	
			//path for hr systems excel file
			xlsReader=new Xls_Reader(System.getProperty("user.dir")+"/src/test/resources/xls/RegressionSuite.xlsx");
		}
	}


	//#################################################################################################################################
	/**
	 * @author Azharuddin Khan
	 * Method name: openBrowser
	 * @param browser
	 * @descrition: This method is used for open browser instance passing browser as parameter
	 *              Browser parameter value will be taken from config.properties file
	 *              Other useful actions are : maximizing window,applying implicit wait,deleting cookies/session
	 *             
	 *              overlappingCheckDisabled(in case of mozilla)
	 *              
	 *              Example: openBrowser(Config.getProperty("Browser"));
	 *              
	 *              
	 */

	public static void openBrowser(String browser,String executionType)
	{
	
		try{
			if(browser.equals("IE")){

				System.setProperty("webdriver.ie.driver",System.getProperty("user.dir")+"\\Drivers\\IEDriverServer.exe"); 
				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setJavascriptEnabled(true);
				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				capabilities.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
				capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
				capabilities.setCapability("ignoreZoomSetting", true);
				capabilities.setCapability("nativeEvents", false);  
				capabilities.setCapability(InternetExplorerDriver.ELEMENT_SCROLL_BEHAVIOR,
						ElementScrollBehavior.BOTTOM);
				capabilities.setJavascriptEnabled(true);
				driver=new InternetExplorerDriver();
				driver.manage().deleteAllCookies();
				APP_LOGS.info(CONFIG.getProperty("Browser")+" Browser Has Been Launched.");
				test.log(LogStatus.INFO,CONFIG.getProperty("Browser")+" Browser Has Been Launched.");

			}
			else if(browser.equals("Chrome")){

				ChromeOptions options = new ChromeOptions();
				options.addArguments("--disable-notifications");
				System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\executables\\chromedriver.exe");
				driver=new ChromeDriver(options);
				driver.manage().deleteAllCookies();
				APP_LOGS.info(CONFIG.getProperty("Browser")+" Browser Has Been Launched.");
				test.log(LogStatus.INFO,"Chrome Browser Has Been Launched.");

			}
		  
			driver.manage().window().maximize();
			driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
		}
		
		catch(Exception e)
		{
			//Write Exception into Extent Report if it fails
			//Write Exception into Log file if it fails
			//APP_LOGS.error("Not able to launch Browser"+e.getMessage());
			test.log(LogStatus.FAIL,e.getMessage());
			test.log(LogStatus.INFO,CONFIG.getProperty("Browser")+" Browser Has Not Been Launched.");
		}
	}	
	
	public static void openBrowser(String browser)
	{
	
		try{
			if(browser.equals("IE")){

				System.setProperty("webdriver.ie.driver",System.getProperty("user.dir")+"\\Drivers\\IEDriverServer.exe"); 
				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setJavascriptEnabled(true);
				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				capabilities.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
				capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
				capabilities.setCapability("ignoreZoomSetting", true);
				capabilities.setCapability("nativeEvents", false);  
				capabilities.setCapability(InternetExplorerDriver.ELEMENT_SCROLL_BEHAVIOR,
						ElementScrollBehavior.BOTTOM);
				capabilities.setJavascriptEnabled(true);
				driver=new InternetExplorerDriver();
				driver.manage().deleteAllCookies();
				APP_LOGS.info(CONFIG.getProperty("Browser")+" Browser Has Been Launched.");
				test.log(LogStatus.INFO,CONFIG.getProperty("Browser")+" Browser Has Been Launched.");

			}
			else if(browser.equals("Chrome")){

				ChromeOptions options = new ChromeOptions();
				options.addArguments("--disable-notifications");
				System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\executables\\chromedriver.exe");
				driver=new ChromeDriver(options);
				driver.manage().deleteAllCookies();
				APP_LOGS.info(CONFIG.getProperty("Browser")+" Browser Has Been Launched.");
				test.log(LogStatus.INFO,"Chrome Browser Has Been Launched.");

			}
		  
			driver.manage().window().maximize();
			driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
		}
		
		catch(Exception e)
		{
			//Write Exception into Extent Report if it fails
			//Write Exception into Log file if it fails
			//APP_LOGS.error("Not able to launch Browser"+e.getMessage());
			test.log(LogStatus.FAIL,e.getMessage());
			test.log(LogStatus.INFO,CONFIG.getProperty("Browser")+" Browser Has Not Been Launched.");
		}
	}	
	//###########################################################################################################################################
	/**
	 * @author Azharuddin khan
	 * Method name:turnOnImplicitWaits
	 * @description:Turning on Implicit wait feature and default maximum  value is 20 seconds to wait for the element on page.
	 * 
	 */
	public static void turnOnImplicitWaits()
	{
		driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
	}
	//##############################################################################################################################################
	/**
	 * @author Azharuddin Khan
	 * Method name: turnturnOnImplicitWaits
	 * type of method: Overloaded method
	 * @param time
	 * @description:This method works for turn on implicit wait while passing time(String) as parameter which will convert 
	 *              this time parameter from string to Long.
	 *              Example:turnturnOnImplicitWaits("30")
	 *Passing time as String value and converting/parsing into long value.
	 */
	public static void turnturnOnImplicitWaits(String time)
	{
		long time_=Long.parseLong(time);
		driver.manage().timeouts().implicitlyWait(time_,TimeUnit.SECONDS);
	}
	//################################################################################################################################################
	/**
	 * @author Azharuddin khan
	 * Method name:turnOffImplicitWaits
	 * @description:Turning off Implicit wait feature and make default time as 0.
	 * 
	 */
	public static void turnOffImplicitWaits()
	{
		driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
	}
	//################################################################################################################################################
	/**
	 * @author Azharuddin Khan
	 * Method name:turnOffImplicitWaits 
	 * @param time
	 * @description:This method works for turn off implicit wait while passing time(String) as parameter which will convert 
	 *              this time parameter from string to Long. 
	 *              Example:turnOffImplicitWaits("0")
	 * Passing "0" as parameter and convert this to long value.             
	 */
	public static void turnOffImplicitWaits(String time)
	{
		long time_=Long.parseLong(time);
		driver.manage().timeouts().implicitlyWait(time_,TimeUnit.SECONDS);
	}

	//##################################################################################################################################################
	/***
	 * @author SESA443020(Azharuddin Khan)
	 * Method Name: launchUrl
	 * @param:env,type
	 * @type: String,String
	 * @Description:This method is used to Launch URL based on parameter envrionement and Type(Home or Work)
	 *
	 */

	public static void launchUrl(String url)
	{

		try{

			driver.navigate().to(url);	
			//driver.get(URL.getProperty(url));
			test.log(LogStatus.INFO,driver.getCurrentUrl()+" Has been launched.");
			//turnOffImplicitWaits();
		}
		catch(Exception ex)
		{
			ex.getMessage();
		}

		finally
		{
			//turnOnImplicitWaits();
		}

	}




	public static void launchUrl(String env,String type)
	{
		String url="";
		url=env+"_"+type;
		System.out.println(url);

		try{
			System.out.println(URL.getProperty(url));
			driver.navigate().to(URL.getProperty(url));	

			test.log(LogStatus.INFO,driver.getCurrentUrl()+" Has been launched.");
			//turnOffImplicitWaits();
			Thread.sleep(10000);
		}
		catch(Exception ex)
		{
			ex.getMessage();
		}

		finally
		{
			//turnOnImplicitWaits();
		}

	}














	//############################################################################################################################################	
	/***
	 * @author SESA443020(Azharuddin Khan)
	 * Method Name: closeBrowser
	 * @param:N/A
	 * @Description:This method is used to close the current focused browser window
	 * 
	 */
	public static void closeBrowser()
	{   
		driver.close();
		APP_LOGS.info("Browser Has been Closed");
		test.log(LogStatus.INFO, "Browser has been closed.");
	}
	//##############################################################################################################################################	
	/***
	 * @author SESA443020(Azharuddin Khan)
	 * Method Name: closeAllBrowser
	 * @param:N/A
	 * @Description:This method is used to close all browser instances
	 * 
	 */
	public static void closeAllBrowser()
	{   
		driver.quit();
		APP_LOGS.info("Session Closed");
		test.log(LogStatus.INFO, "Session Closed");
	}
	//#############################################################################################################################################
	
}
