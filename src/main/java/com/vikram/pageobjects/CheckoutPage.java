package com.vikram.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.vikram.AbstractComponent.AbstractComponent;

public class CheckoutPage extends AbstractComponent {

	WebDriver driver;

	public CheckoutPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);

	}

	// pageFactory
	@FindBy(css = ".action__submit")
	private WebElement submit;

	@FindBy(css = "input[placeholder='Select Country']")
	private WebElement country;

	@FindBy(xpath = "(//button[contains(@class,'ta-item')])[2]")
	private WebElement selectCountry;

	
	private By result = By.cssSelector(".ta-results");
	

	public void selectCountry(String countryName) {

		Actions a = new Actions(driver);
		a.sendKeys(country, countryName).perform();

		// Wait for the country to appear
		waitForElementToAppear(result);

		// select the country
		selectCountry.click();

	}

	public ConfirmationPage submitOrder() {

		submit.click();
		return new ConfirmationPage(driver);
	}

}
