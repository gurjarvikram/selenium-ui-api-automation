package com.vikram.api.clients;

import static io.restassured.RestAssured.given;

import com.vikram.api.endpoints.ApiEndpoints;
import com.vikram.api.models.Product;
import com.vikram.api.specs.SpecFactory;

import io.restassured.response.Response;

/** Product lifecycle calls, used mainly to seed and clean up fixtures. */
public class ProductClient {

	private final String token;

	public ProductClient(String token) {
		this.token = token;
	}

	/** Creates a product and returns its id. */
	public String addProduct(Product product) {
		return given()
				.spec(SpecFactory.authenticatedMultipart(token))
				.param("productName", product.getProductName())
				.param("productAddedBy", product.getProductAddedBy())
				.param("productCategory", product.getProductCategory())
				.param("productSubCategory", product.getProductSubCategory())
				.param("productPrice", product.getProductPrice())
				.param("productDescription", product.getProductDescription())
				.param("productFor", product.getProductFor())
				.multiPart("productImage", product.getProductImage())
				.when()
				.post(ApiEndpoints.ADD_PRODUCT.path())
				.then()
				.statusCode(201)
				.extract()
				.path("productId");
	}

	public Response deleteProduct(String productId) {
		return given()
				.spec(SpecFactory.authenticated(token))
				.pathParam("productId", productId)
				.when()
				.delete(ApiEndpoints.DELETE_PRODUCT.path());
	}

	/**
	 * Best-effort cleanup for @AfterMethod. A failed teardown should not mask the
	 * assertion failure that the test was actually reporting.
	 */
	public void deleteProductQuietly(String productId) {
		if (productId == null || productId.isBlank()) {
			return;
		}
		try {
			deleteProduct(productId);
		} catch (RuntimeException e) {
			System.err.println("Cleanup failed for product " + productId + ": " + e.getMessage());
		}
	}
}
