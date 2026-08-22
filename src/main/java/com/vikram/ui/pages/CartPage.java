package com.vikram.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.vikram.ui.components.AbstractComponent;

/** Cart contents and the route to checkout. */
public class CartPage extends AbstractComponent {

	public CartPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected String pageName() {
		return "cart-page";
	}

	/**
	 * An empty cart answers "no" rather than timing out: the negative tests ask whether a
	 * product they never added is present, and waiting 15 seconds to be told the cart is
	 * empty is both slow and a misleading failure.
	 */
	public boolean isProductDisplayed(String productName) {
		return waits.allVisibleOrEmpty(locator("cartProduct")).stream()
				.map(WebElement::getText)
				.anyMatch(text -> text.equalsIgnoreCase(productName));
	}

	public CheckoutPage goToCheckout() {
		waits.click(locator("checkout"));
		return new CheckoutPage(driver);
	}
}
