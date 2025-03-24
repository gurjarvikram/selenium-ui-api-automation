package com.vikram.stepdefinitions;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.vikram.TestComponents.BaseTest;
import com.vikram.pageobjects.CartPage;
import com.vikram.pageobjects.CheckoutPage;
import com.vikram.pageobjects.ConfirmationPage;
import com.vikram.pageobjects.LandingPage;
import com.vikram.pageobjects.ProductCatalogue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinitionSumitOrder extends BaseTest{
	
	public LandingPage landingPage;
	public ProductCatalogue productCatalogue;
	public ConfirmationPage confirmationPage;
	public CheckoutPage checkoutPage;
	
	@Given("I landed on Ecommerce page")
	public void I_landed_on_Ecommerce_page() throws IOException
	{
		//code
		landingPage = lanuchApplication();
	}
	
	@Given("^I logged in with username (.+) and password (.+)$")
	public void logged_in_with_username_and_password(String username, String password)
	{
		productCatalogue = landingPage.loginApplication(username, password);									   
	}
	
	@When("^I add product (.+) to cart$")
	public void I_add_product_to_cart(String productName) throws InterruptedException
	{
		List<WebElement> products = productCatalogue.getProductList();
		productCatalogue.addProductToCart(productName);
	}
	
	@When("^I checkout (.+) and sumit the order$")
	public void I_checkout_and_sumit_the_order(String productName)
	{
		CartPage cartPage = productCatalogue.goToCartPage();

		Boolean match = cartPage.verifyProductDisplay(productName);
		Assert.assertTrue(match);
		checkoutPage = cartPage.goToCheckout();

		checkoutPage.selectCountry("india");
		confirmationPage = checkoutPage.submitOrder();
	}
	
	@Then("{string} message is displayed on confirmation page")
	public void message_is_displayed_on_confirmation_page(String string)
	{
		String confirmMessage = confirmationPage.getConfirmationMessage(); // Confirmation message
		Assert.assertTrue(confirmMessage.equalsIgnoreCase(string));
		driver.close();
	}
	
	@Then("{string} message is displayed")
	public void message_is_displayed(String string)
	{
		Assert.assertEquals(string, landingPage.getErrorMessage());
		driver.close();
	}
	
	

}
