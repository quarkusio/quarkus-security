package io.quarkus.security;

/**
 * Exception indicating that a redirect is required for the authentication flow to complete.
 *
 * <p>For example, it can be used during an OpenId Connect authorization code flow to redirect
 * the user to the original request URI which was used before the authorization code flow has started.
 *
 * <p><b>Security warning:</b> The {@code redirectUri} is not validated by this class.
 * Callers <b>must</b> ensure that the redirect URI is safe before constructing this exception.
 * Failing to validate the URI can lead to open redirect vulnerabilities (CWE-601), which
 * can be exploited for phishing attacks. At a minimum, callers should verify that the URI
 * is a relative path or that its host belongs to a trusted allowlist.
 */
public class AuthenticationRedirectException extends RuntimeException implements AuthenticationException {

    final int code;
    final String redirectUri;

    /**
     * Creates a redirect exception with HTTP 302 status.
     *
     * <p><b>Security warning:</b> The {@code redirectUri} is not validated by this class.
     * Callers <b>must</b> ensure that the redirect URI is safe before constructing this exception.
     * Failing to validate the URI can lead to open redirect vulnerabilities (CWE-601), which
     * can be exploited for phishing attacks. At a minimum, callers should verify that the URI
     * is a relative path or that its host belongs to a trusted allowlist.
     *
     * @param redirectUri the URI to redirect to
     */
    public AuthenticationRedirectException(String redirectUri) {
        this(302, redirectUri);
    }

    /**
     * Creates a redirect exception with the given HTTP status code.
     *
     * <p><b>Security warning:</b> The {@code redirectUri} is not validated by this class.
     * Callers <b>must</b> ensure that the redirect URI is safe before constructing this exception.
     * Failing to validate the URI can lead to open redirect vulnerabilities (CWE-601), which
     * can be exploited for phishing attacks. At a minimum, callers should verify that the URI
     * is a relative path or that its host belongs to a trusted allowlist.
     *
     * @param code the HTTP redirect status code (must be 3xx)
     * @param redirectUri the URI to redirect to
     * @throws IllegalArgumentException if {@code code} is not a 3xx status code
     */
    public AuthenticationRedirectException(int code, String redirectUri) {
        if (code < 300 || code > 399) {
            throw new IllegalArgumentException(
                    "HTTP status code must be a redirect status (3xx), got: " + code);
        }
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
