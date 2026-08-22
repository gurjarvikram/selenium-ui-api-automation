package com.vikram.ui.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.vikram.ui.components.AbstractComponent;

/** Product grid shown after login. */
public class ProductCatalogue extends AbstractComponent {

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
	 * Adds one product to the cart.
	 *
	 * The click goes through the resilient Waits.click against a locator that addresses
	 * this product's button directly. Previously it was a raw click on a WebElement found
	 * by walking the card list, which bypassed the interception retry entirely -- the
	 * overlay could swallow it, the toast never appeared, and the cart was empty by the
	 * time the next page asserted on it.
	 */
	public ProductCatalogue addProductToCart(String productName) {
		waitForSpinnerToClear();
		getProductByName(productName);
		waits.click(locator("addToCartFor", productName));
		waits.visible(common("toast"));
		waitForSpinnerToClear();
		return this;
	}
}
