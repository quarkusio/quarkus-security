package io.quarkus.security.credential;

/**
 * A simple password based credential
 */
public class PasswordCredential implements Credential {

    private final char[] password;

    public PasswordCredential(char[] password) {
        this.password = password;
    }

    public char[] getPassword() {
        return password;
    }
}
