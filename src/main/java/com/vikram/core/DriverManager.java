package com.vikram.core;

import org.openqa.selenium.WebDriver;

import com.vikram.core.exceptions.FrameworkException;

/**
 * Holds one WebDriver per thread.
 *
 * The suites run with parallel="classes", so a shared driver field would let two tests
 * drive the same browser. Every layer that needs the driver reads it from here instead.
 */
public final class DriverManager {

	private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

	private DriverManager() {
	}

	public static void set(WebDriver driver) {
		DRIVER.set(driver);
	}

	public static WebDriver get() {
		WebDriver driver = DRIVER.get();
		if (driver == null) {
			throw new FrameworkException(
					"No WebDriver bound to thread '" + Thread.currentThread().getName()
							+ "'. Did the test extend BaseUiTest?");
		}
		return driver;
	}

	public static boolean isSet() {
		return DRIVER.get() != null;
	}

	public static void quit() {
		WebDriver driver = DRIVER.get();
		if (driver != null) {
			driver.quit();
			DRIVER.remove();
		}
	}
}
