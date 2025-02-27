package com.vikram.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.vikram.TestComponents.BaseTest;
import com.vikram.pageobjects.CartPage;
import com.vikram.pageobjects.CheckoutPage;
import com.vikram.pageobjects.ConfirmationPage;
import com.vikram.pageobjects.OrderPage;
import com.vikram.pageobjects.ProductCatalogue;

public class SubmitOrderTest extends BaseTest {

	String productName = "ZARA COAT 3";

	@Test(dataProvider = "getData", groups = { "Purchase" })
	public void submitOrder(HashMap<String, String> input) throws IOException, InterruptedException {

		ProductCatalogue productCataloue = landingPage.loginApplication(input.get("email"), input.get("password"));

		List<WebElement> products = productCataloue.getProductList();
		productCataloue.addProductToCart(input.get("product"));
		CartPage cartPage = productCataloue.goToCartPage();

		Boolean match = cartPage.verifyProductDisplay(input.get("product"));
		Assert.assertTrue(match);

		CheckoutPage checkoutPage = cartPage.goToCheckout();
		checkoutPage.selectCountry("India");
		ConfirmationPage confirmationPage = checkoutPage.submitOrder();

		String confirmMessage = confirmationPage.getConfirmationMessage();
		Assert.assertTrue(confirmMessage.equalsIgnoreCase("THANKYOU FOR THE ORDER."));

	}

	@Test(dependsOnMethods = { "submitOrder" })
	public void orderHistory() {

		// "ZARA COAT 3";
		ProductCatalogue productCataloue = landingPage.loginApplication("***REDACTED-EMAIL***", "***REDACTED-PASSWORD***");
		OrderPage orderPage = productCataloue.goToOrderPage();
		Assert.assertTrue(orderPage.verifyOrderDisplay(productName));

	}

//************************************* TEST DATA SET - 1 ***************************

	// This method provides test data in the form of a two-dimensional Object array,
	// where each element is a HashMap containing key-value pairs for email,
	// password, and product.

//	@DataProvider
//	public Object[][] getData()
//	{
//		// First data set: user email, password, and product
//		HashMap<String,String> map = new HashMap<String,String>();
//		map.put("email", "***REDACTED-EMAIL***");
//		map.put("password", "***REDACTED-PASSWORD***");
//		map.put("product", "ZARA COAT 3");
//		
//		// Second data set: another user email, password, and product
//		HashMap<String,String> map1 = new HashMap<String,String>();
//		map1.put("email", "***REDACTED-EMAIL***");
//		map1.put("password", "***REDACTED-PASSWORD***");
//		map1.put("product", "ADIDAS ORIGINAL");
//		
//		// Returning the data sets as a two-dimensional array
//		return new Object[][] {
//			{map}, 		// First data set
//			{map1}};	// Second data set	
//	}


	//****************************** TEST DATA SET - 2 *********************************

	/**
	 * This DataProvider method retrieves test data from a JSON file and provides it
	 * to test methods in the form of a two-dimensional Object array.
	 *
	 * Steps: 1. Reads the JSON file containing test data using the
	 * `getJsonDataToMap` method. - The file is located at:
	 * "src/test/java/com/vikram/data/Purchaseorder.json". 2. Converts the JSON
	 * content into a List of HashMaps, where each HashMap represents a single test
	 * data set (key-value pairs). 3. Returns the test data as individual rows in a
	 * two-dimensional Object array.
	 */

	@DataProvider
	public Object[][] getData() throws IOException {
		// Reading test data from the JSON file and converting it to a List of HashMaps
		List<HashMap<String, String>> data = getJsonDataToMap(System.getProperty("user.dir") + "//src//test//java//com//vikram//data//Purchaseorder.json");

		// Returning individual JSON objects as data sets
		return new Object[][] { { data.get(0) }, // First data set from JSON
				{ data.get(1) } // Second data set from JSON
		};
	}

//****************************** TEST DATA SET - 3	***********************************

// Alternative approach using a simple Object array for test data	

//	@DataProvider
//	public Object[][] getData()
//	{
//		// Returning test data in a simple 2D array: each row contains email, password, and product
//		return new Object[][] {
//			{"***REDACTED-EMAIL***","***REDACTED-PASSWORD***","ZARA COAT 3"},    // First data set
//			{"***REDACTED-EMAIL***","***REDACTED-PASSWORD***","ADIDAS ORIGINAL"} // Second data set	
//	};	
	

	//*************************** TEST DATA SET - 4 ***********************************

//	@DataProvider
//	public Object[][] getData() throws IOException {
//		// Specify the absolute path to the Excel file containing the test data
//		// Adjust this path according to the location of your testData.xlsx file
//		String filePath = "/home/b/Documents/testData.xlsx";
//
//		// Log the file path for debugging purposes to verify the correct path is being
//		// used
//		System.out.println("Looking for test data file at: " + filePath);
//
//		// Create a File object for the specified file path
//		File file = new File(filePath);
//
//		// Check if the file exists; if not, throw a runtime exception with a
//		// descriptive message
//		if (!file.exists()) {
//			throw new RuntimeException("Test data file not found at: " + filePath);
//		}
//
//		// Use Apache POI to read the Excel file
//		try (FileInputStream fis = new FileInputStream(file); // Open the file as an input stream
//				XSSFWorkbook wb = new XSSFWorkbook(fis)) { // Load the Excel workbook from the input stream
//
//			// Get the first sheet from the workbook (assumes the data is on the first
//			// sheet)
//			XSSFSheet sheet = wb.getSheetAt(0);
//
//			// Get the total number of rows in the sheet (including the header row)
//			int rowCount = sheet.getPhysicalNumberOfRows();
//
//			// Get the header row (first row) to extract column names
//			XSSFRow row = sheet.getRow(0);
//
//			// Get the total number of columns in the header row
//			int colCount = row.getLastCellNum();
//
//			// Create a 2D Object array to store the test data
//			// Each row of the array corresponds to one test case
//			// Each cell of the array contains a HashMap representing a test case's data
//			Object[][] data = new Object[rowCount - 1][1];
//
//			// Iterate over each row in the Excel sheet (excluding the header row)
//			for (int i = 0; i < rowCount - 1; i++) {
//				row = sheet.getRow(i + 1); // Move to the next row (data starts from the second row)
//				HashMap<String, String> map = new HashMap<>(); // Create a HashMap to store column-value pairs for this
//																// row
//
//				// Iterate over each column in the row
//				for (int j = 0; j < colCount; j++) {
//					// Get the column name from the header row
//					String key = sheet.getRow(0).getCell(j).toString();
//
//					// Get the value for the current cell, or set it to an empty string if the cell
//					// is null
//					String value = (row.getCell(j) == null) ? "" : row.getCell(j).toString();
//
//					// Add the column name and value as a key-value pair in the HashMap
//					map.put(key, value);
//				}
//
//				// Store the HashMap in the 2D array, representing one test case
//				data[i][0] = map;
//			}
//
//			// Return the 2D Object array containing all test cases and their data
//			return data;
//
//		} catch (IOException e) {
//			// If an exception occurs while reading the file, log the error and rethrow it
//			System.err.println("Error reading test data from file: " + filePath);
//			throw e;
//		}
//	}

}
