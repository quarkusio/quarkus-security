package io.quarkus.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that can be used to annotate a CDI bean method that checks
 * if a {@link io.quarkus.security.identity.SecurityIdentity} holds a permission specified by the {@link #value()}.
 * For example:
 * <pre>
 * {@code
 * &#64;Path("hello")
 * public class HelloResource {
 *
 *     &#64;PermissionsAllowed("speak")
 *     &#64;GET
 *     public String sayHello() {
 *         return "Hello World!";
 *     }
 *
 *     &#64;PermissionChecker("speak")
 *     public boolean canSpeak(SecurityIdentity identity) {
 *         return "speaker".equals(identity.getPrincipal().getName());
 *     }
 * }
 * }
 * </pre>
 * The permission checker methods can include any of secured method parameters (matched by name).
 * Consider the following secured method:
 * <pre>
 * {@code
 * &#64;PermissionsAllowed("update")
 * public String updateString(String a, String b, String c, String d) {
 *     ...
 * }
 * }
 * </pre>
 * The permission checker that grants access to the {@code updateString} method can inject
 * any arguments it requires and optionally even {@link io.quarkus.security.identity.SecurityIdentity}:
 * <pre>
 * {@code
 * &#64;PermissionChecker("update")
 * public boolean canUpdate(String c, String a, SecurityIdentity identity) {
 *     ...
 * }
 * }
 * </pre>
 * The permission checker method parameters are matched with the secured method parameters in exactly same fashion
 * as are constructor parameters of a custom permission. Please see {@link PermissionsAllowed#params()} for more information.
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
