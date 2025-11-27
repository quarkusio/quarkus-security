package io.quarkus.security.identity;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.security.Principal;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Identifies a method that must be run with a new {@link SecurityIdentity} that is valid only
 * for the duration of this method's execution.
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface RunAsUser {

    /**
     * Configures {@link Principal#getName()} as retrieved by the new identity method {@link SecurityIdentity#getPrincipal()}.
     *
     * @return The username used as the new identity principal name.
     */
    String user();

    /**
     * Configures {@link SecurityIdentity#getRoles()} on the new {@link SecurityIdentity}.
     *
     * @return The new identity roles.
     */
    String[] roles() default {};

}
