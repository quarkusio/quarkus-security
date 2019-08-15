package io.quarkus.security.identity;

import io.quarkus.security.credential.PasswordCredential;

/**
 * An simple authentication request that uses a username and password
 */
public class UsernamePasswordAuthenticationRequest implements AuthenticationRequest {

    private final String username;
    private final PasswordCredential password;

    public UsernamePasswordAuthenticationRequest(String username, PasswordCredential password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public PasswordCredential getPassword() {
        return password;
    }
}
