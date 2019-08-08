package io.quarkus.security.credential;

/**
 * A token based credential
 */
public class TokenCredential implements Credential {

    private final String token;
    private final String type;

    public TokenCredential(String token, String type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }
}
