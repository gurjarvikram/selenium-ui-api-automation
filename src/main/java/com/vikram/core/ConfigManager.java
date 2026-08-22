package com.vikram.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vikram.core.exceptions.ConfigurationException;

/**
 * Single source of truth for configuration.
 *
 * Values are layered. Shared defaults live in config.properties; anything that differs
 * per deployment lives in env-{name}.properties and overrides them. On top of that:
 *
 *   1. JVM system property   -Dbrowser=firefox
 *   2. Environment variable  BROWSER=firefox   (dots become underscores, upper-cased)
 *   3. env-{name}.properties selected by -Denv=
 *   4. config.properties
 *
 * Credentials are deliberately absent from all of these files and resolve from the
 * process environment only, so nothing secret is ever committed. See .env.example.
 */
public final class ConfigManager {

	private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);

	/** Environments with a checked-in profile. Naming anything else is a hard error. */
	private static final Set<String> KNOWN_ENVIRONMENTS = new TreeSet<>(Set.of("demo"));

	private static final String ENVIRONMENT = resolveEnvironment();
	private static final Properties PROPS = load();

	private ConfigManager() {
	}

	public static String environment() {
		return ENVIRONMENT;
	}

	private static String resolveEnvironment() {
		String name = System.getProperty("env");
		if (name == null || name.isBlank()) {
			name = System.getenv("ENV");
		}
		if (name == null || name.isBlank()) {
			name = "demo";
		}
		name = name.trim().toLowerCase();

		// Fail on an unknown name rather than silently falling back: a typo in a CI
		// variable should stop the build, not quietly run against the wrong target.
		if (!KNOWN_ENVIRONMENTS.contains(name)) {
			throw new ConfigurationException("Unknown environment '" + name + "'. Known environments: "
					+ String.join(", ", KNOWN_ENVIRONMENTS)
					+ ". Add src/test/resources/config/env-" + name + ".properties and register it"
					+ " in ConfigManager.KNOWN_ENVIRONMENTS to introduce a new one.");
		}
		return name;
	}

	private static Properties load() {
		Properties defaults = read("config/config.properties", true);
		Properties environment = read("config/env-" + ENVIRONMENT + ".properties", true);

		Properties merged = new Properties();
		merged.putAll(defaults);
		merged.putAll(environment);

		log.info("Configuration loaded for environment '{}'", ENVIRONMENT);
		return merged;
	}

	private static Properties read(String resource, boolean required) {
		Properties props = new Properties();
		try (InputStream in = ConfigManager.class.getClassLoader().getResourceAsStream(resource)) {
			if (in == null) {
				if (required) {
					throw new ConfigurationException("Required config file not on the classpath: " + resource);
				}
				return props;
			}
			props.load(in);
		} catch (IOException e) {
			throw new ConfigurationException("Unable to read " + resource, e);
		}
		return props;
	}

	/** Returns the value for {@code key}, failing loudly rather than returning null. */
	public static String get(String key) {
		String value = resolve(key);
		if (value == null || value.isBlank()) {
			throw new ConfigurationException("Missing configuration key '" + key
					+ "'. Set it in config/config.properties or config/env-" + ENVIRONMENT
					+ ".properties, as -D" + key + "=..., or as env " + toEnvKey(key) + ".");
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
		String raw = get(key, String.valueOf(defaultValue));
		try {
			return Integer.parseInt(raw);
		} catch (NumberFormatException e) {
			throw new ConfigurationException("Configuration key '" + key + "' must be a number, got '" + raw + "'", e);
		}
	}

	/**
	 * Reads a required credential from the process environment. Kept separate from
	 * {@link #get} so the error message can point at the right fix, and so credentials
	 * never resolve from a checked-in file by accident.
	 */
	public static String getSecret(String envKey) {
		String value = System.getenv(envKey);
		if (value == null || value.isBlank()) {
			value = System.getProperty(envKey);
		}
		if (value == null || value.isBlank()) {
			throw new ConfigurationException("Missing credential '" + envKey
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
