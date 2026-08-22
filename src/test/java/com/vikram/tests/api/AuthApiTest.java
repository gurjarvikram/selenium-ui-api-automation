package com.vikram.tests.api;

import org.testng.annotations.Test;

import com.vikram.api.models.LoginResponse;
import com.vikram.base.BaseApiTest;
import com.vikram.core.ConfigManager;

import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

/** Contract checks on the login endpoint. */
public class AuthApiTest extends BaseApiTest {

	@Test(groups = { "smoke", "api" })
	public void loginReturnsToken() {
		LoginResponse response = authClient.login(
				ConfigManager.getSecret("ECOM_USER_EMAIL"),
				ConfigManager.getSecret("ECOM_USER_PASSWORD"));

		assertThat(response.getToken()).as("auth token").isNotNull().isNotBlank();
		assertThat(response.getUserId()).as("user id").isNotNull().isNotBlank();
	}

	@Test(groups = { "negative", "api" })
	public void loginRejectsWrongPassword() {
		Response response = authClient.attemptLogin(
				ConfigManager.getSecret("ECOM_USER_EMAIL"), "DefinitelyWrong@123?");

		assertThat(response.statusCode()).as("status for a wrong password").isEqualTo(400);
		assertThat(response.jsonPath().getString("message"))
				.as("documented error message")
				.isEqualTo("Incorrect email or password.");
	}
}
