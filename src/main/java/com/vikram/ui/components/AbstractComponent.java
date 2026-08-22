package com.vikram.ui.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.vikram.ui.ObjectRepository;
import com.vikram.ui.Waits;
import com.vikram.ui.pages.CartPage;
import com.vikram.ui.pages.OrderPage;

/**
 * Behaviour shared by every authenticated page: the header controls and the waits.
 *
 * Locators come from the object repository rather than PageFactory annotations. Plain
 * By lookups are re-resolved on each use, so they do not go stale the way a cached
 * PageFactory proxy does when the page re-renders.
 */
public abstract class AbstractComponent {

	protected static final String COMMON = "common";

	protected final WebDriver driver;
	protected final Waits waits;

	protected AbstractComponent(WebDriver driver) {
		this.driver = driver;
		this.waits = new Waits(driver);
	}

	/** Resolves a locator from this page's own repository file. */
	protected By locator(String key) {
		return ObjectRepository.by(pageName(), key);
	}

	protected By common(String key) {
		return ObjectRepository.by(COMMON, key);
	}

	/** Object repository file backing this page, without the .properties suffix. */
	protected abstract String pageName();

	public CartPage goToCartPage() {
		waits.click(common("cartHeader"));
		return new CartPage(driver);
	}

	public OrderPage goToOrderPage() {
		waits.click(common("ordersHeader"));
		return new OrderPage(driver);
	}

	/** Waits for the loading overlay to clear before the next interaction. */
	protected void waitForSpinnerToClear() {
		waits.invisible(common("spinner"));
	}
}
