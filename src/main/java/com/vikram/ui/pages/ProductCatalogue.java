package com.vikram.ui.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.vikram.ui.components.AbstractComponent;

public class ProductCatalogue extends AbstractComponent{

	WebDriver driver;

	public ProductCatalogue(WebDriver driver) {		
		super(driver);
		// Initilization
		this.driver = driver;
		PageFactory.initElements(driver, this);

	}

	// PageFactory
	
	@FindBy(css = ".mb-3")
	private List<WebElement> products;
	
	@FindBy(css = ".ngx-spinner-overlay")
	private WebElement spinner;
	
	private By productBy = By.cssSelector(".mb-3");
	private By addToCart = By.cssSelector(".card-body button:last-of-type");
	private By toastMessage =	By.cssSelector("#toast-container");
	
	
	
	
	public List<WebElement> getProductList() 
	{		
		waitForElementToAppear(productBy);
		return products;
	}
	
	/**
	 * Fails with the product name in the message rather than returning null, so a missing
	 * fixture reports itself instead of surfacing later as a NullPointerException.
	 */
	public WebElement getProductByName(String productName) {
		return getProductList().stream()
				.filter(product -> product.findElement(By.cssSelector("b")).getText().equals(productName))
				.findFirst()
				.orElseThrow(() -> new NoSuchElementException(
						"Product '" + productName + "' is not present in the catalogue"));
	}
	
	
	public void addProductToCart(String productName) {
        // Click the "Add to Cart" button
		WebElement prod = getProductByName(productName);
        prod.findElement(addToCart).click();

        // Wait for the toast message to appear
        waitForElementToAppear(toastMessage);
        waitForElementToDisappear(spinner);
      
	}
}
