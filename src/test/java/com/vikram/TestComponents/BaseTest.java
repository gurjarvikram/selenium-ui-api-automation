package com.vikram.TestComponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vikram.pageobjects.Landingpage;

public class BaseTest {

	public WebDriver driver;
	public Landingpage landingPage;

	public WebDriver initializeDriver() throws IOException {

		// properties class
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "//src//main//java//com//vikram//resources//GlobalData.properties");
		prop.load(fis);
		
		//This condition handle both maven headless browser execution if it get otherwise by default set browser in the global property file.
		String browserName = System.getProperty("browser")!=null ? System.getProperty("browser") : prop.getProperty("browser");
		
		
		
		//String browserName = prop.getProperty("browser");

		if (browserName.contains("chrome")) {
			// chrome
			ChromeOptions options = new ChromeOptions();			
			if(browserName.contains("headless")) 
			{
				options.addArguments("headless");
			}			
			driver = new ChromeDriver(options);
			driver.manage().window().setSize(new Dimension(1440,900));  //full screen

		} else if (browserName.equalsIgnoreCase("firefox")) {
			// firefox
			driver = new FirefoxDriver();

		} else if (browserName.equalsIgnoreCase("edge")) {
			// edge
			driver = new EdgeDriver();
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		return driver;

	}

	// get the test data set from the purchaseorder json file
	public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {

		// Read JSON content from the file and convert it to a String
		String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);

		// Convert JSON String into a List of HashMaps using Jackson ObjectMapper
		ObjectMapper mapper = new ObjectMapper();
		List<HashMap<String, String>> data = mapper.readValue(jsonContent,
				new TypeReference<List<HashMap<String, String>>>() {
				});

		// Return the parsed data
		return data;

	}

//	public String getScreenshot(String testCaseName, WebDriver driver) throws IOException 
//	{
//		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//		File file = new File(System.getProperty("user.dir"+"//reports"+ testCaseName + ".png"));
//		FileUtils.copyFile(src, file);		
//		return System.getProperty("user.dir"+"//reports"+ testCaseName + ".png");
//		
//	}

	public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
		// Take a screenshot and store it in a temporary file
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		// Define the destination path for the screenshot
		String destPath = System.getProperty("user.dir") + "//reports//" + testCaseName + ".png";
		File file = new File(destPath);

		// Ensure the destination directory exists
		file.getParentFile().mkdirs();

		// Copy the screenshot to the destination path
		FileUtils.copyFile(src, file);

		return destPath; // Return the file path of the screenshot
	}

	@BeforeMethod(alwaysRun = true)
	public Landingpage lanuchApplication() throws IOException {

		driver = initializeDriver();
		landingPage = new Landingpage(driver);
		landingPage.goTo();
		return landingPage;

	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		driver.quit();
	}

}
