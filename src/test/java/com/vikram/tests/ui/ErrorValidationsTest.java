package com.vikram.tests.ui;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.vikram.base.BaseUiTest;
import com.vikram.core.ConfigManager;
import com.vikram.listeners.Retry;
import com.vikram.ui.pages.CartPage;
import com.vikram.ui.pages.ProductCatalogue;

/** Negative paths: bad credentials and absent cart contents. */
public class ErrorValidationsTest extends BaseUiTest {

	@Test(groups = { "negative", "regression" }, retryAnalyzer = Retry.class)
	public void rejectsIncorrectPassword() {
		landingPage.loginApplication(ConfigManager.getSecret("ECOM_USER_EMAIL"), "DefinitelyWrong@123?");

		Assert.assertEquals(landingPage.getErrorMessage(), "Incorrect email or password.",
				"Login with a wrong password should surface the standard error");
	}

	@Test(groups = { "negative", "regression" })
	public void cartDoesNotShowUnaddedProduct() {
		ProductCatalogue catalogue = landingPage.loginApplication(
				ConfigManager.getSecret("ECOM_USER_EMAIL"),
				ConfigManager.getSecret("ECOM_USER_PASSWORD"));

		catalogue.addProductToCart("ZARA COAT 3");
		CartPage cartPage = catalogue.goToCartPage();

		Assert.assertFalse(cartPage.verifyProductDisplay("ZARA COAT 34"),
				"A product that was never added must not appear in the cart");
	}
}
