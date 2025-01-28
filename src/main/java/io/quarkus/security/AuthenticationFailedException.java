package io.quarkus.security;

import io.quarkus.security.identity.IdentityProvider;

import java.util.Map;

/**
 * An exception that should be thrown (or otherwise returned to the client for
 * async implementations) by {@link IdentityProvider} implementations if the
 * authentication failed.
 * <p>
 * This can be used by a mechanism to determine if an authentication failure was
 * due to bad credentials vs some other form of internal failure.
 */
public final class AuthenticationFailedException extends SecurityException implements AuthenticationException {

    private final Map<String, Object> attributes;

    public AuthenticationFailedException() {
        this(null, null, null);
    }

    public AuthenticationFailedException(Map<String, Object> attributes) {
        this(null, null, attributes);
    }

    public AuthenticationFailedException(String errorMessage) {
        this(errorMessage, null, null);
    }

    public AuthenticationFailedException(String errorMessage, Map<String, Object> attributes) {
        this(errorMessage, null, attributes);
    }

    public AuthenticationFailedException(Throwable cause) {
        this(null, cause, null);
    }

    public AuthenticationFailedException(Throwable cause, Map<String, Object> attributes) {
        this(null, cause, attributes);
    }

    public AuthenticationFailedException(String errorMessage, Throwable cause) {
        this(errorMessage, cause, null);
    }

    public AuthenticationFailedException(String errorMessage, Throwable cause, Map<String, Object> attributes) {
        super(errorMessage, cause);
        this.attributes = attributes == null || attributes.isEmpty() ? Map.of() : Map.copyOf(attributes);
    }

    /**
     * Provides an attribute that allows to better understand the cause of the authentication failure.
     *
     * @param name The authentication failure attribute name
     * @return The authentication attribute value or null if the attribute with this name does not exist
     * @param <T> The type of the authentication failure attribute
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String name) {
        return (T) attributes.get(name);
    }

    /**
     * Provides attributes that allow to better understand the cause of the authentication failure.
     *
     * @return authentication failure attributes; never null
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
