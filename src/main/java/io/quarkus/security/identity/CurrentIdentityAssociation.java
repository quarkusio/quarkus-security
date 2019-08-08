package io.quarkus.security.identity;

import javax.enterprise.inject.spi.CDI;

/**
 * A class that maintains the current identity association.
 * <p>
 * This interface should be implemented by a CDI bean that provides a means of associating the
 * current identity with the running thread.
 */
public interface CurrentIdentityAssociation {

    /**
     * Sets the current security identity for the thread, and returns the old one.
     * <p>
     * When the current scope is complete this method should be called again to restore the
     * old identity.
     *
     * @param identity The new identity
     * @return The old identity
     */
    SecurityIdentity setIdentity(SecurityIdentity identity);

    /**
     * Gets the current identity. This method must not return null, but must return a representation of the
     * anonymous identity if there is no current logged in user.
     *
     * @return The current security identity
     */
    SecurityIdentity getIdentity();

    static SecurityIdentity current() {
        return CDI.current().select(CurrentIdentityAssociation.class).get().getIdentity();
    }

}
