package com.vikram.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.vikram.ui.components.AbstractComponent;

/** Delivery details and order submission. */
public class CheckoutPage extends AbstractComponent {

	public CheckoutPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected String pageName() {
		return "checkout-page";
	}

	/**
	 * The country field is an autocomplete: it needs real keystrokes to trigger its
	 * suggestion list, which is why this uses Actions rather than sendKeys.
	 */
	public CheckoutPage selectCountry(String countryName) {
		new Actions(driver).sendKeys(waits.visible(locator("countryInput")), countryName).perform();
		waits.visible(locator("countryResults"));
		waits.click(locator("countrySuggestion"));
		return this;
	}

	public ConfirmationPage submitOrder() {
		waits.click(locator("placeOrder"));
		return new ConfirmationPage(driver);
	}
}
