package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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
public class ExceptionsTests {

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

	@Parameters({ "expectedMessage" })
	@Test(priority = 1)
	public void notVisibleTest(String expectedSuccessMessage) {
		System.out.println("Starting notVisibleTest");

//		Open test page
		String url = "http://the-internet.herokuapp.com/dynamic_loading/1";
		driver.get(url);

//		CLick start button		
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
		startButton.click();

//		Get finish element
		WebElement finishMessage = driver.findElement(By.id("finish"));

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(finishMessage));

		String actualMessage = finishMessage.getText();

//		Verification
		Assert.assertTrue(actualMessage.contains(expectedSuccessMessage),
				"Actual successful message does not contain expected message.\nActual message: " + actualMessage
						+ "\nExpected message: " + expectedSuccessMessage);
	}

	@Parameters({ "expectedMessage" })
	@Test(priority = 2)
	public void timeoutTest(String expectedSuccessMessage) {
		System.out.println("Starting timeoutTest");

//		Open test page
		String url = "http://the-internet.herokuapp.com/dynamic_loading/1";
		driver.get(url);

//		CLick start button		
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
		startButton.click();

//		Get finish element
		WebElement finishMessage = driver.findElement(By.id("finish"));

		WebDriverWait wait = new WebDriverWait(driver, 2);
		try {
			wait.until(ExpectedConditions.visibilityOf(finishMessage));
		} catch (TimeoutException exception) {
			System.out.println("Exception catched" + exception.getMessage());
			sleep(3000);
		}

		String actualMessage = finishMessage.getText();

//		Verification
		Assert.assertTrue(actualMessage.contains(expectedSuccessMessage),
				"Actual successful message does not contain expected message.\nActual message: " + actualMessage
						+ "\nExpected message: " + expectedSuccessMessage);
	}

	@Parameters({ "expectedMessage" })
	@Test(priority = 3)
	public void noSuchElement(String expectedSuccessMessage) {
		System.out.println("Starting notVisibleTest");

//		Open test page
		String url = "http://the-internet.herokuapp.com/dynamic_loading/2";
		driver.get(url);

//		CLick start button		
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
		startButton.click();

//		Get finish element
		WebDriverWait wait = new WebDriverWait(driver, 10);
		Assert.assertTrue(
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("finish"), expectedSuccessMessage)),
				"Couldn't verify expected text 'Hello World!'");
	}

	@Test(priority = 4)
	public void staleElementTest() {
		System.out.println("Starting staleElementTest");

//		Open test page
		String url = "http://the-internet.herokuapp.com/dynamic_controls";
		driver.get(url);

		WebElement checkbox = driver.findElement(By.id("checkbox"));
		WebElement removeButton = driver.findElement(By.xpath("//button[contains(text(), 'Remove')]"));

		WebDriverWait wait = new WebDriverWait(driver, 10);

		removeButton.click();
//		wait.until(ExpectedConditions.invisibilityOf(checkbox));
//		Assert.assertFalse(checkbox.isDisplayed());

//		Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(checkbox)),
//				"Checkbox is still visible, but shouldn't be.");

		Assert.assertTrue(wait.until(ExpectedConditions.stalenessOf(checkbox)),
				"Checkbox is still visible, but shouldn't be.");

		WebElement addButton = driver.findElement(By.xpath("//button[contains(text(), 'Add')]"));
		addButton.click();
		
		checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkbox")));
		Assert.assertTrue(checkbox.isDisplayed(), "Checkbox is not invisible, but shouldn be.");
	}

	private void sleep(long n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		// close browser
		driver.quit();
	}

}
