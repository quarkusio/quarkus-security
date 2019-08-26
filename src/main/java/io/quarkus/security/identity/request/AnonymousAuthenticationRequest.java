package io.quarkus.security.identity.request;

/**
 * A request the for the Anonymous identity
 */
public final class AnonymousAuthenticationRequest implements AuthenticationRequest {

    public static final AnonymousAuthenticationRequest INSTANCE = new AnonymousAuthenticationRequest();

}
