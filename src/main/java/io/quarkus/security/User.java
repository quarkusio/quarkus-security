package io.quarkus.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * A qualifier than can be used to inject the current user.
 *
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface User {
}
