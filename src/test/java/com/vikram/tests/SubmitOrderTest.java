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
import com.vikram.pageobjects.ProductCataloue;

public class SubmitOrderTest extends BaseTest {
	
	
	String productName = "ZARA COAT 3";

	@Test(dataProvider = "getData", groups = { "Purchase" })
	public void submitOrder(HashMap<String, String> input) throws IOException, InterruptedException {
		

		ProductCataloue productCataloue = landingPage.loginAplication(input.get("email"), input.get("password"));

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
		ProductCataloue productCataloue = landingPage.loginAplication("***REDACTED-EMAIL***", "***REDACTED-PASSWORD***");
		OrderPage orderPage = productCataloue.goToOrderPage();
		Assert.assertTrue(orderPage.verifyOrderDisplay(productName));

	}
	
	
//TEST DATA SET - 1 ***************************

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
	//***REDACTED-EMAIL***
	//***REDACTED-PASSWORD***

	//TEST DATA SET - 2 *********************************
	
	/**
	 * This DataProvider method retrieves test data from a JSON file and provides it 
	 * to test methods in the form of a two-dimensional Object array.
	 *
	 * Steps:
	 * 1. Reads the JSON file containing test data using the `getJsonDataToMap` method.
	 *    - The file is located at: "src/test/java/com/vikram/data/Purchaseorder.json".
	 * 2. Converts the JSON content into a List of HashMaps, where each HashMap represents
	 *    a single test data set (key-value pairs).
	 * 3. Returns the test data as individual rows in a two-dimensional Object array. 
	 * */
	
	@DataProvider
	public Object[][] getData() throws IOException {
		// Reading test data from the JSON file and converting it to a List of HashMaps
		List<HashMap<String, String>> data = getJsonDataToMap(
				System.getProperty("user.dir") + "//src//test//java//com//vikram//data//Purchaseorder.json");

		// Returning individual JSON objects as data sets
		return new Object[][] { { data.get(0) }, // First data set from JSON
				{ data.get(1) } // Second data set from JSON
		};
	}

	
//TEST DATA SET - 3	***********************************

// Alternative approach using a simple Object array for test data	

//	@DataProvider
//	public Object[][] getData()
//	{
//		// Returning test data in a simple 2D array: each row contains email, password, and product
//		return new Object[][] {
//			{"***REDACTED-EMAIL***","***REDACTED-PASSWORD***","ZARA COAT 3"},    // First data set
//			{"***REDACTED-EMAIL***","***REDACTED-PASSWORD***","ADIDAS ORIGINAL"} // Second data set	
//	};	

}
