package com.vikram.ui.components;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.vikram.ui.pages.CartPage;
import com.vikram.ui.pages.OrderPage;

public class AbstractComponent {

	WebDriver driver;

	public AbstractComponent(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// PageFactory
	@FindBy(css = "button[routerlink*='/dashboard/cart']")
	WebElement cartHeader;
	
	@FindBy(css = "button[routerlink*='/dashboard/myorders']")
	WebElement orderHeader;

	public void waitForElementToAppear(By findBy) {

		// Explicit wait for product list
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));

	}
	public void waitForWebElementToAppear(WebElement findBy) {

		// Explicit wait for product list
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(7));
		wait.until(ExpectedConditions.visibilityOf(findBy));

	}

	public CartPage goToCartPage() {

		// Ensure the cart button is clickable
		cartHeader.click();

		CartPage cartPage = new CartPage(driver);
		return cartPage;

	}
	
	public OrderPage goToOrderPage() {

		// Ensure the cart button is clickable
		orderHeader.click();

		OrderPage orderPage = new OrderPage(driver);
		return orderPage;

	}

	/**
	 * Waits for a spinner or overlay to clear. Uses an explicit invisibility condition
	 * rather than a fixed sleep, so a fast page does not pay a fixed toll and a slow one
	 * is not cut off early.
	 */
	public void waitForElementToDisappear(WebElement ele) {
		new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.invisibilityOf(ele));
	}

}
