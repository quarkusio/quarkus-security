package io.quarkus.security.identity;

import io.quarkus.security.identity.request.AuthenticationRequest;
import io.smallrye.mutiny.Uni;

/**
 * A manager that can be used to get a specific type of identity provider.
 */
public interface IdentityProviderManager {

    /**
     * Attempts to create an authenticated identity for the provided {@link AuthenticationRequest}.
     * <p>
     * If authentication succeeds the resulting identity will be augmented with any configured {@link SecurityIdentityAugmentor}
     * instances that have been registered.
     *
     * @param request The authentication request
     * @return The first identity provider that was registered with this type
     */
    Uni<SecurityIdentity> authenticate(AuthenticationRequest request);

    /**
     * Attempts to create an authenticated identity for the provided {@link AuthenticationRequest} in a blocking manner
     * <p>
     * If authentication succeeds the resulting identity will be augmented with any configured {@link SecurityIdentityAugmentor}
     * instances that have been registered.
     *
     * @param request The authentication request
     * @return The first identity provider that was registered with this type
     */
    SecurityIdentity authenticateBlocking(AuthenticationRequest request);

}
