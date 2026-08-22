package com.vikram.tests.ui;

import java.util.Map;

import org.testng.annotations.Test;

import com.vikram.base.BaseUiTest;
import com.vikram.core.ConfigManager;
import com.vikram.ui.pages.CartPage;
import com.vikram.ui.pages.ProductCatalogue;

import static org.assertj.core.api.Assertions.assertThat;

/** Negative paths: rejected credentials and absent cart contents. */
public class ErrorValidationsTest extends BaseUiTest {

	private static final String FIXTURE = "testdata/purchase-orders.json";

	@Test(groups = { "negative", "regression" })
	public void rejectsIncorrectPassword() {
		String message = landingPage
				.loginExpectingFailure(ConfigManager.getSecret("ECOM_USER_EMAIL"), "DefinitelyWrong@123?")
				.getErrorMessage();

		assertThat(message)
				.as("error shown for a wrong password")
				.isEqualTo("Incorrect email or password.");
	}

	@Test(groups = { "negative", "regression" })
	public void cartDoesNotShowUnaddedProduct() {
		Map<String, String> data = testData(FIXTURE, "standardCustomer");

		ProductCatalogue catalogue = landingPage.loginApplication(
				ConfigManager.getSecret("ECOM_USER_EMAIL"),
				ConfigManager.getSecret("ECOM_USER_PASSWORD"));

		CartPage cartPage = catalogue.addProductToCart(data.get("product")).goToCartPage();

		assertThat(cartPage.isProductDisplayed(data.get("product") + " 34"))
				.as("a product that was never added must not appear in the cart")
				.isFalse();
	}
}
