package com.vikram.ui.pages;

import org.openqa.selenium.WebDriver;

import com.vikram.core.users.User;
import com.vikram.ui.UiRoute;
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
		driver.get(UiRoute.LOGIN.url());
	}

	public ProductCatalogue loginApplication(User user) {
		submit(user);
		return new ProductCatalogue(driver);
	}

	/** Submits credentials expected to be rejected, leaving the browser on this page. */
	public LandingPage loginExpectingFailure(User user) {
		submit(user);
		return this;
	}

	private void submit(User user) {
		waits.type(locator("userEmail"), user.email());
		waits.type(locator("userPassword"), user.password());
		waits.click(locator("loginButton"));
	}

	public String getErrorMessage() {
		return waits.visible(locator("errorMessage")).getText();
	}
}
