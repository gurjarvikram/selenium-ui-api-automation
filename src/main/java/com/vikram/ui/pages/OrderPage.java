package com.vikram.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.vikram.ui.components.AbstractComponent;

/** Order history for the signed-in customer. */
public class OrderPage extends AbstractComponent {

	public OrderPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected String pageName() {
		return "order-page";
	}

	public boolean isOrderDisplayed(String productName) {
		return waits.allVisible(locator("orderedProductName")).stream()
				.map(WebElement::getText)
				.anyMatch(text -> text.equalsIgnoreCase(productName));
	}
}
