package com.vikram.listeners;

import com.vikram.core.ConfigManager;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries a failed test up to retry.count times.
 *
 * Kept deliberately low: retries are for genuine environment flakiness, and a high limit
 * turns a real regression into an intermittent one that nobody investigates.
 */
public class Retry implements IRetryAnalyzer {

	private static final int MAX_RETRIES = ConfigManager.getInt("retry.count", 1);

	private int attempts = 0;

	@Override
	public boolean retry(ITestResult result) {
		if (attempts < MAX_RETRIES) {
			attempts++;
			return true;
		}
		return false;
	}
}
