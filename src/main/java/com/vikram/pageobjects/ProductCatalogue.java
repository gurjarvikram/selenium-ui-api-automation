package com.vikram.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.vikram.AbstractComponent.AbstractComponent;

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
	
	By productBy = By.cssSelector(".mb-3");
	By addToCart = By.cssSelector(".card-body button:last-of-type");
	By toastMessage =	By.cssSelector("#toast-container");
	
	
	
	
	public List<WebElement> getProductList() 
	{		
		waitForElementToAppear(productBy);
		return products;
	}
	
	public WebElement getProductByName(String productName) {
		
        // Use streams to find the desired product
        WebElement prod = getProductList().stream()
            .filter(product -> product.findElement(By.cssSelector("b")).getText().equals(productName))
            .findFirst().orElse(null);
        return prod;
	}
	
	
	public void addProductToCart(String productName) throws InterruptedException 
	{
        // Click the "Add to Cart" button
		WebElement prod = getProductByName(productName);
        prod.findElement(addToCart).click();

        // Wait for the toast message to appear
        waitForElementToAppear(toastMessage);
        waitForElementToDisappear(spinner);
      
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
