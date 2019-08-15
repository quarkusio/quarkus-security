package io.quarkus.security.identity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import org.jboss.logging.Logger;

import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.identity.request.AnonymousAuthenticationRequest;
import io.quarkus.security.identity.request.AuthenticationRequest;

/**
 * A manager that can be used to get a specific type of identity provider.
 */
public class IdentityProviderManager {
    private static final Logger log = Logger.getLogger(IdentityProviderManager.class);

    private final Map<Class<? extends AuthenticationRequest>, List<IdentityProvider>> providers;
    private final List<SecurityIdentityAugmentor> augmenters;

    IdentityProviderManager(Builder builder) {
        this.providers = builder.providers;
        this.augmenters = builder.augmenters;
    }

    /**
     * Attempts to create an authenticated identity for the provided {@link AuthenticationRequest}.
     * <p>
     * If authentication succeeds the resulting identity will be augmented with any configured {@link SecurityIdentityAugmentor}
     * instances that have been registered.
     *
     * @param request The authentication request
     * @return The first identity provider that was registered with this type
     */
    public CompletionStage<SecurityIdentity> authenticate(AuthenticationRequest request) {
        List<IdentityProvider> providers = this.providers.get(request.getClass());
        if (providers == null) {
            CompletableFuture<SecurityIdentity> cf = new CompletableFuture<>();
            cf.completeExceptionally(new IllegalArgumentException("No IdentityProviders were registered to handle AuthenticationRequest " + request));
            return cf;
        }
        return handleProvider(0, (List) providers, request);
    }

    private <T extends AuthenticationRequest> CompletionStage<SecurityIdentity> handleProvider(int pos, List<IdentityProvider<T>> providers, T request) {
        if (pos == providers.size()) {
            //we failed to authentication
            log.debugf("Authentication failed as providers would authenticate the request");
            CompletableFuture<SecurityIdentity> cf = new CompletableFuture<>();
            cf.completeExceptionally(new AuthenticationFailedException());
            return cf;
        }
        IdentityProvider<T> current = providers.get(pos);
        CompletionStage<SecurityIdentity> cs = current.authenticate(request).thenCompose(new Function<SecurityIdentity, CompletionStage<SecurityIdentity>>() {
            @Override
            public CompletionStage<SecurityIdentity> apply(SecurityIdentity identity) {
                if (identity != null) {
                    return CompletableFuture.completedFuture(identity);
                }
                return handleProvider(pos + 1, providers, request);
            }
        });
        return cs.thenCompose(new Function<SecurityIdentity, CompletionStage<SecurityIdentity>>() {
            @Override
            public CompletionStage<SecurityIdentity> apply(SecurityIdentity identity) {
                return handleIdentityFromProvider(0, identity);
            }
        });
    }

    private CompletionStage<SecurityIdentity> handleIdentityFromProvider(int pos, SecurityIdentity identity) {
        if (pos == augmenters.size()) {
            return CompletableFuture.completedFuture(identity);
        }
        SecurityIdentityAugmentor a = augmenters.get(pos);
        return a.augment(identity).thenCompose(new Function<SecurityIdentity, CompletionStage<SecurityIdentity>>() {
            @Override
            public CompletionStage<SecurityIdentity> apply(SecurityIdentity identity) {
                return handleIdentityFromProvider(pos + 1, identity);
            }
        });
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

        private final Map<Class<? extends AuthenticationRequest>, List<IdentityProvider>> providers = new HashMap<>();
        private final List<SecurityIdentityAugmentor> augmenters = new ArrayList<>();
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
            providers.computeIfAbsent(provider.getRequestType(), (a) -> new ArrayList<>()).add(provider);
            return this;
        }

        /**
         * Adds an augmentor that can modify the security identity that is provided by the identity store.
         *
         * @param augmenter The augmentor
         * @return this builder
         */
        public Builder addSecurityIdentityAugmenter(SecurityIdentityAugmentor augmenter) {
            augmenters.add(augmenter);
            return this;
        }

        /**
         * @return a new {@link IdentityProviderManager}
         */
        public IdentityProviderManager build() {
            built = true;
            if (!providers.containsKey(AnonymousAuthenticationRequest.class)) {
                throw new IllegalStateException("No AnonymousIdentityProvider registered. An instance of AnonymousIdentityProvider must be provided to allow the Anonymous identity to be created.");
            }
            augmenters.sort(new Comparator<SecurityIdentityAugmentor>() {
                @Override
                public int compare(SecurityIdentityAugmentor o1, SecurityIdentityAugmentor o2) {
                    return Integer.compare(o2.priority(), o1.priority());
                }
            });
            return new IdentityProviderManager(this);
        }
    }
}
