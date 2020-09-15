package io.quarkus.security;

/**
 * Exception indicating that a redirect is required for the authentication flow to complete.
 *
 * For example, it can be used during an OpenId Connect authorization code flow to redirect
 * the user to the original request URI which was used before the authorization code flow has started.
 */
public class AuthenticationRedirectException extends RuntimeException {

    final int code;
    final String redirectUri;

    public AuthenticationRedirectException(String redirectUri) {
        this(302, redirectUri);
    }

    public AuthenticationRedirectException(int code, String redirectUri) {
        this.code = code;
        this.redirectUri = redirectUri;
    }

    public int getCode() {
        return this.code;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
