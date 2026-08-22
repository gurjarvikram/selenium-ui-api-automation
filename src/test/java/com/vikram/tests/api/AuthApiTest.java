package com.vikram.tests.api;

import org.testng.annotations.Test;

import com.vikram.api.models.LoginResponse;
import com.vikram.base.BaseApiTest;
import com.vikram.core.users.User;
import com.vikram.core.users.UserManager;
import com.vikram.core.users.UserRole;

import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

/** Contract checks on the login endpoint. */
public class AuthApiTest extends BaseApiTest {

	@Test(groups = { "smoke", "api" })
	public void loginReturnsToken() {
		LoginResponse response = authClient.login(UserManager.standardCustomer());

		assertThat(response.getToken()).as("auth token").isNotNull().isNotBlank();
		assertThat(response.getUserId()).as("user id").isNotNull().isNotBlank();
	}

	@Test(groups = { "negative", "api" })
	public void loginRejectsWrongPassword() {
		User user = UserManager.withWrongPassword(UserRole.STANDARD_CUSTOMER);
		Response response = authClient.attemptLogin(user.email(), user.password());

		assertThat(response.statusCode()).as("status for a wrong password").isEqualTo(400);
		assertThat(response.jsonPath().getString("message"))
				.as("documented error message")
				.isEqualTo("Incorrect email or password.");
	}
}
