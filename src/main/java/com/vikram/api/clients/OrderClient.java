package com.vikram.api.clients;

import static io.restassured.RestAssured.given;

import java.util.List;

import com.vikram.api.endpoints.ApiEndpoints;
import com.vikram.api.models.OrderDetail;
import com.vikram.api.models.Orders;
import com.vikram.api.specs.SpecFactory;

import io.restassured.response.Response;

/** Order creation and retrieval. */
public class OrderClient {

	private final String token;

	public OrderClient(String token) {
		this.token = token;
	}

	public Response createOrder(Orders orders) {
		return given()
				.spec(SpecFactory.authenticated(token))
				.body(orders)
				.when()
				.post(ApiEndpoints.CREATE_ORDER.path());
	}

	/** Convenience for the common single-product order. */
	public Response createOrder(String productId, String country) {
		return createOrder(new Orders(List.of(new OrderDetail(country, productId))));
	}

	public Response getOrders(String userId) {
		return given()
				.spec(SpecFactory.authenticated(token))
				.pathParam("userId", userId)
				.when()
				.get(ApiEndpoints.GET_ORDERS.path());
	}
}
