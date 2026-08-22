package com.vikram.tests.hybrid;

import org.testng.annotations.Test;

import com.vikram.base.BaseHybridTest;
import com.vikram.ui.pages.CartPage;
import com.vikram.ui.pages.ProductCatalogue;

import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

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
		ProductCatalogue catalogue = session.loginViaApiAsStandardCustomer();
		CartPage cartPage = catalogue.addProductToCart("ZARA COAT 3").goToCartPage();
		cartPage.goToCheckout().selectCountry("India").submitOrder();

		Response orders = orderClient.getOrders(apiSession.getUserId());
		assertThat(orders.statusCode()).as("order history status").isEqualTo(200);
		assertThat(orders.asString())
				.as("order placed through the UI should be present in the backend order history")
				.contains("ZARA COAT 3");
	}
}
