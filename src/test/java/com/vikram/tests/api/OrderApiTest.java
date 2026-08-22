package com.vikram.tests.api;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.vikram.api.models.Product;
import com.vikram.base.BaseApiTest;

import io.restassured.response.Response;

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

		Assert.assertNotNull(createdProductId, "add-product should return the new product id");

		Response deleted = productClient.deleteProduct(createdProductId);
		Assert.assertEquals(deleted.statusCode(), 200, "Product deletion should succeed");
		Assert.assertEquals(deleted.jsonPath().getString("message"), "Product Deleted Successfully");
		createdProductId = null;
	}

	@Test(groups = { "regression", "api" })
	public void placesOrderForCreatedProduct() {
		createdProductId = productClient.addProduct(
				Product.defaults(session.getUserId(), fixture("testdata/product-image.png"))
						.withName("API ORDER FIXTURE"));

		Response order = orderClient.createOrder(createdProductId, "India");

		Assert.assertEquals(order.statusCode(), 201, "Order creation should return 201");
		Assert.assertEquals(order.jsonPath().getString("message"), "Order Placed Successfully");
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
