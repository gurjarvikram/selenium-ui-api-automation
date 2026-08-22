package com.vikram.tests.api;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.vikram.api.models.Product;
import com.vikram.base.BaseApiTest;

import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Product and order lifecycle over the API alone.
 *
 * Each test cleans up the product it created, so the suite can be re-run against the same
 * account without accumulating fixtures.
 */
public class OrderApiTest extends BaseApiTest {

	private String createdProductId;

	@Test(groups = { "smoke", "api" })
	public void createsAndDeletesProduct() {
		createdProductId = productClient.addProduct(
				Product.defaults(session.getUserId(), fixture("testdata/product-image.png")));

		assertThat(createdProductId).as("new product id from add-product").isNotNull().isNotBlank();

		Response deleted = productClient.deleteProduct(createdProductId);
		assertThat(deleted.statusCode()).as("delete-product status").isEqualTo(200);
		assertThat(deleted.jsonPath().getString("message")).isEqualTo("Product Deleted Successfully");
		createdProductId = null;
	}

	@Test(groups = { "regression", "api" })
	public void placesOrderForCreatedProduct() {
		createdProductId = productClient.addProduct(
				Product.defaults(session.getUserId(), fixture("testdata/product-image.png"))
						.withName("API ORDER FIXTURE"));

		Response order = orderClient.createOrder(createdProductId, "India");

		assertThat(order.statusCode()).as("create-order status").isEqualTo(201);
		assertThat(order.jsonPath().getString("message")).isEqualTo("Order Placed Successfully");
	}

	/**
	 * alwaysRun means this fires even when @BeforeClass authentication failed and the
	 * clients were never built, so it has to tolerate a null client.
	 */
	@AfterMethod(alwaysRun = true)
	public void cleanUpProduct() {
		if (productClient != null) {
			productClient.deleteProductQuietly(createdProductId);
		}
		createdProductId = null;
	}
}
