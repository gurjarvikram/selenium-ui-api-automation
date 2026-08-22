package com.vikram.tests.hybrid;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.vikram.api.models.Product;
import com.vikram.base.BaseHybridTest;
import com.vikram.ui.pages.OrderPage;
import com.vikram.ui.pages.ProductCatalogue;

import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Set the state up over the API, then assert the UI renders it.
 *
 * Seeding an order through the backend takes a fraction of the time the equivalent
 * click-through would, and it removes the checkout flow as a dependency: if this fails,
 * the defect is in how orders are displayed, not in how they are placed.
 */
public class ApiSetupUiVerificationTest extends BaseHybridTest {

	private static final String FIXTURE_PRODUCT = "HYBRID FIXTURE COAT";

	private String productId;

	@Test(groups = { "regression", "hybrid" })
	public void orderCreatedViaApiIsListedInUi() {
		productId = productClient.addProduct(
				Product.defaults(apiSession.getUserId(), fixture("testdata/product-image.png"))
						.withName(FIXTURE_PRODUCT));

		Response order = orderClient.createOrder(productId, "India");
		assertThat(order.statusCode()).as("precondition: order created over the API").isEqualTo(201);

		ProductCatalogue catalogue = session.loginViaApiAsDefaultUser();
		OrderPage orderPage = catalogue.goToOrderPage();

		assertThat(orderPage.isOrderDisplayed(FIXTURE_PRODUCT))
				.as("order created over the API should be visible in the UI order history")
				.isTrue();
	}

	/** Tolerates a null client so a failed setup reports its own cause, not an NPE. */
	@AfterMethod(alwaysRun = true)
	public void cleanUpProduct() {
		if (productClient != null) {
			productClient.deleteProductQuietly(productId);
		}
		productId = null;
	}
}
