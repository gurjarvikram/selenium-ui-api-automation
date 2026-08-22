package com.vikram.tests.api;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.vikram.api.models.LoginResponse;
import com.vikram.base.BaseApiTest;
import com.vikram.core.ConfigManager;

import io.restassured.response.Response;

/** Contract checks on the login endpoint. */
public class AuthApiTest extends BaseApiTest {

	@Test(groups = { "smoke", "api" })
	public void loginReturnsToken() {
		LoginResponse response = authClient.login(
				ConfigManager.getSecret("ECOM_USER_EMAIL"),
				ConfigManager.getSecret("ECOM_USER_PASSWORD"));

		Assert.assertNotNull(response.getToken(), "Login should return an auth token");
		Assert.assertFalse(response.getToken().isBlank(), "Auth token should not be blank");
		Assert.assertNotNull(response.getUserId(), "Login should return a user id");
	}

	@Test(groups = { "negative", "api" })
	public void loginRejectsWrongPassword() {
		Response response = authClient.attemptLogin(
				ConfigManager.getSecret("ECOM_USER_EMAIL"), "DefinitelyWrong@123?");

		Assert.assertEquals(response.statusCode(), 400,
				"A wrong password should be rejected with 400");
		Assert.assertEquals(response.jsonPath().getString("message"), "Incorrect email or password.",
				"Error message should match the documented contract");
	}
}
