package com.aut.pages;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.auk.base.Base;
import com.auk.utilities.CommonUtility;

public class LoginPage extends Base{


	@FindBy(id="userid")
	private WebElement username;

	@FindBy(id="password")
	private WebElement password;

	@FindBy(id="btnActive")
	private WebElement signin;

	//Enter username 
	public  void Enter_username(String uname)
	{ 
		Assert.assertEquals(true, CommonUtility.isWebElementDisplayed(username));
		CommonUtility.input1(username, uname, "Enter username");
	}
	//Enter password
	public  void Enter_password(String pwd)
	{
		CommonUtility.input1(password,pwd, "Enter Password");	 
	}

	//click on Signin
	public void Clickon_Signin()
	{
		wait=new WebDriverWait(driver,120); 
		wait.until(ExpectedConditions.elementToBeClickable(signin));
		CommonUtility.click(signin, "Click on Signin");		
	}

	//Loginto application	
	public void LogintoApplication(String uname,String pwd)
	{
		this.Enter_username(uname);
		this.Enter_password(pwd);
		this.Clickon_Signin();
    }
	
	@FindBy(xpath="(//table/tbody/tr/td/a/table/tbody/tr/td/div/div/div/a)[2]")
	private WebElement profileIcon;
	
	@FindBy(xpath="//a[text()='Sign Out']")
	private WebElement signoutLink;
	
	@FindBy(id="Confirm")
	private WebElement confirmButton;
	
	public void logout() {
		// TODO Auto-generated method stub
		//click on profile Icon 
		CommonUtility.click(profileIcon, "Click on Profile Icon");
		//click on signout link
		CommonUtility.click(signoutLink, "User Clicked on Sign out Link");
		//click on confirm button
		wait=new WebDriverWait(driver, 180);
		wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
		CommonUtility.click(confirmButton, "User Clicked on Confirm Button.");
		
	}
	
	public boolean verifyLoginPageDisplayed()
	{
		return CommonUtility.isWebElementDisplayed(username);
	}

}
