package io.quarkus.security;

import io.quarkus.security.identity.IdentityProvider;

/**
 * An exception that should be thrown (or otherwise returned to the client for async implementations)
 * by {@link IdentityProvider} implementations if the authentication failed.
 * <p>
 * This can be used by a mechanism to determine if an authentication failure was due to bad credentials vs
 * some other form of internal failure.
 * <p>
 * This class does not allow for any message or other context to be passed by design to prevent information leakage
 * from the identity provider.
 */
public final class AuthenticationFailedException extends RuntimeException {
}
