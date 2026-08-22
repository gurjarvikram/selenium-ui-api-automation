package com.vikram.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.vikram.core.ConfigManager;
import com.vikram.ui.components.AbstractComponent;

public class LandingPage extends AbstractComponent {

	WebDriver driver;

	public LandingPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);

	}

	// PageFactory
	@FindBy(id = "userEmail")
	private WebElement userEmailField;

	@FindBy(id = "userPassword")
	private WebElement passwordField;

	@FindBy(id = "login")
	private WebElement loginButton;
	
	@FindBy(css = "[class*='flyInOut']")
	private WebElement errorMessage;

	
	public ProductCatalogue loginApplication(String email, String password) {
		
		userEmailField.sendKeys(email);
		passwordField.sendKeys(password);
		loginButton.click();
		
		ProductCatalogue productCatalogue = new ProductCatalogue(driver);
		return productCatalogue;
	}

	public String getErrorMessage() {
		waitForWebElementToAppear(errorMessage);
		return errorMessage.getText();

	}
	
	public void goTo() {
		driver.get(ConfigManager.get("ui.base.url"));
	}

}
