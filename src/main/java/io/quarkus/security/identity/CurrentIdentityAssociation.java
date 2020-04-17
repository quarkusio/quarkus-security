package io.quarkus.security.identity;

import javax.enterprise.inject.spi.CDI;

import io.smallrye.mutiny.Uni;

/**
 * A class that maintains the current identity association.
 * <p>
 * This interface should be implemented by a CDI bean that provides a means of associating the
 * current identity with the running thread.
 */
public interface CurrentIdentityAssociation {

    /**
     * Sets the current security identity for the thread.
     * <p>
     * It is the responsibility of the implementation/runtime to clear the identity at the appropriate time.
     * <p>
     * In general this will be achieved by using a request scoped bean to manage the identity so it is scoped to
     * the current request.
     *
     * @param identity The new identity
     */
    void setIdentity(SecurityIdentity identity);

    /**
     * Sets the current deferred security identity for the thread. This is a Uni that will resolve to the logged in
     * user.
     * <p>
     * Note that as Uni is lazy in some circumstances authentication will only be attempted if the Uni is subscribed to.
     * <p>
     * It is the responsibility of the implementation/runtime to clear the identity at the appropriate time.
     * <p>
     * In general this will be achieved by using a request scoped bean to manage the identity so it is scoped to
     * the current request.
     *
     * @param identity The new identity
     */
    void setIdentity(Uni<SecurityIdentity> identity);

    /**
     * Gets the current identity. This method must not return null, but must return a representation of the
     * anonymous identity if there is no current logged in user.
     * <p>
     * If a deferred identity has been set this may throw {@link io.quarkus.security.AuthenticationFailedException}
     * if authentication fails when the Uni is resolved.
     *
     * @return The current security identity
     */
    SecurityIdentity getIdentity();

    /**
     * Gets the (potentially lazy) security identity wrapped in a Uni.
     * <p>
     * Note that as Uni is lazy in some circumstances authentication will not actually be attempted until
     * the Uni is subscribed to.
     * <p>
     * If there is no logged in user the Uni will resolve to the anonymous identity.
     *
     * @return the current security identity
     */
    Uni<SecurityIdentity> getDeferredIdentity();

    static SecurityIdentity current() {
        return CDI.current().select(CurrentIdentityAssociation.class).get().getIdentity();
    }

}
