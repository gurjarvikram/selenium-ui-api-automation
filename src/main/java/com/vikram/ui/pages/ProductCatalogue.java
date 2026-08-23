package com.vikram.ui.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.vikram.ui.components.AbstractComponent;

/** Product grid shown after login. */
public class ProductCatalogue extends AbstractComponent {

	/** Toast the application raises once the add-to-cart call has been accepted. */
	private static final String ADDED_TO_CART = "Product Added To Cart";

	public ProductCatalogue(WebDriver driver) {
		super(driver);
	}

	@Override
	protected String pageName() {
		return "product-catalogue";
	}

	public List<WebElement> getProductList() {
		return waits.allVisible(locator("productCard"));
	}

	/**
	 * Fails with the product name in the message rather than returning null, so a missing
	 * fixture reports itself instead of surfacing later as a NullPointerException.
	 */
	public WebElement getProductByName(String productName) {
		By title = locator("productTitle");
		return getProductList().stream()
				.filter(card -> card.findElement(title).getText().equals(productName))
				.findFirst()
				.orElseThrow(() -> new NoSuchElementException(
						"Product '" + productName + "' is not present in the catalogue"));
	}

	/**
	 * Adds one product to the cart, returning only once the server has confirmed it.
	 *
	 * The click goes through the resilient Waits.click against a locator that addresses
	 * this product's button directly. Previously it was a raw click on a WebElement found
	 * by walking the card list, which bypassed the interception retry entirely -- the
	 * overlay could swallow it, the toast never appeared, and the cart was empty by the
	 * time the next page asserted on it.
	 *
	 * The wait afterwards is on the toast's *text*, not merely on the container being
	 * visible. Both toasts share one #toast-container, and the "Login Successfully" toast
	 * lives about five seconds -- longer than it takes to reach this point -- so a plain
	 * visibility check was satisfied by the login toast the instant the click landed and
	 * synchronised on nothing. The add's own POST was still in flight when goToCartPage
	 * navigated away, and the cart rendered empty. That is invisible on a fast machine,
	 * where the POST wins the race anyway, and reproducible on a loaded CI runner.
	 *
	 * Waiting for the container to clear afterwards keeps the next add honest: it cannot
	 * mistake this add's toast for its own.
	 */
	public ProductCatalogue addProductToCart(String productName) {
		waitForSpinnerToClear();
		getProductByName(productName);
		waits.click(locator("addToCartFor", productName));
		waits.textToBePresent(common("toast"), ADDED_TO_CART);
		waits.invisible(common("toast"));
		waitForSpinnerToClear();
		return this;
	}
}
