package io.quarkus.security;

import io.quarkus.security.identity.IdentityProvider;

/**
 * An exception that should be thrown (or otherwise returned to the client for
 * async implementations) by {@link IdentityProvider} implementations if the
 * authentication failed.
 * <p>
 * This can be used by a mechanism to determine if an authentication failure was
 * due to bad credentials vs some other form of internal failure.
 */
public final class AuthenticationFailedException extends SecurityException {
    public AuthenticationFailedException() {

    }

    public AuthenticationFailedException(String errorMessage) {
        this(errorMessage, null);
    }

    public AuthenticationFailedException(Throwable cause) {
        this(null, cause);
    }

    public AuthenticationFailedException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
