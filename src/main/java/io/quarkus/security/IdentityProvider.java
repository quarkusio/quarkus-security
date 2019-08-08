package io.quarkus.security;

/**
 * Marker interface for all identity provider implementations.
 *
 * As the requirements for different providers are often different this interface has purposefully been
 * left empty. Interfaces that extend IdentityProvider should be created for different use cases, which can
 * then be used directly.
 *
 * {@link IdentityProviderManager} should be used to get a specific type of {@link IdentityProvider} for a specific
 * use case.
 */
public interface IdentityProvider {
}
