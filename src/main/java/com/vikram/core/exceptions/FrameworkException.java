package com.vikram.core.exceptions;

/**
 * Signals a fault in the framework or its configuration rather than a failure of the
 * application under test.
 *
 * The distinction matters when triaging a red build: a FrameworkException means the
 * harness is wrong -- a missing key, an unreadable fixture, an unknown environment -- and
 * no assertion ever ran. An AssertionError means the application misbehaved.
 */
public class FrameworkException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FrameworkException(String message) {
		super(message);
	}

	public FrameworkException(String message, Throwable cause) {
		super(message, cause);
	}
}
