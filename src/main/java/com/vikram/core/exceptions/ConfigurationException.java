package com.vikram.core.exceptions;

/** A required setting is missing, malformed, or names something that does not exist. */
public class ConfigurationException extends FrameworkException {

	private static final long serialVersionUID = 1L;

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
}
