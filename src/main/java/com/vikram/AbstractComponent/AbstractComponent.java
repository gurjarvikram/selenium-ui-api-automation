package com.vikram.AbstractComponent;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.vikram.pageobjects.CartPage;
import com.vikram.pageobjects.OrderPage;

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
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
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

	public void waitForElementToDisappear(WebElement ele) throws InterruptedException {
		Thread.sleep(2000);
		// Wait for the spinner/overlay to disappear completely
//		  WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
//        wait.until(ExpectedConditions.invisibilityOf(ele)); 

	}

}
