package com.vikram.core.users;

import java.util.EnumMap;
import java.util.Map;

import com.vikram.core.ConfigManager;

/**
 * Resolves credentials for a role.
 *
 * The env-variable names live here and nowhere else: previously every test repeated the
 * literals "ECOM_USER_EMAIL" and "ECOM_USER_PASSWORD", so renaming one meant editing
 * seven files and a typo produced a runtime failure in only some of them.
 *
 * Credentials are read from the process environment only -- never from a checked-in file
 * -- and cached per role so a suite does not re-read them for every test.
 */
public final class UserManager {

	private static final Map<UserRole, User> CACHE = new EnumMap<>(UserRole.class);

	private UserManager() {
	}

	public static synchronized User get(UserRole role) {
		return CACHE.computeIfAbsent(role, r -> new User(
				r,
				ConfigManager.getSecret(r.emailKey()),
				ConfigManager.getSecret(r.passwordKey())));
	}

	/** The role the suites run as unless a test says otherwise. */
	public static User standardCustomer() {
		return get(UserRole.STANDARD_CUSTOMER);
	}

	/** A real account with a deliberately wrong password, for negative login paths. */
	public static User withWrongPassword(UserRole role) {
		return new User(role, get(role).email(), "DefinitelyWrong@123?");
	}
}
