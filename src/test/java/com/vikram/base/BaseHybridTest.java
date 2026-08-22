package com.vikram.base;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.annotations.BeforeMethod;

import com.vikram.api.clients.AuthClient;
import com.vikram.api.clients.OrderClient;
import com.vikram.api.clients.ProductClient;
import com.vikram.api.models.LoginResponse;

/**
 * Browser plus API clients, both authenticated as the same user.
 *
 * This is the base the cross-layer tests use: set state up over the API, assert it in the
 * UI, or the reverse.
 */
public class BaseHybridTest extends BaseUiTest {

	protected AuthClient authClient;
	protected ProductClient productClient;
	protected OrderClient orderClient;
	protected LoginResponse apiSession;

	@BeforeMethod(alwaysRun = true, dependsOnMethods = "launchApplication")
	public void authenticateApi() {
		authClient = new AuthClient();
		apiSession = authClient.loginAsDefaultUser();
		productClient = new ProductClient(apiSession.getToken());
		orderClient = new OrderClient(apiSession.getToken());
	}

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
