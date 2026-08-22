package com.vikram.api.clients;

import static io.restassured.RestAssured.given;

import com.vikram.api.endpoints.ApiEndpoints;
import com.vikram.api.models.LoginRequest;
import com.vikram.api.models.LoginResponse;
import com.vikram.api.specs.SpecFactory;
import com.vikram.core.users.User;
import com.vikram.core.users.UserManager;

import io.restassured.response.Response;

/** Authentication against the ecommerce backend. */
public class AuthClient {

	/**
	 * Logs in and returns the parsed response. Used both by the API tests and, in the
	 * hybrid tests, to obtain a token that is injected straight into the browser.
	 */
	public LoginResponse login(String email, String password) {
		return given()
				.spec(SpecFactory.credentials())
				.body(new LoginRequest(email, password))
				.when()
				.post(ApiEndpoints.LOGIN.path())
				.then()
				.spec(SpecFactory.okJson())
				.extract()
				.as(LoginResponse.class);
	}

	public LoginResponse login(User user) {
		return login(user.email(), user.password());
	}

	/** Logs in as the role the suites run as by default. */
	public LoginResponse loginAsStandardCustomer() {
		return login(UserManager.standardCustomer());
	}

	/** Raw response, so negative tests can assert on non-200 status codes. */
	public Response attemptLogin(String email, String password) {
		return given()
				.spec(SpecFactory.credentials())
				.body(new LoginRequest(email, password))
				.when()
				.post(ApiEndpoints.LOGIN.path());
	}
}
