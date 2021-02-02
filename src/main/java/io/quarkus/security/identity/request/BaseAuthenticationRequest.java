package io.quarkus.security.identity.request;

import java.util.HashMap;
import java.util.Map;

/**
 * Base implementation of the {@link AuthenticationRequest} interface for convenience.
 */
public abstract class BaseAuthenticationRequest implements AuthenticationRequest {

    private Map<String, Object> attributes;

    private Map<String, Object> attributes() {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        return attributes;
    }

    @Override
    public <T> T getAttribute(String name) {
        return attributes != null ? ((T) attributes.get(name)) : null;
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes().put(name, value);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes();
    }
}
