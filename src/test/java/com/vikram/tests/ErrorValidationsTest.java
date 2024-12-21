package com.vikram.tests;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.vikram.TestComponents.Retry;
import com.vikram.TestComponents.BaseTest;
import com.vikram.pageobjects.CartPage;
import com.vikram.pageobjects.ProductCataloue;

public class ErrorValidationsTest extends BaseTest {

	@Test(groups= {"ErrorHandling"}, retryAnalyzer= Retry.class)
	public void loginErrorValidation() throws IOException, InterruptedException 
	{

		landingPage.loginAplication("***REDACTED-EMAIL***", "AdminWrong@123?");
		Assert.assertEquals("Incorrect email or password.",landingPage.getErrorMessage());					
		//Incorrect email or password.
	}
	
	@Test
	public void productErrorValidation() throws IOException, InterruptedException 
	{

		String productName = "ZARA COAT 3";

		ProductCataloue productCataloue = landingPage.loginAplication("***REDACTED-EMAIL***", "***REDACTED-PASSWORD***");

		List<WebElement> products = productCataloue.getProductList();
		productCataloue.addProductToCart(productName);
		CartPage cartPage = productCataloue.goToCartPage();

		Boolean match = cartPage.verifyProductDisplay("ZARA COAT 34");
		Assert.assertFalse(match);

	}
}
