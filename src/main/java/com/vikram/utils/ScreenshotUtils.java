package com.vikram.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/** Captures failure screenshots into the report directory. */
public final class ScreenshotUtils {

	private static final Path REPORT_DIR = Paths.get("target", "reports");
	private static final String SCREENSHOT_FOLDER = "screenshots";
	private static final AtomicInteger SEQUENCE = new AtomicInteger();

	private ScreenshotUtils() {
	}

	/**
	 * Saves a screenshot and returns its path <em>relative to the report</em>.
	 *
	 * Relative, because Extent embeds whatever string it is given straight into the HTML.
	 * An absolute path resolves on the machine that produced it and nowhere else, so
	 * every image in a downloaded CI artifact would be broken.
	 *
	 * The name carries a sequence number as well as the test name: a DataProvider runs
	 * one method many times, and without it the second failing row would overwrite the
	 * first row's evidence.
	 */
	public static String capture(WebDriver driver, String testName) throws IOException {
		Path folder = REPORT_DIR.resolve(SCREENSHOT_FOLDER);
		Files.createDirectories(folder);

		String fileName = sanitise(testName) + "-" + SEQUENCE.incrementAndGet() + ".png";
		Path source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath();
		Files.copy(source, folder.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

		return SCREENSHOT_FOLDER + "/" + fileName;
	}

	/** Test names can contain data-provider values, which are not safe as file names. */
	private static String sanitise(String testName) {
		String cleaned = testName.replaceAll("[^A-Za-z0-9_.-]", "_").toLowerCase(Locale.ROOT);
		return cleaned.length() > 80 ? cleaned.substring(0, 80) : cleaned;
	}
}
