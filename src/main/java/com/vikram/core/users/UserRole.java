package com.vikram.core.users;

/**
 * Roles the suites can authenticate as.
 *
 * Each role maps to a pair of environment variables, {prefix}_EMAIL and
 * {prefix}_PASSWORD. Adding a role is one enum constant plus two variables in the
 * environment -- no code in the tests changes, and no credential enters the repository.
 */
public enum UserRole {

	/** The standard shopper the suites run as. */
	STANDARD_CUSTOMER("ECOM_USER");

	private final String envPrefix;

	UserRole(String envPrefix) {
		this.envPrefix = envPrefix;
	}

	public String emailKey() {
		return envPrefix + "_EMAIL";
	}

	public String passwordKey() {
		return envPrefix + "_PASSWORD";
	}
}
