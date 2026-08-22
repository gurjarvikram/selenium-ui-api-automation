package com.vikram.ui;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.vikram.core.ConfigManager;

/**
 * Explicit waits, used everywhere.
 *
 * The framework sets no implicit wait at all. Mixing the two makes timeouts
 * unpredictable -- the implicit wait applies inside each polling cycle of the explicit
 * one, so a documented 15-second wait can take far longer, and a negative check that
 * should fail fast pays the implicit timeout on every poll. Explicit-only keeps the
 * timeout equal to the number written here.
 */
public final class Waits {

	private final WebDriver driver;
	private final Duration timeout;

	public Waits(WebDriver driver) {
		this.driver = driver;
		this.timeout = Duration.ofSeconds(ConfigManager.getInt("timeout.explicit.seconds", 15));
	}

	private WebDriverWait until() {
		return new WebDriverWait(driver, timeout);
	}

	public WebElement visible(By locator) {
		return until().until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public WebElement clickable(By locator) {
		return until().until(ExpectedConditions.elementToBeClickable(locator));
	}

	public List<WebElement> allVisible(By locator) {
		return until().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	public void invisible(By locator) {
		until().until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public void textToBePresent(By locator, String text) {
		until().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
	}

	/** Clicks once the element is genuinely clickable, rather than as soon as it exists. */
	public void click(By locator) {
		clickable(locator).click();
	}

	public void type(By locator, String text) {
		WebElement element = visible(locator);
		element.clear();
		element.sendKeys(text);
	}
}
