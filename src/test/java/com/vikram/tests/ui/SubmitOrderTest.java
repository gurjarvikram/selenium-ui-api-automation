package com.vikram.tests.ui;

import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.vikram.base.BaseUiTest;
import com.vikram.core.ConfigManager;
import com.vikram.listeners.Retry;
import com.vikram.ui.pages.CartPage;
import com.vikram.ui.pages.CheckoutPage;
import com.vikram.ui.pages.ConfirmationPage;
import com.vikram.ui.pages.OrderPage;
import com.vikram.ui.pages.ProductCatalogue;

/** End-to-end purchase journey driven entirely through the browser. */
public class SubmitOrderTest extends BaseUiTest {

	@Test(dataProvider = "purchaseData", groups = { "smoke", "regression" }, retryAnalyzer = Retry.class)
	public void submitOrder(HashMap<String, String> data) {
		ProductCatalogue catalogue = landingPage.loginApplication(
				ConfigManager.getSecret("ECOM_USER_EMAIL"),
				ConfigManager.getSecret("ECOM_USER_PASSWORD"));

		catalogue.addProductToCart(data.get("product"));
		CartPage cartPage = catalogue.goToCartPage();
		Assert.assertTrue(cartPage.verifyProductDisplay(data.get("product")),
				"Product '" + data.get("product") + "' should appear in the cart");

		CheckoutPage checkoutPage = cartPage.goToCheckout();
		checkoutPage.selectCountry(data.get("country"));
		ConfirmationPage confirmationPage = checkoutPage.submitOrder();

		Assert.assertEquals(confirmationPage.getConfirmationMessage().toUpperCase(),
				"THANKYOU FOR THE ORDER.", "Order confirmation message mismatch");
	}

	@Test(groups = { "regression" }, dependsOnMethods = "submitOrder")
	public void orderAppearsInHistory() {
		ProductCatalogue catalogue = landingPage.loginApplication(
				ConfigManager.getSecret("ECOM_USER_EMAIL"),
				ConfigManager.getSecret("ECOM_USER_PASSWORD"));

		OrderPage orderPage = catalogue.goToOrderPage();
		Assert.assertTrue(orderPage.verifyOrderDisplay("ZARA COAT 3"),
				"Previously placed order should be listed in order history");
	}

	@DataProvider(name = "purchaseData")
	public Object[][] purchaseData() {
		List<HashMap<String, String>> rows = readTestData("testdata/purchase-order.json");
		return rows.stream().map(row -> new Object[] { row }).toArray(Object[][]::new);
	}
}
