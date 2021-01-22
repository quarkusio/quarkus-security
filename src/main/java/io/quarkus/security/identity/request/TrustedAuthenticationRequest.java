package io.quarkus.security.identity.request;


/**
 * A request to authenticate from a trusted source, such as an encrypted cookie
 */
public class TrustedAuthenticationRequest extends BaseAuthenticationRequest implements AuthenticationRequest {

    private final String principal;

    public TrustedAuthenticationRequest(String principal) {
        this.principal = principal;
    }

    public String getPrincipal() {
        return principal;
    }
}
