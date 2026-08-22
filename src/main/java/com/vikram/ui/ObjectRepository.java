package com.vikram.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.By;

import com.vikram.core.exceptions.ConfigurationException;

/**
 * Locators, kept out of the page objects.
 *
 * Each page has a properties file under objectrepository/, holding entries of the form
 * {@code name = strategy:value}. A markup change is then a one-line edit in a data file
 * that someone can make without touching Java, and every locator for a page is visible in
 * one place rather than scattered through its methods.
 *
 * Files are parsed once and cached; an unknown page or key fails immediately with the
 * available names, so a typo surfaces at startup rather than as a timeout later.
 */
public final class ObjectRepository {

	private static final Map<String, Properties> CACHE = new ConcurrentHashMap<>();

	private ObjectRepository() {
	}

	public static By by(String page, String key) {
		return by(page, key, new Object[0]);
	}

	/**
	 * Resolves a locator, substituting {@code args} into its %s placeholders.
	 *
	 * Lets one entry address a specific row or card -- "the add-to-cart button of the
	 * card titled X" -- so the click targets a single element rather than being resolved
	 * by walking a list in Java.
	 */
	public static By by(String page, String key, Object... args) {
		Properties locators = load(page);
		String raw = locators.getProperty(key);
		if (raw == null || raw.isBlank()) {
			throw new ConfigurationException("No locator '" + key + "' in objectrepository/" + page
					+ ".properties. Available: " + new java.util.TreeSet<>(locators.stringPropertyNames()));
		}
		String resolved = (args == null || args.length == 0) ? raw.trim() : String.format(raw.trim(), args);
		return parse(page, key, resolved);
	}

	private static Properties load(String page) {
		return CACHE.computeIfAbsent(page, name -> {
			String resource = "objectrepository/" + name + ".properties";
			Properties props = new Properties();
			try (InputStream in = ObjectRepository.class.getClassLoader().getResourceAsStream(resource)) {
				if (in == null) {
					throw new ConfigurationException("Object repository not found: " + resource);
				}
				props.load(in);
			} catch (IOException e) {
				throw new ConfigurationException("Unable to read " + resource, e);
			}
			return props;
		});
	}

	private static By parse(String page, String key, String raw) {
		int separator = raw.indexOf(':');
		if (separator < 1) {
			throw new ConfigurationException("Locator '" + page + "." + key + "' must be 'strategy:value', got: " + raw);
		}
		String strategy = raw.substring(0, separator).trim().toLowerCase();
		String value = raw.substring(separator + 1).trim();

		return switch (strategy) {
			case "id" -> By.id(value);
			case "name" -> By.name(value);
			case "css" -> By.cssSelector(value);
			case "xpath" -> By.xpath(value);
			case "class" -> By.className(value);
			case "link" -> By.linkText(value);
			case "partiallink" -> By.partialLinkText(value);
			case "tag" -> By.tagName(value);
			default -> throw new ConfigurationException("Unknown locator strategy '" + strategy + "' for "
					+ page + "." + key + ". Supported: id, name, css, xpath, class, link, partialLink, tag.");
		};
	}
}
