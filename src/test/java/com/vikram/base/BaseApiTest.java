package com.vikram.base;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.annotations.BeforeClass;

import com.vikram.api.clients.AuthClient;
import com.vikram.api.clients.OrderClient;
import com.vikram.api.clients.ProductClient;
import com.vikram.api.models.LoginResponse;

/**
 * Authenticates once per class and exposes ready-to-use clients.
 *
 * No browser is started here, so the API suite runs without Chrome installed -- which is
 * what lets CI run it as a fast gate ahead of the UI suite.
 */
public class BaseApiTest {

	protected AuthClient authClient;
	protected ProductClient productClient;
	protected OrderClient orderClient;
	protected LoginResponse session;

	@BeforeClass(alwaysRun = true)
	public void authenticate() {
		authClient = new AuthClient();
		session = authClient.loginAsDefaultUser();
		productClient = new ProductClient(session.getToken());
		orderClient = new OrderClient(session.getToken());
	}

	/** Resolves a fixture file from the test classpath. */
	protected File fixture(String classpathResource) {
		URL url = getClass().getClassLoader().getResource(classpathResource);
		if (url == null) {
			throw new IllegalArgumentException("Fixture not found on classpath: " + classpathResource);
		}
		try {
			return new File(url.toURI());
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Malformed fixture path: " + classpathResource, e);
		}
	}
}
