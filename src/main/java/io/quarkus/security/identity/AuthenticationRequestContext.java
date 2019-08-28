package io.quarkus.security.identity;

import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

/**
 * A context object that can be used to run blocking tasks
 * <p>
 * Blocking identity providers should used this context object to run blocking tasks, to prevent excessive and
 * unnecessary delegation to thread pools
 */
public interface AuthenticationRequestContext {

    CompletionStage<SecurityIdentity> runBlocking(Supplier<SecurityIdentity> function);

}
