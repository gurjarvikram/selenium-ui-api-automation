package com.vikram.tests.hybrid;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.vikram.base.BaseHybridTest;
import com.vikram.listeners.Retry;
import com.vikram.ui.pages.CartPage;
import com.vikram.ui.pages.CheckoutPage;
import com.vikram.ui.pages.ConfirmationPage;
import com.vikram.ui.pages.ProductCatalogue;

/**
 * The same purchase journey as the UI suite, but authenticated over the API.
 *
 * The token from /auth/login is written into localStorage and the browser starts on the
 * dashboard, so the test spends its time on the behaviour it actually covers -- checkout
 * -- instead of re-proving that the login form works. A broken login form fails one
 * dedicated test here rather than every test in the suite.
 */
public class ApiLoginUiJourneyTest extends BaseHybridTest {

	@Test(groups = { "smoke", "hybrid" }, retryAnalyzer = Retry.class)
	public void completesCheckoutAfterApiLogin() {
		ProductCatalogue catalogue = session.loginViaApiAsDefaultUser();

		catalogue.addProductToCart("ZARA COAT 3");
		CartPage cartPage = catalogue.goToCartPage();
		Assert.assertTrue(cartPage.verifyProductDisplay("ZARA COAT 3"),
				"Product added after API login should be in the cart");

		CheckoutPage checkoutPage = cartPage.goToCheckout();
		checkoutPage.selectCountry("India");
		ConfirmationPage confirmation = checkoutPage.submitOrder();

		Assert.assertEquals(confirmation.getConfirmationMessage().toUpperCase(),
				"THANKYOU FOR THE ORDER.", "Order placed via API-seeded session should confirm");
	}
}
