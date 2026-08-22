package com.vikram.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Single source of truth for configuration.
 *
 * Resolution order, highest priority first:
 *   1. JVM system property   -Dbrowser=firefox
 *   2. Environment variable  BROWSER=firefox   (dots become underscores, upper-cased)
 *   3. config.properties on the test classpath
 *
 * Credentials are deliberately NOT stored in config.properties. They resolve from the
 * environment only, so nothing secret is ever committed. See .env.example.
 */
public final class ConfigManager {

	private static final Properties PROPS = load();

	private ConfigManager() {
	}

	private static Properties load() {
		Properties props = new Properties();
		try (InputStream in = ConfigManager.class.getClassLoader().getResourceAsStream("config/config.properties")) {
			if (in == null) {
				throw new IllegalStateException("config/config.properties not found on the classpath");
			}
			props.load(in);
		} catch (IOException e) {
			throw new IllegalStateException("Unable to read config/config.properties", e);
		}
		return props;
	}

	/** Returns the value for {@code key}, failing loudly rather than returning null. */
	public static String get(String key) {
		String value = resolve(key);
		if (value == null || value.isBlank()) {
			throw new IllegalStateException("Missing configuration key '" + key
					+ "'. Set it in config/config.properties, as -D" + key + "=..., or as env "
					+ toEnvKey(key) + ".");
		}
		return value.trim();
	}

	public static String get(String key, String defaultValue) {
		String value = resolve(key);
		return (value == null || value.isBlank()) ? defaultValue : value.trim();
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
	}

	public static int getInt(String key, int defaultValue) {
		return Integer.parseInt(get(key, String.valueOf(defaultValue)));
	}

	/**
	 * Reads a required credential from the environment. Kept separate from {@link #get}
	 * so the error message can point at the right fix.
	 */
	public static String getSecret(String envKey) {
		String value = System.getenv(envKey);
		if (value == null || value.isBlank()) {
			value = System.getProperty(envKey);
		}
		if (value == null || value.isBlank()) {
			throw new IllegalStateException("Missing credential '" + envKey
					+ "'. Copy .env.example to .env and export it, or pass -D" + envKey + "=...");
		}
		return value.trim();
	}

	private static String resolve(String key) {
		String fromSystem = System.getProperty(key);
		if (fromSystem != null && !fromSystem.isBlank()) {
			return fromSystem;
		}
		String fromEnv = System.getenv(toEnvKey(key));
		if (fromEnv != null && !fromEnv.isBlank()) {
			return fromEnv;
		}
		return PROPS.getProperty(key);
	}

	private static String toEnvKey(String key) {
		return key.replace('.', '_').toUpperCase();
	}
}
