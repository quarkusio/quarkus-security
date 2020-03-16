package io.quarkus.security.identity;

import java.util.function.Supplier;

import io.smallrye.mutiny.Uni;

/**
 * A context object that can be used to run blocking tasks
 * <p>
 * Blocking identity providers should used this context object to run blocking tasks, to prevent excessive and
 * unnecessary delegation to thread pools
 */
public interface AuthenticationRequestContext {

    Uni<SecurityIdentity> runBlocking(Supplier<SecurityIdentity> function);

}
