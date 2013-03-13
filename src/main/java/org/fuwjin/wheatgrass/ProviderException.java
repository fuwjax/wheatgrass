package org.fuwjin.wheatgrass;

import javax.inject.Provider;

/**
 * Exception thrown within a {@link Provider#get()} if the object cannot be
 * created or retrieved.
 * 
 * @author fuwjax
 * 
 */
public class ProviderException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exception.
	 * @param message the message
	 * @param cause the cause of this exception
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public ProviderException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new exception.
	 * @param message the message
	 * @see RuntimeException#RuntimeException(String)
	 */
	public ProviderException(String message) {
		super(message);
	}
}
