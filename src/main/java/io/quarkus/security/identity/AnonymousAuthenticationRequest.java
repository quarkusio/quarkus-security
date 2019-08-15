package io.quarkus.security.identity;

public final class AnonymousAuthenticationRequest implements AuthenticationRequest {

    public static AnonymousAuthenticationRequest INSTANCE = new AnonymousAuthenticationRequest();

}
