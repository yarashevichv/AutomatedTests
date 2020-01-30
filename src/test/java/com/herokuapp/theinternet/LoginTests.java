package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @author veron
 *
 */
public class LoginTests {

	private WebDriver driver;

	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	private void setUp(@Optional String browser) {
		switch (browser) {
		case "chrome":
//			Create driver
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();
			break;
		case "firefox":
//			Create driver
			System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
			driver = new FirefoxDriver();
			break;

		default:
			System.out.println("Don't khow how to run " + browser + ", starting chrome instead");
//			Create driver
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();
			break;
		}

//		maximize browser window
		driver.manage().window().maximize();
		
//		implicit wait
//		driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
	}

	@Parameters({ "username", "password", "expectedMessage" })
	@Test(priority = 1, groups = { "positiveTests", "smokeTests" })
	public void positiveLoginTest(String username, String password, String expectedSuccessMessage) {
		System.out.println("Starting loginTest");

//		open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("Page is opened");

//		enter username
		WebElement usernameElement = driver.findElement(By.id("username"));
		usernameElement.sendKeys(username);

//		enter password
		WebElement passwordElement = driver.findElement(By.name("password"));
		passwordElement.sendKeys(password);
		
		WebDriverWait wait = new WebDriverWait(driver, 4);

//		click login button
		WebElement logInButton = driver.findElement(By.tagName("button"));
		wait.until(ExpectedConditions.elementToBeClickable(logInButton));
		logInButton.click();

//		verifications:
//			new url
		String expectedUrl = "http://the-internet.herokuapp.com/secure";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not the same as expected.");

//			logout button is visible
		WebElement logOutButton = driver.findElement(By.xpath("//a[@class='button secondary radius']"));
		Assert.assertTrue(logOutButton.isDisplayed(), "Log Out button is not visible.");

//			successful login message
		WebElement successMessage = driver.findElement(By.cssSelector("#flash"));
		String actualMessage = successMessage.getText();
//		Assert.assertEquals(actualMessage, expectedMessage, "Actual successful message is not the same as expected.");
		Assert.assertTrue(actualMessage.contains(expectedSuccessMessage),
				"Actual successful message does not contain expected message.\nActual message: " + actualMessage
						+ "\nExpected message: " + expectedSuccessMessage);
	}

	@Parameters({ "username", "password", "expectedMessage" })
	@Test(priority = 2, groups = { "negativeTests", "smokeTests" })
	public void negativeLoginTest(String username, String password, String expectedSuccessMessage) {
		System.out.println("Starting negativeLoginTest with username: " + username + ", password: " + password);

//		open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("Page is opened");

//		enter username
		WebElement usernameElement = driver.findElement(By.id("username"));
		usernameElement.sendKeys(username);

//		enter password
		WebElement passwordElement = driver.findElement(By.name("password"));
		passwordElement.sendKeys(password);

//		click login button
		WebElement logInButton = driver.findElement(By.tagName("button"));
		logInButton.click();

//		verifications:
//		url
		String expectedUrl = "http://the-internet.herokuapp.com/login";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not the same as expected.");

//			successful login message
		WebElement errorMessage = driver.findElement(By.xpath("//div[@id='flash']"));
		String actualMessage = errorMessage.getText();
		Assert.assertTrue(actualMessage.contains(expectedSuccessMessage),
				"Actual successful message does not contain expected message.\nActual message: " + actualMessage
						+ "\nExpected message: " + expectedSuccessMessage);
	}

//	private void sleep(long n) {
//		try {
//			Thread.sleep(n);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		// close browser
		driver.quit();
	}

}
