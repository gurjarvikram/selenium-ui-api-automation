package com.vikram.ui;

/**
 * Application routes, relative to ui.base.url.
 *
 * Deep links were previously built by string concatenation at the point of use, which
 * put "dashboard/dash" inside a page object. Naming them here means a route change is a
 * one-line edit and the set of reachable pages is discoverable.
 */
public enum UiRoute {

	LOGIN(""),
	DASHBOARD("dashboard/dash"),
	CART("dashboard/cart"),
	ORDERS("dashboard/myorders");

	private final String path;

	UiRoute(String path) {
		this.path = path;
	}

	public String path() {
		return path;
	}

	/** Absolute URL for this route against the configured environment. */
	public String url() {
		return Routes.url(this);
	}
}
