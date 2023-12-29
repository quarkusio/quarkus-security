package io.quarkus.security.identity;

import io.smallrye.mutiny.Uni;

import java.util.Map;

/**
 * An interface that allows for a {@link SecurityIdentity} to be modified after creation.
 * <p>
 * Implementations of this interface should be CDI beans. At run time all CDI beans that implement this interface
 * will be used to augment the {@link SecurityIdentity}, with the order determined via the {@link #priority()} field.
 * <p>
 * Implementations are run from highest to lowest priority.
 */
public interface SecurityIdentityAugmentor {

    /**
     * @return The priority
     */
    default int priority() {
        return 0;
    }

    /**
     * Augments a security identity to allow for modification of the underlying identity.
     *
     * @param identity The identity
     * @return A completion stage that will resolve to the modified identity
     */
    Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context);


    /**
     * Augments a security identity to allow for modification of the underlying identity.
     *
     * @param identity The identity
     * @param context A context object that can be used to run blocking tasks
     * @param attributes All the authentication request attributes
     * @return A completion stage that will resolve to the modified identity
     */
    default Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context, Map<String, Object> attributes) {
        return augment(identity, context);
    }
}
