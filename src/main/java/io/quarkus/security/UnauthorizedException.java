package io.quarkus.security;

/**
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 */
public class UnauthorizedException extends SecurityException {
    public UnauthorizedException() {

    }

    public UnauthorizedException(String errorMessage) {
        this(errorMessage, null);
    }

    public UnauthorizedException(Throwable cause) {
        this(null, cause);
    }

    public UnauthorizedException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
