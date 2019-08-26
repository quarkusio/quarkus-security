package io.quarkus.security.identity.request;

import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;

/**
 * A marker interface that represents a request for an authenticated identity.
 *
 * Different {@link IdentityProvider} implementations will be able to handle different
 * types of request.
 *
 * This approach of using a marker interface allows for maximum flexibility for the
 * providers, while still allowing for a single API to get an authenticated
 * {@link SecurityIdentity}.
 *
 * Note that identity providers can only handle a single request type, and when a
 * request type is registered with the {@link IdentityProviderManager} inheritance
 * is not taken into account.
 */
public interface AuthenticationRequest {
}
