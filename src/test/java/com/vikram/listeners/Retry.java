package com.vikram.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.vikram.core.ConfigManager;

/**
 * Retries a failed test up to retry.count times.
 *
 * Kept deliberately low: retries are for genuine environment flakiness, and a high limit
 * turns a real regression into an intermittent one that nobody investigates. Every retry
 * is logged so the rate stays visible rather than hiding failures.
 */
public class Retry implements IRetryAnalyzer {

	private static final Logger log = LoggerFactory.getLogger(Retry.class);
	private static final int MAX_RETRIES = ConfigManager.getInt("retry.count", 1);

	private int attempts = 0;

	@Override
	public boolean retry(ITestResult result) {
		if (attempts < MAX_RETRIES) {
			attempts++;
			log.warn("Retrying {}.{} (attempt {} of {}) after: {}",
					result.getTestClass().getName(), result.getMethod().getMethodName(),
					attempts, MAX_RETRIES,
					result.getThrowable() == null ? "unknown" : result.getThrowable().toString());
			return true;
		}
		return false;
	}
}
