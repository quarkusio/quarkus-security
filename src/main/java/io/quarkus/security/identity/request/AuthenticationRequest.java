package io.quarkus.security.identity.request;

import java.util.Map;

import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;

/**
 * Represents a request for an authenticated identity.
 * <p>
 * Different {@link IdentityProvider} implementations will be able to handle different
 * types of request.
 * <p>
 * This approach of using a marker interface allows for maximum flexibility for the
 * providers, while still allowing for a single API to get an authenticated
 * {@link SecurityIdentity}.
 * <p>
 * <p>
 * Attributes can be used to transport additional context information with the request such as context path,
 * http header or query parameter values. Attributes may also be enriched or verified by a central component before the request
 * arrives at the {@link IdentityProvider}.
 * </p>
 * Note that identity providers can only handle a single request type, and when a
 * request type is registered with the {@link IdentityProviderManager} inheritance
 * is not taken into account.
 */
public interface AuthenticationRequest {

    /**
     * Gets an attribute from the authentication request.
     * <p>
     * These can be arbitrary, and extensions are encouraged to use name spaced attribute names in a similar
     * manner to package names.
     * <p>
     * The `quarkus.` namespace is reserved
     * <p>
     *
     * @param name The attribute name
     * @param <T>  The type of the attribute
     * @return The attribute value
     */
    <T> T getAttribute(String name);

    /**
     * Sets an attribute on the authentication request.
     * <p>
     * These can be arbitrary, and extensions are encouraged to use name spaced attribute names in a similar
     * manner to package names.
     * <p>
     * The `quarkus.` namespace is reserved
     * <p>
     *
     * @param name The attribute name
     * @param value The attribute value
     */
    void setAttribute(String name, Object value);

    /**
     * @return All the authentication request attributes. Modifications on the returned map will affect the authentication
     * request attributes.
     */
    Map<String, Object> getAttributes();
}
