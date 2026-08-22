package com.vikram.api.clients;

import static io.restassured.RestAssured.given;

import com.vikram.api.endpoints.ApiEndpoints;
import com.vikram.api.models.LoginRequest;
import com.vikram.api.models.LoginResponse;
import com.vikram.api.specs.SpecFactory;
import com.vikram.core.ConfigManager;

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

	/** Logs in with the credentials supplied through the environment. */
	public LoginResponse loginAsDefaultUser() {
		return login(ConfigManager.getSecret("ECOM_USER_EMAIL"),
				ConfigManager.getSecret("ECOM_USER_PASSWORD"));
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
