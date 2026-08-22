package com.vikram.api.specs;

import com.vikram.core.ConfigManager;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
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
 */
public final class SpecFactory {

	private SpecFactory() {
	}

	/** Unauthenticated JSON request against the configured base URI. */
	public static RequestSpecification base() {
		return new RequestSpecBuilder()
				.setBaseUri(ConfigManager.get("api.base.url"))
				.setContentType(ContentType.JSON)
				.addFilter(new RequestLoggingFilter())
				.addFilter(new ResponseLoggingFilter())
				.build();
	}

	/** JSON request carrying the ecommerce bearer token. */
	public static RequestSpecification authenticated(String token) {
		return new RequestSpecBuilder()
				.setBaseUri(ConfigManager.get("api.base.url"))
				.setContentType(ContentType.JSON)
				.addHeader("Authorization", token)
				.addFilter(new RequestLoggingFilter())
				.addFilter(new ResponseLoggingFilter())
				.build();
	}

	/**
	 * Multipart request carrying the bearer token. add-product uploads an image, so it
	 * cannot use the JSON content type the other calls share.
	 */
	public static RequestSpecification authenticatedMultipart(String token) {
		return new RequestSpecBuilder()
				.setBaseUri(ConfigManager.get("api.base.url"))
				.addHeader("Authorization", token)
				.addFilter(new RequestLoggingFilter())
				.addFilter(new ResponseLoggingFilter())
				.build();
	}

	public static ResponseSpecification okJson() {
		return new ResponseSpecBuilder()
				.expectStatusCode(200)
				.expectContentType(ContentType.JSON)
				.build();
	}
}
