package com.vikram.tests.hybrid;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.vikram.base.BaseHybridTest;
import com.vikram.ui.pages.CartPage;
import com.vikram.ui.pages.CheckoutPage;
import com.vikram.ui.pages.ProductCatalogue;

import io.restassured.response.Response;

/**
 * Place the order in the browser, then verify it over the API.
 *
 * Asserting on the confirmation banner only proves the front end said the right thing.
 * Reading the order back from the backend proves it was actually persisted, which is the
 * failure this catches and a UI-only assertion does not.
 */
public class UiOrderApiVerificationTest extends BaseHybridTest {

	@Test(groups = { "regression", "hybrid" })
	public void orderPlacedInUiIsPersistedInBackend() {
		ProductCatalogue catalogue = session.loginViaApiAsDefaultUser();
		catalogue.addProductToCart("ZARA COAT 3");

		CartPage cartPage = catalogue.goToCartPage();
		CheckoutPage checkoutPage = cartPage.goToCheckout();
		checkoutPage.selectCountry("India");
		checkoutPage.submitOrder();

		Response orders = orderClient.getOrders(apiSession.getUserId());
		Assert.assertEquals(orders.statusCode(), 200, "Order history should be retrievable");
		Assert.assertTrue(orders.asString().contains("ZARA COAT 3"),
				"Order placed through the UI should be present in the backend order history");
	}
}
