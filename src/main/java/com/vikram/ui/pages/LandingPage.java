package com.vikram.ui.pages;

import org.openqa.selenium.WebDriver;

import com.vikram.core.ConfigManager;
import com.vikram.ui.components.AbstractComponent;

/** Login screen. */
public class LandingPage extends AbstractComponent {

	public LandingPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected String pageName() {
		return "landing-page";
	}

	public void goTo() {
		driver.get(ConfigManager.get("ui.base.url"));
	}

	public ProductCatalogue loginApplication(String email, String password) {
		waits.type(locator("userEmail"), email);
		waits.type(locator("userPassword"), password);
		waits.click(locator("loginButton"));
		return new ProductCatalogue(driver);
	}

	/** Submits credentials expected to be rejected, leaving the browser on this page. */
	public LandingPage loginExpectingFailure(String email, String password) {
		waits.type(locator("userEmail"), email);
		waits.type(locator("userPassword"), password);
		waits.click(locator("loginButton"));
		return this;
	}

	public String getErrorMessage() {
		return waits.visible(locator("errorMessage")).getText();
	}
}
