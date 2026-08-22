package com.vikram.base;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
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

	protected LandingPage landingPage;
	protected SessionManager session;

	protected WebDriver driver() {
		return DriverManager.get();
	}

	@BeforeMethod(alwaysRun = true)
	public void launchApplication() {
		DriverManager.set(DriverFactory.create());
		landingPage = new LandingPage(driver());
		session = new SessionManager(driver());
		landingPage.goTo();
	}

	@AfterMethod(alwaysRun = true)
	public void quitBrowser() {
		DriverManager.quit();
	}

	protected List<HashMap<String, String>> readTestData(String classpathResource) {
		return JsonUtils.readAsMapList(classpathResource);
	}
}
