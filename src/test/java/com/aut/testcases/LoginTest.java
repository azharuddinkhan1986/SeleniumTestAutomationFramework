package com.aut.testcases;
import java.util.Hashtable;

import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.auk.base.Base;
import com.auk.utilities.TestUtil;
import com.aut.pages.LoginPage;
import com.relevantcodes.extentreports.LogStatus;

public class LoginTest extends Base {
	
	//Runmode one dimensional array for testdata set .
	String runmodes[] = null;
	// intial counter is negative
	static int count = -1;
	LoginPage loginPage;
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(xlsReader, this.getClass().getSimpleName())) {
			APP_LOGS.debug("Skippped");
			test = report.startTest(this.getClass().getSimpleName());
			test.log(LogStatus.SKIP, this.getClass().getSimpleName() + " Skipped.");
			skip = true;
			reportTestResult();
			throw new SkipException("Test case skipped.");
		}
		// load the runmodes of the tests
		runmodes = TestUtil.getDataSetRunmodes(xlsReader, this.getClass().getSimpleName());
		
	}

	@Test(dataProvider = "getTestData")
	public void AddPerson(Hashtable<String, String> data) throws Exception {
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			test = report.startTest(this.getClass().getSimpleName());
			test.log(LogStatus.SKIP, this.getClass().getSimpleName() + " Skipped.");
			skip = true;
			reportTestResult();
			throw new SkipException("Runmode for test set data set to no " + count);
		}
		// 1. Start Test
		test = report.startTest("Add_Person");
		/// 2.Open Browser
		openBrowser(CONFIG.getProperty("Browser"));
		// 3.Launch QA URL
		launchUrl(CONFIG.getProperty("BASE_URL"));
		loginPage = PageFactory.initElements(driver, LoginPage.class);
		// 4.Login To Application
		loginPage.LogintoApplication(data.get("USERNAME"),data.get("PASSWORD"));
		
	}


	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getTestdataUsingTable(xlsReader, this.getClass().getSimpleName());
	}

	@AfterMethod
	public void reportDataSetResult() {
		if (driver != null) {
			//closeBrowser();
			closeAllBrowser();
		}
		if (skip)
			TestUtil.reportDataSetResult(xlsReader, this.getClass().getSimpleName(), count + 2, "SKIP");
		else if (fail) {
			isTestPass = false;
			TestUtil.reportDataSetResult(xlsReader, this.getClass().getSimpleName(), count + 2, "FAIL");
		} else
			TestUtil.reportDataSetResult(xlsReader, this.getClass().getSimpleName(), count + 2, "PASS");
		skip = false;
		fail = false;
		report.endTest(test);
		report.flush();
	}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass) {
			TestUtil.reportDataSetResult(xlsReader, "Test Cases",
					TestUtil.getRowNum(xlsReader, this.getClass().getSimpleName()), "PASS");
		} else {
			TestUtil.reportDataSetResult(xlsReader, "Test Cases",
					TestUtil.getRowNum(xlsReader, this.getClass().getSimpleName()), "FAIL");
		}
		if (skip && isTestPass) {
			TestUtil.reportDataSetResult(xlsReader, "Test Cases",
					TestUtil.getRowNum(xlsReader, this.getClass().getSimpleName()), "SKIP");
		}
	}

}
