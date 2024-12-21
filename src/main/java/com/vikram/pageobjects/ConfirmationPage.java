package com.vikram.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.vikram.AbstractComponent.AbstractComponent;

public class ConfirmationPage extends AbstractComponent {

	WebDriver driver;

	public ConfirmationPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);

	}

	// pageFactory

	@FindBy(css = ".hero-primary")
	private WebElement confirmationMessage;

	public String getConfirmationMessage() {

		return confirmationMessage.getText();

	}

}
