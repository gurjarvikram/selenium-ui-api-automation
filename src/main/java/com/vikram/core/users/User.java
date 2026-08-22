package com.vikram.core.users;

/**
 * A set of credentials.
 *
 * toString masks the password deliberately. Test names, assertion messages, retry logs
 * and Extent output all stringify whatever they are handed, and a record's generated
 * toString would print the password in every one of them.
 */
public record User(UserRole role, String email, String password) {

	@Override
	public String toString() {
		return "User[" + role + ", " + email + ", password=********]";
	}
}
