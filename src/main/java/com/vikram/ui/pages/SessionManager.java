package com.vikram.ui.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.vikram.api.clients.AuthClient;
import com.vikram.api.models.LoginResponse;
import com.vikram.core.users.User;
import com.vikram.core.users.UserManager;
import com.vikram.ui.Routes;
import com.vikram.ui.UiRoute;

/**
 * Puts the browser into a logged-in state without driving the login form.
 *
 * The application keeps its auth token in localStorage, so a token obtained over the API
 * can be written straight into the origin and the browser lands on the dashboard already
 * authenticated. Tests that are not about login itself use this: it removes a page load
 * and a form round-trip from every one of them, and it stops an unrelated login change
 * from failing the whole suite.
 */
public class SessionManager {

	private final WebDriver driver;
	private final AuthClient authClient = new AuthClient();

	public SessionManager(WebDriver driver) {
		this.driver = driver;
	}

	/** Authenticates over the API, seeds the session, and returns the catalogue page. */
	public ProductCatalogue loginViaApi(User user) {
		seedSession(authClient.login(user));
		return new ProductCatalogue(driver);
	}

	public ProductCatalogue loginViaApiAsStandardCustomer() {
		return loginViaApi(UserManager.standardCustomer());
	}

	/**
	 * Writes the token onto the application origin. The browser has to already be on that
	 * origin before localStorage is addressable, hence the initial get().
	 */
	public void seedSession(LoginResponse login) {
		driver.get(Routes.baseUrl());

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.localStorage.setItem('token', arguments[0]);", login.getToken());
		js.executeScript("window.localStorage.setItem('userId', arguments[0]);", login.getUserId());

		driver.get(UiRoute.DASHBOARD.url());
	}
}
