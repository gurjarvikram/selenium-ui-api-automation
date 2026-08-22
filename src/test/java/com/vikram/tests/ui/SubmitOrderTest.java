package com.vikram.tests.ui;

import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.vikram.base.BaseUiTest;
import com.vikram.core.ConfigManager;
import com.vikram.ui.pages.CartPage;
import com.vikram.ui.pages.CheckoutPage;
import com.vikram.ui.pages.ConfirmationPage;
import com.vikram.ui.pages.ProductCatalogue;
import com.vikram.utils.JsonUtils;

import static org.assertj.core.api.Assertions.assertThat;

/** End-to-end purchase journey driven entirely through the browser. */
public class SubmitOrderTest extends BaseUiTest {

	private static final String FIXTURE = "testdata/purchase-orders.json";

	@Test(dataProvider = "customers", groups = { "smoke", "regression" })
	public void submitOrder(String role, Map<String, String> data) {
		log.info("Placing an order as '{}' for '{}'", role, data.get("product"));

		ProductCatalogue catalogue = landingPage.loginApplication(
				ConfigManager.getSecret("ECOM_USER_EMAIL"),
				ConfigManager.getSecret("ECOM_USER_PASSWORD"));

		CartPage cartPage = catalogue.addProductToCart(data.get("product")).goToCartPage();
		assertThat(cartPage.isProductDisplayed(data.get("product")))
				.as("cart should contain '%s' after adding it", data.get("product"))
				.isTrue();

		CheckoutPage checkoutPage = cartPage.goToCheckout();
		ConfirmationPage confirmation = checkoutPage.selectCountry(data.get("country")).submitOrder();

		assertThat(confirmation.getConfirmationMessage().toUpperCase())
				.as("order confirmation message")
				.isEqualTo(data.get("expectedConfirmation"));
	}

	/** Feeds every role in the fixture, so adding a customer needs no code change. */
	@DataProvider(name = "customers")
	public Object[][] customers() {
		return JsonUtils.readRoles(FIXTURE).entrySet().stream()
				.map(entry -> new Object[] { entry.getKey(), entry.getValue() })
				.toArray(Object[][]::new);
	}
}
