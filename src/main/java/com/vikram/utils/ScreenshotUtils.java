package com.vikram.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/** Captures failure screenshots into the report directory. */
public final class ScreenshotUtils {

	private static final Path SCREENSHOT_DIR = Paths.get("target", "reports", "screenshots");

	private ScreenshotUtils() {
	}

	/** Returns the absolute path of the saved image, for embedding in the Extent report. */
	public static String capture(WebDriver driver, String testName) throws IOException {
		Files.createDirectories(SCREENSHOT_DIR);
		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Path destination = SCREENSHOT_DIR.resolve(testName + ".png");
		Files.copy(source.toPath(), destination, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		return destination.toAbsolutePath().toString();
	}
}
