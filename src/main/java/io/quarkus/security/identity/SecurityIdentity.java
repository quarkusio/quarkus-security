package io.quarkus.security.identity;

import java.security.Permission;
import java.security.Principal;
import java.util.Map;
import java.util.Set;

import io.quarkus.security.StringPermission;
import io.quarkus.security.credential.Credential;
import io.smallrye.mutiny.Uni;

/**
 * Interface that represents the current security identity such as a logged-in user or other authenticated subject.
 * <p>
 * Instances of this class will always be available for injection even if no authentication has taken place.
 * In this case {@link #isAnonymous()} will return <code>true</code>, and the security identity will generally have no roles.
 * <p>
 * Implementations should be immutable.
 */
public interface SecurityIdentity {

    /**
     * The attribute name that is used to store the underlying security identity representation.
     */
    String USER_ATTRIBUTE = "quarkus.user";

    /**
     * @return the {@link Principal} representing the current security identity.
     */
    Principal getPrincipal();

    /**
     * @param clazz {@link Principal} subclass
     * @return the {@link Principal} subclass representing the current security identity.
     */
    default <T extends Principal> T getPrincipal(Class<T> clazz) {
        return clazz.cast(getPrincipal());
    }

    /**
     * @return <code>true</code> if this identity is anonymous
     */
    boolean isAnonymous();

    /**
     * Returns the set of all roles held by the security identity. These roles must be resolvable in advance for every request.
     * <p>
     * Note that roles are returned on a best effort basis. To actually check if
     * a user holds a role {@link #hasRole(String)} should be used instead. Some API's (e.g. JAX-RS) do not allow
     * for all roles to be returned, so if the underlying identity representation does not support retrieving all the roles
     * this method will not always be reliable. In general all built in Quarkus security extensions should provide this,
     * unless it is documented otherwise.
     *
     * <p>
     * This set should either be unmodifiable, or a defensive copy so attempts to change the role set do not modify
     * the underlying identity.
     *
     * @return The set of all roles held by the user
     */
    Set<String> getRoles();

    /**
     * Checks if a security identity has a given role. These roles must be resolvable in advance for every request.
     *
     * @return <code>true</code> if the identity has the specified role.
     */
    boolean hasRole(String role);

    /**
     * Returns an unmodifiable set of permissions held by the security identity that have already been resolved
     * and can be represented as {@link Permission}.
     * <p>
     * Note that this set of permissions is not guaranteed to represent a complete set of permissions held by the identity.
     * For example, a JSON Web Token (JWT) token scope might be represented as a {@link Permission} instance, while an unresolved permission
     * that requires an asynchronous or remote permission check can not be.
     *
     * @return The set of resolved permissions that can be represented as {@link Permission}
     */
    Set<Permission> getPermissions();

    /**
     * Gets the security identity credential of the given type, or <code>null</code> if a credential of the given type is not
     * present.
     *
     * @param credentialType The type of the credential
     * @param <T>            The type of the credential
     * @return The credential
     */
    <T extends Credential> T getCredential(Class<T> credentialType);

    /**
     * Returns a set of all credentials owned by this security identity.
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
     * @return All the request attributes
     */
    Map<String, Object> getAttributes();

    /**
     * Checks if a security identity holds a given permission.
     * <p>
     * This method is asynchronous, as it may involve calls to a remote resource.
     *
     * @param permission The permission
     * @return Uni that will resolve to true if the security identity has the specified permission
     */
    Uni<Boolean> checkPermission(Permission permission);

    /**
     * Checks if a security identity holds a given permission.
     * <p>
     * This method is a blocking version of {@link #checkPermission(Permission)}..
     *
     * @param permission The permission
     * @return true if the security identity has the specified permission
     */
    default boolean checkPermissionBlocking(Permission permission) {
        return checkPermission(permission).await().indefinitely();
    }

    /**
     * Checks if a security identity holds a given permission.
     * <p>
     * This method is asynchronous, as it may involve calls to a remote resource.
     *
     * @param permission The permission
     * @return Uni that will resolve to true if the security identity has the specified permission
     */
    default Uni<Boolean> checkPermission(String permission) {
    	return checkPermission(new StringPermission(permission));
    }

    /**
     * Checks if a security identity holds a given permission.
     * <p>
     * This method is a blocking version of {@link #checkPermission(Permission)}. By default it will
     * just wait for the {@link CompletionStage} to be complete, however it is likely that some implementations
     * will want to provide a more efficient version.
     *
     * @param permission The permission
     * @return Uni that will resolve to true if the security identity has the specified permission
     */
    default boolean checkPermissionBlocking(String permission) {
        return checkPermission(permission).await().indefinitely();
    }
}
