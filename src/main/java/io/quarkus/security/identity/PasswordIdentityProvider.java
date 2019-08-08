package io.quarkus.security.identity;

import java.util.concurrent.CompletionStage;

import io.quarkus.security.credential.PasswordCredential;

/**
 * An identity provider that uses a simple username and password combination.
 */
public interface PasswordIdentityProvider extends IdentityProvider {

    CompletionStage<SecurityIdentity> authenticate(String username, PasswordCredential password);

}
