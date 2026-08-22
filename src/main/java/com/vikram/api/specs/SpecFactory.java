package com.vikram.api.specs;

import com.vikram.core.ConfigManager;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

/**
 * Shared request/response specifications.
 *
 * Base URI, content type and logging are configured once here rather than repeated in
 * every call, so a client method reads as just the request it actually makes.
 *
 * Request/response logging is off by default and enabled with -Dapi.log.requests=true
 * when debugging. Left on, it writes the login payload and the bearer token in plaintext
 * into the console -- and in CI, into a retained build log.
 */
public final class SpecFactory {

	/** Even with logging on, the bearer token is never printed. */
	private static final RestAssuredConfig CONFIG = RestAssuredConfig.config()
			.logConfig(LogConfig.logConfig().blacklistHeader("Authorization"));

	private SpecFactory() {
	}

	private static boolean loggingEnabled() {
		return ConfigManager.getBoolean("api.log.requests", false);
	}

	private static RequestSpecBuilder builder() {
		RequestSpecBuilder builder = new RequestSpecBuilder()
				.setBaseUri(ConfigManager.get("api.base.url"))
				.setConfig(CONFIG);
		if (loggingEnabled()) {
			builder.addFilter(new RequestLoggingFilter());
			builder.addFilter(new ResponseLoggingFilter());
		}
		return builder;
	}

	/** Unauthenticated JSON request against the configured base URI. */
	public static RequestSpecification base() {
		return builder().setContentType(ContentType.JSON).build();
	}

	/**
	 * Request for the login call. Never logs, whatever api.log.requests says: the body
	 * carries the account password and the response carries the token.
	 */
	public static RequestSpecification credentials() {
		return new RequestSpecBuilder()
				.setBaseUri(ConfigManager.get("api.base.url"))
				.setConfig(CONFIG)
				.setContentType(ContentType.JSON)
				.build();
	}

	/** JSON request carrying the ecommerce bearer token. */
	public static RequestSpecification authenticated(String token) {
		return builder()
				.setContentType(ContentType.JSON)
				.addHeader("Authorization", token)
				.build();
	}

	/**
	 * Multipart request carrying the bearer token. add-product uploads an image, so it
	 * cannot use the JSON content type the other calls share.
	 */
	public static RequestSpecification authenticatedMultipart(String token) {
		return builder()
				.addHeader("Authorization", token)
				.build();
	}

	public static ResponseSpecification okJson() {
		return new ResponseSpecBuilder()
				.expectStatusCode(200)
				.expectContentType(ContentType.JSON)
				.build();
	}
}
