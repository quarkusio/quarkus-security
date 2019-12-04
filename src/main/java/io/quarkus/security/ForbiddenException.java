package io.quarkus.security;

/**
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 */
public class ForbiddenException extends SecurityException {
    public ForbiddenException() {

    }

    public ForbiddenException(String errorMessage) {
        this(errorMessage, null);
    }

    public ForbiddenException(Throwable cause) {
        this(null, cause);
    }

    public ForbiddenException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
