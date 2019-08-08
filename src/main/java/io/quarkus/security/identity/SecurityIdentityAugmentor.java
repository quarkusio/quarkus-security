package io.quarkus.security.identity;

import java.util.concurrent.CompletionStage;

/**
 * An interface that allows for a {@link SecurityIdentity} to be modified after creation.
 * <p>
 * Implementations of this interface should be CDI beans. At run time all CDI beans that implement this interface
 * will be used to augment the {@link SecurityIdentity}, with the order determined via the {@link #priority()} field.
 *
 * Implementations are run from highest to lowest priority.
 */
public interface SecurityIdentityAugmentor {

    /**
     *
     * @return The priority
     */
    int priority();

    /**
     * Augments a security identity to allow for modification of the underlying identity.
     *
     * @param identity The identity
     * @return A completion stage that will resolve to the modified identity
     */
    CompletionStage<SecurityIdentity> augment(SecurityIdentity identity);

    /**
     * Augments a security identity to allow for modification of the underlying identity.
     *
     * @param identity The identity
     * @return A completion stage that will resolve to the modified identity
     */
    default CompletionStage<SecurityIdentity> augment(CompletionStage<SecurityIdentity> identity) {
        return identity.thenCompose(this::augment);
    }

}
