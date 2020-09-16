package io.quarkus.security;

/**
 * Exception indicating that a user authentication flow has failed and no challenge is required.
 *
 * For example, it can be used to avoid the redirect loops during an OpenId Connect authorization code flow
 * after the user has already authenticated at the IDP site but a state parameter is not available after
 * a redirect back to the Quarkus endpoint or if the code flow can not be completed for some other reasons.
 */
public class AuthenticationCompletionException extends RuntimeException {

    public AuthenticationCompletionException() {

    }

    public AuthenticationCompletionException(String errorMessage) {
        this(errorMessage, null);
    }

    public AuthenticationCompletionException(Throwable cause) {
        this(null, cause);
    }

    public AuthenticationCompletionException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
