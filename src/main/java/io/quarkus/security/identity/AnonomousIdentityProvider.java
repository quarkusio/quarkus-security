package io.quarkus.security.identity;

import java.util.concurrent.CompletionStage;

/**
 * An identity provider that provides the anonymous identity
 */
public interface AnonomousIdentityProvider extends IdentityProvider {

    CompletionStage<SecurityIdentity> authenticate();

}
