
package com.auk.utilities;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;
import com.auk.base.Base;
import com.relevantcodes.extentreports.LogStatus;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class CommonUtility extends Base{



	public static void acceptAlert()
	{
		try{
			Alert alert=driver.switchTo().alert();
			alert.accept();
		}
		catch(Exception ex)
		{
			test.log(LogStatus.FAIL, ex.getMessage());
			System.out.println(ex.getMessage());
		}
	}

	/*public static void dismissAlert()
	{
		//driver.switchTo().alert().dismiss();	
		Actions act=new Actions(driver);
		act().sendKeys(Keys.ESCAPE).build().perform();
	}
*/


	public static void switchToWindowOrTab()
	{
		//get windowhandles
		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
		//switch to new opened window and get title of the page-it should be login page
		driver.switchTo().window(tabs2.get(1));
	}

	public static void TabHandles() {
		// String currentWindowHandle = driver.getWindowHandle();
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.TAB);
		/* //Get the list of all window handles
	    ArrayList<String> windowHandles = new ArrayList<String>(driver.getWindowHandles());

	    for (String window:windowHandles){

	        //if it contains the current window we want to eliminate that from switchTo();
	        if (window != currentWindowHandle){
	            //Now switchTo new Tab.
	            driver.switchTo().window(window);
	            //Do whatever you want to do here.

	            //Close the newly opened tab
	            driver.close();
	        }
	    }*/
	}

	// highLightElement Method
	public static void highLightElement(WebDriver driver,WebElement element)
	{
		JavascriptExecutor js=(JavascriptExecutor)driver; 

		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);

		try 
		{
			Thread.sleep(2000);
		} 
		catch (InterruptedException e) {

			System.out.println(e.getMessage());
		} 

		js.executeScript("arguments[0].setAttribute('style','border: solid 2px white');", element); 

	}


	public static void selectDropdown(WebElement element,String data,String desc)
	{
		try{
			//CommonUtility.highLightElement(driver, element);
			Select DropDown=new Select(element);
			DropDown.selectByVisibleText(data);
			//DropDown.selectByValue(data);
			test.log(LogStatus.INFO,desc+" as "+data);
		}

		catch(Exception ex)
		{
			test.log(LogStatus.FAIL, ex.getMessage());
			APP_LOGS.debug(ex.getMessage());
		}
	}

	public static void selectDropdownWithoutHighlight(WebElement element,String Data,String desc)
	{
		try{
			Select DropDown=new Select(element);
			DropDown.selectByVisibleText(Data);
			test.log(LogStatus.INFO,desc+" as "+Data);
		}

		catch(Exception ex)
		{
			test.log(LogStatus.ERROR, ex.getMessage());
			APP_LOGS.debug(ex.getMessage());
		}
	}





	//Select from list dropdown
	public void selectFromList(String option) {
		// Open the dropdown so the options are visible
		driver.findElement(By.className("dropdown-menu")).click();
		// Get all of the options
		List<WebElement> options = driver.findElements(By.xpath("//ul[@class='dropdown-menu']/li"));
		// Loop through the options and select the one that matches
		for (WebElement opt : options) {
			if (opt.getText().equals(option)) {
				opt.click();
				return;
			}
		}
		throw new NoSuchElementException("Can't find " + option + " in dropdown");
	}


	public static void navigateTo(String Url)
	{
		try{
			driver.navigate().to(Url);
			test.log(LogStatus.INFO,"Navigate to Url "+Url);
			String x=addScreenshot();
			test.log(LogStatus.PASS,test.addScreenCapture(x));
		}
		catch(Exception ex)
		{
			test.log(LogStatus.INFO,ex.getMessage());
		}
	}

	public static void clearText(WebElement element)
	{
		try{
			element.clear();
		}
		catch(Exception ex)
		{
			test.log(LogStatus.INFO,ex.getMessage());
			String x=addScreenshot();
			test.log(LogStatus.FAIL,test.addScreenCapture(x));
		}

	}

	public static void verifyTitle(String Title )
	{
		try{
			String actual=driver.getTitle();
			Assert.assertEquals(Title,actual);
			test.log(LogStatus.PASS, "Title of the page is "+actual);
		}
		catch(Throwable t)
		{
			//Add Error report to Extent Report
			test.log(LogStatus.FAIL, "Title of the page is not correct.");
			String x=addScreenshot();
			test.log(LogStatus.FAIL,"Image-1",test.addScreenCapture(x));
			ErrorUtil.addVerificationFailure(t);
			//report to xls file
			//this return will make test case stops
			//return;
		}

	}



	public static void selectListBox(WebElement element,String data,String desc)
	{
		try{
			CommonUtility.highLightElement(driver, element);
			Select listBox=new Select(element);	
			if(data.contains(",")){
				ArrayList<String> items = new  ArrayList<String>(Arrays.asList(data.split(",")));
				for (String temp : items) {
					listBox.selectByVisibleText(temp);
				}
			}
			else
				listBox.selectByVisibleText(data);		
			test.log(LogStatus.INFO,desc+" as "+data);
		}

		catch(Exception ex)
		{
			test.log(LogStatus.ERROR, ex.getMessage());
			APP_LOGS.debug(ex.getMessage());
		}
	}



	public static String getListBoxValues(WebElement element) {
		String listString=" ";
		Select select = new Select(element);
		ArrayList<String>values=new ArrayList<String>();
		List<WebElement> allOptions=select.getOptions();
		for (int i=0; i<allOptions.size();i++){
			//System.out.println("ENter loop");
			values.add(allOptions.get(i).getText());
			//System.out.println("ListBox value: "+values);
		}
		//convert List to csv
		if(values.size()>1){
			listString = String.join(", ", values);
		}
		else if (values.size()==1){
			listString = String.valueOf(values);
		}
		else if(values.size()==0){
			listString=" ";
		}
		return listString;  
	}

	public void waitBeforeAction(long time) throws InterruptedException
	{
		Thread.sleep(time);
	}


	//Enter Input in Textbox,TextArea
	public static void input(WebElement element,String Data,String desc)
	{
		try{
			//CommonUtility.highLightElement(driver, element);	
			wait=new WebDriverWait(driver,30);
			wait.until(ExpectedConditions.visibilityOf(element));
			element.sendKeys(Data);
			byte bytes[] = Data.getBytes("UTF-8"); 
			String value = new String(bytes, "UTF-8");
			System.out.println(value);
			test.log(LogStatus.INFO, desc + value);
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL,e.getMessage());
			String screenshot=fullPageScreenshot("Screenshot1");
			test.log(LogStatus.FAIL, test.addScreenCapture(screenshot));
		}

	}
	
	//Enter Input in Textbox,TextArea
		public static void input1(WebElement element,String Data,String desc)
		{
			
			
			try{
				element.sendKeys(Data);
				test.log(LogStatus.INFO, desc + Data);
			}catch(NoSuchElementException ex)
			{
				test.log(LogStatus.INFO,ex.getMessage());
				String x=addScreenshot();
				test.log(LogStatus.FAIL,test.addScreenCapture(x));
				throw(ex);
			}
			catch(StaleElementReferenceException ex)
			{
				test.log(LogStatus.INFO,ex.getMessage());
				String x=addScreenshot();
				test.log(LogStatus.FAIL,test.addScreenCapture(x));
				throw(ex);
			}
			catch(Exception ex)
			{
				test.log(LogStatus.INFO,ex.getMessage());
				String x=addScreenshot();
				test.log(LogStatus.FAIL,test.addScreenCapture(x));
				//throw(ex);
			}
			
		}

	public static void inputWithoutHiglight(WebElement element,String Data,String desc)
	{
		try{	
			element.sendKeys(Data);
			byte bytes[] = Data.getBytes("UTF-8"); 
			String value = new String(bytes, "UTF-8");
			System.out.println(value);
			test.log(LogStatus.INFO,desc+ "as "+value);
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL,e.getMessage());
		}

	}
	//Click on link,button
	public static void click(WebElement element,String desc)
	{  
		try{
			wait=new WebDriverWait(driver, 120);
			wait.until(ExpectedConditions.visibilityOf(element));
			element.click();
			test.log(LogStatus.INFO,desc);
		}
		catch(Exception ex)
		{
			test.log(LogStatus.FAIL,ex.getMessage());
			ex.printStackTrace();
			fail=true;
		}
	}

	//clicking using javascript executor
	public static void javascripExecutor(WebElement element,String desc)
	{  
		try{
			//CommonUtility.highLightElement(driver, element);
			wait=new WebDriverWait(driver, 120);
			wait.until(ExpectedConditions.visibilityOf(element));
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].click();", element);
			test.log(LogStatus.INFO,desc);
		}
		catch(Exception ex)
		{
			test.log(LogStatus.FAIL,ex.getMessage());
			ex.printStackTrace();
			fail=true;
		}
	}

	//Click on link,button
	public void doubleClick(WebElement element,String desc)
	{  
		try{
			Actions act=new Actions(driver);
			act.moveToElement(element).doubleClick().build().perform();
			test.log(LogStatus.INFO,desc);
		}
		catch(Exception ex)
		{
			test.log(LogStatus.FAIL,ex.getMessage());
			fail=true;
		}
	}

	//Click on link,button
	public static void highlightAndClick(WebElement element,String desc)
	{  
		try{
			CommonUtility.highLightElement(driver, element);	
			element.click();
			test.log(LogStatus.INFO,desc);
		}
		catch(Exception ex)
		{
			test.log(LogStatus.FAIL,ex.getMessage());
			fail=true;
		}
	}





	public static String getValue(WebElement element)
	{  String value="";
	try{
		value=element.getAttribute("value");
	}
	catch(Exception ex)
	{
		test.log(LogStatus.INFO,ex.getMessage());

	}
	return value;

	}


	public static void tabAndSpaceOnElement(WebElement element,String desc)
	{
		try{
			//CommonUtility.highLightElement(driver, element);	
			element.sendKeys(Keys.TAB);
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("document.getElementById('id').focus();");
			element.sendKeys(Keys.SPACE);
			test.log(LogStatus.INFO,desc);
		}
		catch(Exception ex)
		{
			test.log(LogStatus.FAIL,ex.getMessage());
			APP_LOGS.debug(ex.getMessage());
            fail=true;
		}
	}


	public static String getDefaultvalue(WebElement element) {
		Select select = new Select(element);
		WebElement value = select.getFirstSelectedOption();
		return value.getText();  
	}

	public static String addScreen()
	{  String encodedBase64 = null;
	FileInputStream fileInputStreamReader = null;
	try {
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension d = tool.getScreenSize();
		Rectangle rect = new Rectangle(d);
		Robot robot = new Robot();
		Thread.sleep(2000);
		// File f = new File("screenshot.png");
		File f=new File(System.getProperty("user.dir")+"\\Screenshots\\screenshot.png");
		BufferedImage img = robot.createScreenCapture(rect);
		ImageIO.write(img,"png",f);
		fileInputStreamReader = new FileInputStream(f);
		byte[] bytes = new byte[(int)f.length()];
		fileInputStreamReader.read(bytes);
		encodedBase64 = new String(Base64.encodeBase64(bytes));
		tool.beep();
	} catch(Exception e){
		e.printStackTrace();
		test.log(LogStatus.FAIL,e.getMessage());
		fail=true;
	}

	return "data:image/png;base64,"+encodedBase64;

	}


	//Add screenshot method is used for getting screenshot using base64

	public static String addScreenshot() {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String encodedBase64 = null;
		FileInputStream fileInputStreamReader = null;
		try {
			fileInputStreamReader = new FileInputStream(scrFile);
			byte[] bytes = new byte[(int)scrFile.length()];
			fileInputStreamReader.read(bytes);
			encodedBase64 = new String(Base64.encodeBase64(bytes));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail=true;
			test.log(LogStatus.FAIL, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail=true;
			test.log(LogStatus.FAIL, e.getMessage());
		}
		return "data:image/png;base64,"+encodedBase64;
	}

	public static File getScreenshotusingAshot()
	{   
		Screenshot fpScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
		File file=new File("C:\\Users\\SESA443020\\workspace\\HR_SYSTEM\\FullScreen\\FullPageScreenshot.png");
		try {
			
			ImageIO.write(fpScreenshot.getImage(),"PNG",file);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail=true;
			test.log(LogStatus.FAIL, e.getMessage());
		}
		return file;
	}
	
	
	public static File getScreenshotFilePathUsingShutterbug(String name)
	{  
		
		Shutterbug.shootPage(driver, ScrollStrategy.WHOLE_PAGE).withName(name).save(System.getProperty("user.dir")+"\\FullScreen\\");
		File f=new File(System.getProperty("user.dir")+"\\FullScreen\\"+name+".png");
		return f;

	}
	
	
	
	public static String fullPageScreenshot(String name) {
		//File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String encodedBase64 = null;
		FileInputStream fileInputStreamReader = null;
		try {
			fileInputStreamReader = new FileInputStream(getScreenshotFilePathUsingShutterbug(name));
			byte[] bytes = new byte[(int)getScreenshotFilePathUsingShutterbug(name).length()];
			fileInputStreamReader.read(bytes);
			encodedBase64 = new String(Base64.encodeBase64(bytes));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "data:image/png;base64,"+encodedBase64;
	}

	/***
	 * Method Name: IsWebElementDisplayed
	 * 
	 * @param: WebElement
	 *             and Description
	 * @return: boolean
	 * @Description:If the WebElement passed to the method is displayed on
	 *                 Webpage, the method will return true and the description
	 *                 passed to the method will be captured in the report. If
	 *                 the WebElement is not displayed on page, the method will
	 *                 return false.
	 * @author SESA459145(Shyni Prasanna)
	 */
	/*public static boolean isWebElementDisplayed(WebElement element, String description) {
		try {
			
			if (retryingFindelement(element)) {
				test.log(LogStatus.PASS, description);
				String x = addScreenshot();
				test.log(LogStatus.PASS, test.addScreenCapture(x));
			}
		}
		catch (Exception ex) {
			String x = addScreenshot();
			test.log(LogStatus.FAIL, test.addScreenCapture(x));
			test.log(LogStatus.FAIL, "Expected element is not displayed");
			test.log(LogStatus.FAIL, ex.getMessage());
			return false;
		}
		return true;
	}*/
	
	public static boolean isWebElementDisplayed(WebElement element) {
		try{
		new WebDriverWait(driver, 1).ignoring(StaleElementReferenceException.class)
		.until(ExpectedConditions.visibilityOf(element));
		 String screenshotName=addScreenshot();
		test.log(LogStatus.PASS,test.addScreenCapture(screenshotName));
		
	 }catch(TimeoutException ex)
		{
		 String x=addScreenshot();
			test.log(LogStatus.FAIL,test.addScreenCapture(x));
			test.log(LogStatus.FAIL,ex.getMessage());
			test.log(LogStatus.PASS,"element is not displayed.");
			return false;
		}
		
		return true;
	}

	public static void verifyLandingPage(WebElement element, String message)
	{
		try{
			if(element.isDisplayed())
			{
				String x=addScreenshot();
				test.log(LogStatus.PASS,test.addScreenCapture(x));
				test.log(LogStatus.PASS,message);
			}
		}
		catch(Exception ex)
		{
			String x=addScreenshot();
			test.log(LogStatus.FAIL,test.addScreenCapture(x));
			test.log(LogStatus.FAIL,"Landing page is not displayed");
			fail=true;

		}
	}

	public static void getErrorMessage(WebElement element)
	{
		try{
			test.log(LogStatus.PASS,element.getText());
		}
		catch(Exception ex)
		{
			String x=addScreenshot();
			test.log(LogStatus.FAIL,test.addScreenCapture(x));
			test.log(LogStatus.FAIL,"Error Message is not displayed");
		}
	}


	public static void getErrorMessage(WebElement element,String text)
	{
		try{
			if(element.isDisplayed()){
				test.log(LogStatus.PASS,text);
				String x=addScreenshot();
				test.log(LogStatus.PASS,test.addScreenCapture(x));
			}
		}
		catch(Exception ex)
		{
			String x=addScreenshot();
			test.log(LogStatus.FAIL,test.addScreenCapture(x));
		}
	}

	public static void scrollToBottom(int time)
	{
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		for(int i=0;i<time;i++){
			jse.executeScript("window.scrollBy(0,250)", "");
		}
	}

	public static void scrollToUp(int time)
	{
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		for(int i=0;i<time;i++){
			jse.executeScript("window.scrollBy(0,-250)", "");
		}
	}

	public static void scrollUptoElementIsVisible(WebElement element)
	{
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].scrollIntoView(true);",element);
	}

	public static void scrollUptoElementsIsVisible(List<WebElement> element)
	{
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].scrollIntoView(true);",element);
	}




	public static void selectByValue(String xpath, String value)
	{
		driver.findElement(By.xpath(xpath+"/option[.='"+value+"']")).click();
	}

	public static String getLinkAddress(WebElement locator){
		String linkAddress=locator.getAttribute("href");
		return linkAddress;
	} 


	public static void moveToChildWindow() throws Exception{
		for(String winHandle : driver.getWindowHandles())
		{

			driver.switchTo().window(winHandle);

			System.out.println(winHandle);

			driver.manage().window().maximize();
			Thread.sleep(7000);
		}

	}




	public static void moveToChildWindow1() throws Exception{
		try{
			Set <String> handles =driver.getWindowHandles();
			Thread.sleep(4000);
			Iterator<String> it = handles.iterator();
			while (it.hasNext()){
				String parent = it.next();
				System.out.println(parent);
				String newwin = it.next();
				System.out.println(newwin);
				Thread.sleep(5000);
				driver.switchTo().window(newwin);
				driver.manage().window().maximize();
				Thread.sleep(7000);
			} 
		}

		catch(Exception ex){
			test.log(LogStatus.FAIL,ex.getMessage());
			System.out.println(ex.getMessage());
		}
	}

	public static void switchToFrame(WebElement ele){
		driver.switchTo().frame(ele);
	}


	public static boolean isCheckboxChecked(WebElement element)
	{
		boolean result = false;
		if(element.isDisplayed())
		{
			return result;
		}
		else
			return false;
	}

	/*public boolean isCheckboxChecked(WebElement element)
		{
		    boolean result;
		    if(element.isDisplayed())
		    {
		     result=element.isSelected();
		     return result;
		    }
		  else
		   return false;
		}*/


	public static void waitForLoad() {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>()
		{
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(pageLoadCondition);
	}


	public static void waitForPageLoaded2() {
		ExpectedCondition<Boolean> expectation = new
				ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
			}
		};
		try {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(expectation);
		} catch (Throwable error) {
			Assert.fail("Timeout waiting for Page Load Request to complete.");
		}
	}


	public static void waitForNewWindowAndSwitchToIt() throws InterruptedException {

		String cHandle = driver.getWindowHandle();
		String newWindowHandle = null;
		Set<String> allWindowHandles = driver.getWindowHandles();

		//Wait for 20 seconds for the new window and throw exception if not found
		for (int i = 0; i < 20; i++) {
			if (allWindowHandles.size() > 1) {
				for (String allHandlers : allWindowHandles) {
					if (!allHandlers.equals(cHandle))
						newWindowHandle = allHandlers;
				}
				driver.switchTo().window(newWindowHandle);
				driver.manage().window().maximize();
				break;
			} else {
				Thread.sleep(1000);
			}
		}

		if (cHandle == newWindowHandle) {

			throw new RuntimeException("Time out - No window found");
		}
	}

	public static void clickOnHeaderLink(List<WebElement> list,String headerLinkName)
	{


		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).getText().equalsIgnoreCase(headerLinkName))
			{   
				list.get(i).click();
				test.log(LogStatus.INFO,"user naviagtes to "+headerLinkName+" Page");
				break;
			}
		}

	}

	public static boolean isElementDisplayedAndClickable(WebElement element)
	{   try{
		wait=new WebDriverWait(driver,120);
		wait.until(ExpectedConditions.visibilityOf(element));
		wait.until(ExpectedConditions.elementToBeClickable(element));
		if(!element.isDisplayed())
		{
			return false;
		}
		else
		{    
		return true;
		}
	}catch(NoSuchElementException ex)
	{
		test.log(LogStatus.FAIL, ex.getMessage());
		fail=true;
		return false;
	}
	}

	public static String getText(WebElement element) {
		// TODO Auto-generated method stub
		String message="";
		try{
			message=element.getText();
			test.log(LogStatus.INFO, message);
		}catch(NoSuchElementException ex)
		{
			test.log(LogStatus.FAIL, ex.getMessage());
		}
		return message;
	}

	public static String getCurrentWindow()
	{
		return driver.getWindowHandle();
	}

	public static void openNewTab(){

		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.open()");

	}

	public static void switchToAnotherTab(String currentWindow)
	{
		//Switch to Another Tab
		Set<String> allWindows=driver.getWindowHandles();
		System.out.println(allWindows.size());
		for(String window:allWindows)
		{
			if(!window.equals(currentWindow))
			{
				driver.switchTo().window(window);
				break;
			}

		}
	}


	public static void switchToPreviuosTab(String currentWindow)
	{    try{
		driver.switchTo().window(currentWindow);
		test.log(LogStatus.PASS,"Switched to Previous Window");	
	}catch(Exception ex){
		test.log(LogStatus.FAIL,ex.getMessage());	 
	}

	}

	public static void switchToDefaultContent() {
		// TODO Auto-generated method stub
		driver.switchTo().defaultContent();
	}

	public static void mouseHover(WebElement ele,String menuName) {
		// TODO Auto-generated method stub
		
		
		  try{
			  Actions act = new Actions(driver);
				act.moveToElement(ele).click().build().perform();
				test.log(LogStatus.PASS,"User Selected "+menuName);	
			}catch(Exception ex){
				test.log(LogStatus.FAIL,ex.getMessage());	 
			}
	}
	
	
	public static void mouseHover(List<WebElement> lists,String menuName) {
		// TODO Auto-generated method stub
		Actions act = new Actions(driver);
		
		  try{
			  for(int i=0;i<lists.size();i++)
			  {
				  if(lists.get(i).getText().equals(menuName))
				  {
					  act.moveToElement(lists.get(i)).build().perform();
						test.log(LogStatus.PASS,"User Selected "+menuName);	
						break;
				  }
			  }
				
			}catch(Exception ex){
				test.log(LogStatus.FAIL,ex.getMessage());	 
			}
	}
	
	
	
	public static String getMonthNumber(String monthName) throws ParseException
	{
		SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM");
		Calendar cal = Calendar.getInstance();
		cal.setTime(inputFormat.parse(monthName));
		SimpleDateFormat outputFormat = new SimpleDateFormat("MM"); // 01-12
		String monthNumber = outputFormat.format(cal.getTime());
		if(monthNumber.startsWith("1"))
		{
			System.out.println("Not Required.");
		}else{
		monthNumber=monthNumber.substring(1);
		}
		System.out.println(monthNumber);
		return monthNumber;
	}
	
	public static void main(String[] args) {
		try {
			CommonUtility.getMonthNumber("August");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String  getLastTwoCharcatersFromString(String text)
	{
		String value="";
		if(text.length()<=2)
			return text;
		else{
			char[] ch=text.toCharArray();
			int length=ch.length;
			int countStarts=length-2;
			for(int i=countStarts;i<ch.length;i++)
			{
				value+=ch[i];
				
				
			}
		}
		return value;
	}

	public static void waitForelementVisible(WebElement element) {
		try{
		wait=new WebDriverWait(driver,120); 
		wait.until(ExpectedConditions.visibilityOf(element));
		}catch(TimeoutException ex)
		{
			test.log(LogStatus.INFO,ex.getMessage());
			String x=addScreenshot();
			test.log(LogStatus.FAIL,test.addScreenCapture(x));
			throw(ex);
			
		}
		catch(Exception ex)
		{
			test.log(LogStatus.INFO,ex.getMessage());
			String x=addScreenshot();
			test.log(LogStatus.FAIL,test.addScreenCapture(x));
			//throw(ex);
			
		}
	}	
	
	public static void selectOptionsFromList(List<WebElement> lists,String option)
	{
		
		for(int i=0;i<lists.size();i++)
		{
		  if(lists.get(i).getText().equals(option))
		  {
			  click(lists.get(i),"User Seleced Option as "+option);
			  break;
		  }
			
		}
		
	}
	
	
	
	
	public static void checkPageIsReady() {

		  JavascriptExecutor js = (JavascriptExecutor)driver;


		  //Initially bellow given if condition will check ready state of page.
		  if (js.executeScript("return document.readyState").toString().equals("complete")){ 
		   System.out.println("Page Is loaded.");
		   return; 
		  } 

		  //This loop will rotate for 25 times to check If page Is ready after every 1 second.
		  //You can replace your value with 25 If you wants to Increase or decrease wait time.
		  for (int i=0; i<25; i++){ 
		   try {
		    Thread.sleep(1000);
		    }catch (InterruptedException e) {} 
		   //To check page ready state.
		   if (js.executeScript("return document.readyState").toString().equals("complete")){ 
		    break; 
		   }   
		  }
		 }
	
	public static void switchToAnotherTab() throws InterruptedException {
		try {
			// Switch to Another Tab
			String currentWindow = driver.getWindowHandle();
			Thread.sleep(4000);
			Set<String> allWindows = driver.getWindowHandles();
			System.out.println("number of windows: " + allWindows.size());
			for (String window : allWindows) {
				if (!window.equals(currentWindow)) {
					driver.switchTo().window(window);
					driver.manage().window().maximize();
					break;
				}

			}
		} catch (Exception e) {
			// TODO: handle exception

		}
	}
	
	public static void waitForPageLoaded() {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		try {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(expectation);
		} catch (Throwable error) {
			Assert.fail("Timeout waiting for Page Load Request to complete.");
		}
	}
	
	public static void waitForPageLoad() {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(pageLoadCondition);
	}
	
	// clicking using javascript executor
		public static void javascriptExecutorclick(WebElement element, String desc) {
			try {
				// CommonUtility.highLightElement(driver, element);
				// wait=new WebDriverWait(driver, 120);
				// wait.until(ExpectedConditions.visibilityOf(element));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", element);
				test.log(LogStatus.INFO, desc);
			} catch (Exception ex) {
				test.log(LogStatus.FAIL, ex.getMessage());
				ex.printStackTrace();
				fail = true;
			}
		}
}