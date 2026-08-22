package com.vikram.ui.pages;

import org.openqa.selenium.WebDriver;

import com.vikram.ui.components.AbstractComponent;

/** Post-order confirmation. */
public class ConfirmationPage extends AbstractComponent {

	public ConfirmationPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected String pageName() {
		return "confirmation-page";
	}

	public String getConfirmationMessage() {
		return waits.visible(locator("confirmationMessage")).getText();
	}
}
