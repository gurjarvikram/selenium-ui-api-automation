package com.vikram.ui;

import com.vikram.core.ConfigManager;

/**
 * Builds absolute URLs from the configured base and a named route.
 *
 * Joining is done here rather than at each call site so a base URL with or without a
 * trailing slash produces the same result either way.
 */
public final class Routes {

	private Routes() {
	}

	public static String baseUrl() {
		return ConfigManager.get("ui.base.url");
	}

	public static String url(UiRoute route) {
		return join(baseUrl(), route.path());
	}

	private static String join(String base, String path) {
		if (path == null || path.isBlank()) {
			return base;
		}
		String left = base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
		String right = path.startsWith("/") ? path.substring(1) : path;
		return left + "/" + right;
	}
}
