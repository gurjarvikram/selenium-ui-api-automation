package com.vikram.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Reads test-data fixtures from the test classpath. */
public final class JsonUtils {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private JsonUtils() {
	}

	/**
	 * Loads a fixture as a list of key/value rows, ready to hand to a TestNG DataProvider.
	 * Resolved from the classpath rather than user.dir so it works the same from an IDE,
	 * from Maven, and in CI.
	 */
	public static List<HashMap<String, String>> readAsMapList(String classpathResource) {
		try (InputStream in = JsonUtils.class.getClassLoader().getResourceAsStream(classpathResource)) {
			if (in == null) {
				throw new IllegalArgumentException("Test data not found on classpath: " + classpathResource);
			}
			return MAPPER.readValue(in, new TypeReference<List<HashMap<String, String>>>() {
			});
		} catch (IOException e) {
			throw new IllegalStateException("Unable to parse test data: " + classpathResource, e);
		}
	}
}
