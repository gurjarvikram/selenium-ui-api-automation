package com.vikram.core;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.Locale;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vikram.core.exceptions.ConfigurationException;

/**
 * Builds a WebDriver from configuration.
 *
 * Local by default. Setting grid.url routes the same browser choice at a Selenium Grid,
 * which is why there are no separate "grid tests" -- any suite can run remotely with
 * -Dgrid.url=http://localhost:4444.
 */
public final class DriverFactory {

	private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);

	private DriverFactory() {
	}

	public static WebDriver create() {
		String browser = ConfigManager.get("browser", "chrome").toLowerCase(Locale.ROOT);
		boolean headless = ConfigManager.getBoolean("headless", false) || browser.contains("headless");
		String gridUrl = ConfigManager.get("grid.url", "");

		MutableCapabilities options = optionsFor(browser, headless);
		log.info("Starting {} ({}) {}", browser, headless ? "headless" : "headed",
				gridUrl.isBlank() ? "locally" : "on grid " + gridUrl);

		WebDriver driver = gridUrl.isBlank() ? local(browser, options) : remote(gridUrl, options);

		driver.manage().window().setSize(new Dimension(1440, 900));
		// No implicit wait is set anywhere: see Waits for why the two must not be mixed.
		driver.manage().timeouts()
				.pageLoadTimeout(Duration.ofSeconds(ConfigManager.getInt("timeout.pageload.seconds", 30)));
		return driver;
	}

	private static MutableCapabilities optionsFor(String browser, boolean headless) {
		if (browser.startsWith("firefox")) {
			FirefoxOptions options = new FirefoxOptions();
			if (headless) {
				options.addArguments("-headless");
			}
			return options;
		}
		if (browser.startsWith("edge")) {
			EdgeOptions options = new EdgeOptions();
			if (headless) {
				options.addArguments("--headless=new");
			}
			return options;
		}
		if (browser.startsWith("chrome")) {
			ChromeOptions options = new ChromeOptions();
			if (headless) {
				options.addArguments("--headless=new");
			}
			options.addArguments("--disable-gpu", "--no-sandbox", "--window-size=1440,900");
			return options;
		}
		throw new ConfigurationException(
				"Unsupported browser '" + browser + "'. Expected one of: chrome, firefox, edge.");
	}

	private static WebDriver local(String browser, MutableCapabilities options) {
		if (options instanceof FirefoxOptions firefox) {
			return new FirefoxDriver(firefox);
		}
		if (options instanceof EdgeOptions edge) {
			return new EdgeDriver(edge);
		}
		return new ChromeDriver((ChromeOptions) options);
	}

	private static WebDriver remote(String gridUrl, MutableCapabilities options) {
		try {
			URL url = URI.create(gridUrl).toURL();
			return new RemoteWebDriver(url, options);
		} catch (MalformedURLException e) {
			throw new ConfigurationException("grid.url is not a valid URL: " + gridUrl, e);
		}
	}
}
