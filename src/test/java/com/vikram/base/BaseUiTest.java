package com.vikram.base;

import java.util.Map;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.vikram.core.DriverFactory;
import com.vikram.core.DriverManager;
import com.vikram.ui.pages.LandingPage;
import com.vikram.ui.pages.SessionManager;
import com.vikram.utils.JsonUtils;

/**
 * Per-test browser lifecycle.
 *
 * The driver is held in {@link DriverManager} rather than on this class, so classes can
 * run in parallel without sharing a browser.
 */
public class BaseUiTest {

	protected static final Logger log = LoggerFactory.getLogger(BaseUiTest.class);

	protected LandingPage landingPage;
	protected SessionManager session;

	protected WebDriver driver() {
		return DriverManager.get();
	}

	/**
	 * Starts a browser on the login screen, retrying once if the first page load times out.
	 *
	 * The retry lives here rather than in {@link com.vikram.listeners.Retry} because TestNG
	 * only applies a retry analyser to @Test methods: a slow application host fails this
	 * configuration method instead, which reds the whole class with no second attempt. The
	 * retry starts a fresh browser, since a renderer that stopped responding usually stays
	 * wedged and reusing the session would only burn a second page-load timeout.
	 */
	@BeforeMethod(alwaysRun = true)
	public void launchApplication() {
		try {
			startBrowser();
			landingPage.goTo();
		} catch (TimeoutException first) {
			log.warn("Page load timed out opening the application; retrying once on a fresh browser: {}",
					first.getMessage());
			discardBrowser();
			startBrowser();
			landingPage.goTo();
		}
	}

	private void startBrowser() {
		DriverManager.set(DriverFactory.create());
		landingPage = new LandingPage(driver());
		session = new SessionManager(driver());
	}

	/** Quits the timed-out browser. A wedged session can fail to quit, which must not mask the retry. */
	private void discardBrowser() {
		try {
			DriverManager.quit();
		} catch (WebDriverException e) {
			log.warn("Could not quit the timed-out browser cleanly: {}", e.getMessage());
		}
	}

	@AfterMethod(alwaysRun = true)
	public void quitBrowser() {
		DriverManager.quit();
	}

	/** Loads one role from a role-keyed fixture. */
	protected Map<String, String> testData(String classpathResource, String role) {
		return JsonUtils.readRole(classpathResource, role);
	}
}
