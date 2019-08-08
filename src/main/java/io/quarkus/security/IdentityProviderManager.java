package io.quarkus.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A manager that can be used to get a specific type of identity provider.
 */
public class IdentityProviderManager {

    private final Map<Class<? extends IdentityProvider>, List<IdentityProvider>> providers;

    IdentityProviderManager(Builder builder) {
        this.providers = builder.providers;
    }

    /**
     * Gets an {@link IdentityProvider} implementation of a specific type
     *
     * @param type The type of identity provider
     * @param <T>  The type of identity provider
     * @return The first identity provider that was registered with this type
     */
    public <T extends IdentityProvider> T getIdentityProvider(Class<T> type) {
        if (!type.isInterface()) {
            throw new IllegalArgumentException("IdentityProviderManager only supports interface types. All identity provider implementation must support a more specific IdentityProvider type.");
        }
        List<IdentityProvider> providers = this.providers.getOrDefault(type, Collections.emptyList());
        return providers.isEmpty() ? null : (T) providers.get(0);
    }


    /**
     * Gets all {@link IdentityProvider} implementations of a specific type. If none
     * are registered this method will return an empty list.
     *
     * @param type The type of identity provider
     * @param <T>  The type of identity provider
     * @return The first identity provider that was registered with this type, or an empty list
     */
    public <T extends IdentityProvider> List<T> getIdentityProviders(Class<T> type) {
        if (!type.isInterface()) {
            throw new IllegalArgumentException("IdentityProviderManager only supports interface types. All identity provider implementation must support a more specific IdentityProvider type.");
        }
        return (List<T>) this.providers.getOrDefault(type, Collections.emptyList());
    }

    /**
     * Creates a builder for constructing instances of {@link IdentityProviderManager}
     *
     * @return A builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for constructing instances of {@link IdentityProviderManager}
     */
    public static class Builder {

        Builder() {
        }

        private Map<Class<? extends IdentityProvider>, List<IdentityProvider>> providers = new HashMap<>();
        private boolean built = false;

        /**
         * Adds an {@link IdentityProvider} implementation to this manager
         *
         * @param provider The provider
         * @return this builder
         */
        public Builder addProvider(IdentityProvider provider) {
            if (built) {
                throw new IllegalStateException("manager has already been built");
            }
            boolean found = false;
            for (Class<?> i : provider.getClass().getInterfaces()) {
                if (i.isAssignableFrom(IdentityProvider.class) && i != IdentityProvider.class) {
                    found = true;
                    providers.computeIfAbsent((Class<? extends IdentityProvider>) i, (a) -> new ArrayList<>()).add(provider);
                }
            }
            if (!found) {
                throw new IllegalArgumentException("IdentityProvider implementation must extend a more specific sub-interface of IdentityProvider");
            }
            return this;
        }

        /**
         * @return a new {@link IdentityProviderManager}
         */
        public IdentityProviderManager build() {
            built = true;
            return new IdentityProviderManager(this);
        }
    }
}
