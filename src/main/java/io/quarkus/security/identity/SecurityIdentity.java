package io.quarkus.security.identity;

import java.security.Permission;
import java.security.Principal;
import java.util.Set;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import io.quarkus.security.credential.Credential;

/**
 * Interface that represents the currently logged in user.
 * <p>
 * Instances of this class will always be available for injection even if no user is currently
 * logged in. In this case {@link #isAnonymous()} will return <code>true</code>, and the user
 * will generally not have any roles (although some implementation may assign roles to anonymous users).
 * <p>
 * Implementations should be immutable.
 */
public interface SecurityIdentity {

    /**
     * The attribute name that is used to store the underlying user representation.
     */
    String USER_ATTRIBUTE = "quarkus.user";

    /**
     * @return the {@link Principal} representing the current user.
     */
    Principal getPrincipal();

    /**
     * @return <code>true</code> if this identity represents an anonymous (i.e. not logged in) user
     */
    boolean isAnonymous();

    /**
     * Returns the set of all roles held by the user. These roles must be resolvable in advance for every request.
     * <p>
     * If more advanced authorization support is required than can be provided by a simple role based system
     * then {@link #checkPermission(Permission)} and {@link #checkPermissionBlocking(Permission)} should be used
     * instead.
     * <p>
     * This set should either be unmodifiable, or a defensive copy so attempts to change the role set do not modify
     * the underlying identity.
     *
     * @return The set of all roles held by the user
     */
    Set<String> getRoles();

    /**
     * Checks if a user has a given role. This is a convenience method that is equivalent to
     * <code>getRoles().contains(role);</code>.
     *
     * @param role The role
     * @return <code>true</code> if the identity has the specified role
     */
    default boolean hasRole(String role) {
        return getRoles().contains(role);
    }

    /**
     * Gets the users credential of the given type, or <code>null</code> if a credential of the given type is not
     * present.
     *
     * @param credentialType The type of the credential
     * @param <T>            The type of the credential
     * @return The credential
     */
    <T extends Credential> T getCredential(Class<T> credentialType);

    /**
     * Returns a set of all credentials owned by this user.
     *
     * @return a set of all credentials
     */
    Set<Credential> getCredentials();

    /**
     * Gets an attribute from the identity.
     * <p>
     * These can be arbitrary, and extensions are encouraged to use name spaced attribute names in a similar
     * manner to package names.
     * <p>
     * The `quarkus.` namespace is reserved
     * <p>
     * The root
     *
     * @param name The attribute name
     * @param <T>  The type of the attribute
     * @return The attribute value
     */
    <T> T getAttribute(String name);

    /**
     * Checks if a user holds a given permissions, and if so will return <code>true</code>.
     *
     * This method is asynchronous, as it may involve calls to a remote resource.
     *
     * @param permission The permission
     * @return A completion stage that will resolve to true if the user has the specified permission
     */
    CompletionStage<Boolean> checkPermission(Permission permission);

    /**
     * Checks if a user holds a given permissions, and if so will return <code>true</code>.
     *
     * This method is a blocking version of {@link #checkPermission(Permission)}. By default it will
     * just wait for the {@link CompletionStage} to be complete, however it is likely that some implementations
     * will want to provide a more efficient version.
     *
     * @param permission The permission
     * @return A completion stage that will resolve to true if the user has the specified permission
     */
    default boolean checkPermissionBlocking(Permission permission) {
        try {
            return checkPermission(permission).toCompletableFuture().join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw e;
        }
    }
}
