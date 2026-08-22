package com.vikram.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vikram.core.exceptions.FrameworkException;

/**
 * Reads test-data fixtures from the test classpath.
 *
 * Fixtures are keyed by role rather than being a positional array. A test then asks for
 * "standardCustomer" instead of row 0, so reordering or adding a case cannot silently
 * repoint an existing test at different data.
 */
public final class JsonUtils {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private JsonUtils() {
	}

	/** Loads a role-keyed fixture: role name to its field map. */
	public static Map<String, Map<String, String>> readRoles(String classpathResource) {
		try (InputStream in = JsonUtils.class.getClassLoader().getResourceAsStream(classpathResource)) {
			if (in == null) {
				throw new FrameworkException("Test data not found on classpath: " + classpathResource);
			}
			return MAPPER.readValue(in, new TypeReference<LinkedHashMap<String, Map<String, String>>>() {
			});
		} catch (IOException e) {
			throw new FrameworkException("Unable to parse test data: " + classpathResource, e);
		}
	}

	/** Loads one role, failing with the available role names when it is absent. */
	public static Map<String, String> readRole(String classpathResource, String role) {
		Map<String, Map<String, String>> roles = readRoles(classpathResource);
		Map<String, String> data = roles.get(role);
		if (data == null) {
			throw new FrameworkException("No role '" + role + "' in " + classpathResource
					+ ". Available roles: " + roles.keySet());
		}
		return data;
	}
}
