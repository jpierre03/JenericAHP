package org.taeradan.ahp;

/**
 * @author Jean-Pierre PRUNARET
 * @since 14/07/2014
 */
public final class UsualCheck {

	private UsualCheck() {
	}

	public static void notNullOrFail(Object o, String msg) {

		if (o == null) {
			throw new IllegalArgumentException(msg == null ? "Null object" : msg);
		}
	}

	public static void notNullOrFail(Object o) {
		notNullOrFail(o, null);
	}

	public static void notNullOrFail(String o, String msg) {
		if (o.trim().isEmpty()) {
			throw new IllegalArgumentException("Empty String");
		}
	}

	public static void notNullOrFail(String o) {
		notNullOrFail(o, null);
	}
}
