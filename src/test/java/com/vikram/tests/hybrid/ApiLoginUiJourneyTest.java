package com.vikram.tests.hybrid;

import org.testng.annotations.Test;

import com.vikram.base.BaseHybridTest;
import com.vikram.ui.pages.CartPage;
import com.vikram.ui.pages.CheckoutPage;
import com.vikram.ui.pages.ConfirmationPage;
import com.vikram.ui.pages.ProductCatalogue;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The same purchase journey as the UI suite, but authenticated over the API.
 *
 * The token from /auth/login is written into localStorage and the browser starts on the
 * dashboard, so the test spends its time on the behaviour it actually covers -- checkout
 * -- instead of re-proving that the login form works. A broken login form fails one
 * dedicated test here rather than every test in the suite.
 */
public class ApiLoginUiJourneyTest extends BaseHybridTest {

	@Test(groups = { "smoke", "hybrid" })
	public void completesCheckoutAfterApiLogin() {
		ProductCatalogue catalogue = session.loginViaApiAsStandardCustomer();

		CartPage cartPage = catalogue.addProductToCart("ZARA COAT 3").goToCartPage();
		assertThat(cartPage.isProductDisplayed("ZARA COAT 3"))
				.as("product added after API login should be in the cart")
				.isTrue();

		CheckoutPage checkoutPage = cartPage.goToCheckout();
		ConfirmationPage confirmation = checkoutPage.selectCountry("India").submitOrder();

		assertThat(confirmation.getConfirmationMessage().toUpperCase())
				.as("order placed from an API-seeded session should confirm")
				.isEqualTo("THANKYOU FOR THE ORDER.");
	}
}
