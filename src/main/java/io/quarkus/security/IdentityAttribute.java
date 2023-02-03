package io.quarkus.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jakarta.inject.Qualifier;

import io.quarkus.security.identity.IdentityProvider;

/**
 * A qualifier than can be used to inject attributes from the current identity.
 *
 * The only standard attribute that should be provided wherever applicable is the 'quarkus.user'
 * attribute, which represents the underlying user representation. This representation will
 * depend on the underlying {@link IdentityProvider} that is in use, e.g. for a JPA based one
 * this will likely be a JPA entity representing the current user.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface IdentityAttribute {

    /**
     *
     * @return The attribute name
     */
    String value();
}
