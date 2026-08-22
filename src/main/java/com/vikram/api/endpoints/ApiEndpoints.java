package com.vikram.api.endpoints;

/**
 * Every endpoint the framework talks to, in one place.
 *
 * Keeping paths in an enum rather than scattered string literals means a backend route
 * change is a one-line edit, and a typo is a compile error instead of a 404 at runtime.
 */
public enum ApiEndpoints {

	LOGIN("/api/ecom/auth/login"),
	ADD_PRODUCT("/api/ecom/product/add-product"),
	DELETE_PRODUCT("/api/ecom/product/delete-product/{productId}"),
	CREATE_ORDER("/api/ecom/order/create-order"),
	GET_ORDERS("/api/ecom/order/get-orders-for-customer/{userId}");

	private final String path;

	ApiEndpoints(String path) {
		this.path = path;
	}

	public String path() {
		return path;
	}
}
