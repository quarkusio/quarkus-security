package io.quarkus.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that can be used to annotate a CDI bean method that checks
 * if a matching {@link PermissionsAllowed} permission with the {@link #value()} name can be granted.
 * For example:
 * <pre>
 * {@code
 * @Path("hello")
 * public class HelloResource {
 *
 *     @PermissionsAllowed("speak")
 *     @GET
 *     public String sayHello() {
 *         return "Hello World!";
 *     }
 *
 *     @PermissionChecker("speak")
 *     public boolean canSpeak(SecurityIdentity identity) {
 *         return "speaker".equals(identity.getPrincipal().getName());
 *     }
 * }
 * }
 * </pre>
 * The permission checker methods can include any of the secured method parameters, matched by name.
 * Consider the following secured method:
 * <pre>
 * {@code
 * @PermissionsAllowed("update")
 * public String updateString(String a, String b, String c, String d) {
 *     ...
 * }
 * }
 * </pre>
 * The permission checker that grants access to the {@code updateString} method can include
 * any of the {@code updateString} method parameters, {@link io.quarkus.security.identity.SecurityIdentity} can also be included:
 * <pre>
 * {@code
 * @PermissionChecker("update")
 * public boolean canUpdate(String c, String a, SecurityIdentity identity) {
 *     ...
 * }
 * }
 * </pre>
 * The permission checker method parameters are matched with the secured method parameters exactly the same way as
 * the constructor parameters of a custom permission are. Please see {@link PermissionsAllowed#params()} for more information.
 * <pre>
 * If the {@link PermissionsAllowed} annotation lists several permission names and its {@link PermissionsAllowed#inclusive} property is set to `true` then an equal number of permission checker methods must be available.
 * Consider the following secured method:
 * <pre>
 * {@code
 * @PermissionsAllowed(value={"read:all", "write"}, inclusive=true)
 * public String readWriteString(String a) {
 *     ...
 * }
 * }
 * </pre>
 * For the access to the {@code readWriteString} method be granted, two permission checkers,
 * one for the `read:all` permission, and another one for the `write` permission, must be available:
 * <pre>
 * {@code
 * @PermissionChecker("read:all")
 * public boolean canRead(SecurityIdentity identity) {
 *     ...
 * }
 * @PermissionChecker("write")
 * public boolean canWrite(SecurityIdentity identity) {
 *     ...
 * }
 * }
 * </pre>
 * Note that a permission checker matches one of the {@link PermissionsAllowed} permissions if their String names are equal.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionChecker {

    /**
     * Specifies a permission this checker grants.
     *
     * @see PermissionsAllowed#value()
     * @return name of the permission this checker grants
     */
    String value();

}
