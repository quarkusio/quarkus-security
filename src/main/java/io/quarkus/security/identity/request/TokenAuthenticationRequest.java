package io.quarkus.security.identity.request;

import io.quarkus.security.credential.TokenCredential;

/**
 * An simple authentication request that uses a token
 */
public class TokenAuthenticationRequest extends BaseAuthenticationRequest implements AuthenticationRequest {

    private final TokenCredential token;

    public TokenAuthenticationRequest(TokenCredential token) {
        this.token = token;
    }

    public TokenCredential getToken() {
        return token;
    }
}
