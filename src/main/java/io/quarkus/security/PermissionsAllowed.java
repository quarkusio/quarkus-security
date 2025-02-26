package io.quarkus.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.security.Permission;

/**
 * Lists one or more required permissions that must be granted.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Repeatable(PermissionsAllowed.List.class)
public @interface PermissionsAllowed {

    /**
     * Constant value for {@link #params()} indicating that the constructor parameters of the {@link #permission()}
     * should be autodetected based on formal parameter names. For example, consider the following method secured with this
     * annotation:
     * <pre>
     * {@code
     * &#64;PermissionsAllowed(value = "resource:retrieve", permission = UserPermission.class)
     * public Resource getResource(String param1, String param2, String param3) {
     *      // business logic
     * }
     * }
     * </pre>
     * The {@code getResource} method parameters {@code param1} and {@code param3} will be
     * matched with the {@code UserPermission} constructor parameters {@code param1} and {@code param3}.
     * <pre>
     * {@code
     * public class UserPermission extends Permission {
     *
     *     public UserPermission(String name, String param3, String param1) {
     *         ...
     *     }
     *
     *     ...
     * }
     * }
     * If no method parameter name matches the constructor parameter name, Quarkus checks names of fields and methods
     * declared on the method parameter type.
     * For example:
     * <pre>
     * {@code
     * record BeanParam2(String param1, String param2) {}
     * record BeanParam3(String param3) {}
     * record BeanParam1(BeanParam2 beanParam2, BeanParam3 beanParam3) {
     *
     * }
     *
     * &#64;PermissionsAllowed(value = "resource:retrieve", permission = UserPermission.class)
     * public Resource getResource(BeanParam1 beanParam) {
     *      // business logic
     * }
     *
     * }
     * }
     * </pre>
     * In this example, resolution of the {@code param1} and {@code param3} formal parameters is unambiguous.
     * For more complex scenarios, we suggest to specify {@link #params()} explicitly.
     */
    String AUTODETECTED = "<<autodetected>>";

    /**
     * Colon is used to separate a {@link Permission#getName()} and an element of the {@link Permission#getActions()}.
     * For example, {@link StringPermission} created for the 'getResource' method:
     *
     * <pre>
     * &#64;PermissionsAllowed("resource:retrieve")
     * public Resource getResource() {
     *     // business logic
     * }
     * </pre>
     * is equal to the {@code perm}:
     * <pre>
     * var perm = new StringPermission("resource", "retrieve");
     * </pre>
     */
    String PERMISSION_TO_ACTION_SEPARATOR = ":";

    /**
     * Specifies a list of permissions that grants access to the resource.
     * It is also possible to define permission actions that are permitted for the resource.
     * Consider the `getResource` method:
     *
     * <pre>
     * &#64;PermissionsAllowed({"resource:crud", "resource:retrieve", "system-resource:retrieve"})
     * public Resource getResource() {
     *     // business logic
     * }
     * </pre>
     *
     * Two {@link StringPermission} permissions will be created:
     *
     * <pre>
     * var pem1 = new StringPermission("resource", "crud", "retrieve");
     * var pem2 = new StringPermission("system-resource", "retrieve");
     * </pre>
     *
     * The permission check will pass if either {@code pem1} or {@code pem2} implies user permissions.
     * It is also possible to combine permissions with and without actions like this:
     *
     * <pre>
     * &#64;PermissionsAllowed({"resource:crud", "resource:retrieve", "natural-resource"})
     * public Resource getResource() {
     *     // business logic
     * }
     * </pre>
     *
     * Quarkus will create two permissions:
     *
     * <pre>
     * var pem1 = new StringPermission("resource", "crud", "retrieve");
     * var pem2 = new StringPermission("natural-resource");
     * </pre>
     * Alternatively, when multiple required permissions must be listed, you can repeat the annotation, for example:
     * <pre>
     * &#64;PermissionsAllowed("create")
     * &#64;PermissionsAllowed("update")
     * public Resource createOrUpdate(Long id) {
          // business logic
     * }
     * </pre>
     *
     * @see StringPermission#implies(Permission) for more details on how the above example is evaluated.
     *
     * @return permissions
     */
    String[] value();

    /**
     * Choose a relation between multiple permissions specified in {@link #value()}.
     * By default, at least one of permissions must be granted.
     * You can request that all of the listed  permissions by setting the `inclusive` property to `true`.
     * For example:
     *
     * <pre>
     * &#64;PermissionsAllowed(value = {"resource:crud", "resource:retrieve", "natural-resource"}, inclusive = true)
     * public Resource getResource() {
     *     // business logic
     * }
     * </pre>
     *
     * Two {@link StringPermission}s will be created:
     *
     * <pre>
     * var pem1 = new StringPermission("resource", "crud", "retrieve");
     * var pem2 = new StringPermission("system-resource", "retrieve");
     * </pre>
     *
     * And the permission check will pass if <b>both</b> {@code pem1} and {@code pem2} imply user permissions.
     *
     * @return `true` if permissions should be inclusive
     */
    boolean inclusive() default false;

    /**
     * Mark parameters of the annotated method that should be passed to the constructor of the {@link #permission()}.
     * Consider the following three classes:
     *
     * <pre>
     * class ResourceIdentity { }
     * class User extends ResourceIdentity { }
     * class Admin extends ResourceIdentity { }
     * </pre>
     *
     * Next, consider the secured 'getResource' method:
     *
     * <pre>
     * &#64;PermissionsAllowed(permission = UserPermission.class, value = "resource", params = {user1, admin1})
     * public Resource getResource(User user, User user1, Admin admin, Admin admin1) {
     *     // business logic
     * }
     * </pre>
     *
     * In the example above, the parameters {@code user1} and {@code admin1} are marked as {@link #permission()} constructor
     * arguments:
     *
     * <pre>
     * public class UserPermission extends Permission {
     *
     *     private final ResourceIdentity user;
     *     private final ResourceIdentity admin;
     *
     *     public UserPermission(String name, ResourceIdentity user1, ResourceIdentity admin1) {
     *         super(name);
     *         this.user = user1;
     *         this.admin = admin1;
     *     }
     *
     *     ...
     * }
     * </pre>
     *
     * Please note that:
     * <ul>
     * <li>The constructor parameter names {@code user1} and {@code admin1} must match respective {@code PermissionsAllowed#params}</li>
     * <li>`ResourceIdentity` can also be used as a constructor parameter data type</li>
     * </ul>
     *
     * When this annotation is used as the class-level annotation, it applies to every secured method in the class.
     *
     * Method parameter fields or methods can be passed to a Permission constructor as well.
     * Consider the following secured method and its parameters:
     * <pre>
     * {@code
     * &#64;PermissionsAllowed(permission = UserPermission.class, value = "resource", params = {"admin1.param1", "user1.param3"})
     * public Resource getResource(User user, User user1, Admin admin, Admin admin1) {
     *     // business logic
     * }
     * class ResourceIdentity {
     *     private final String param1;
     *
     *     public String getParam1() {
     *         return param1;
     *     }
     * }
     * class User extends ResourceIdentity {
     *     public String getParam3() {
     *         return "param3";
     *     }
     * }
     * class Admin extends ResourceIdentity { }
     * }
     * </pre>
     *
     * The corresponding {@code UserPermission} constructor would look like this:
     *
     * <pre>
     * public class UserPermission extends Permission {
     *
     *     public UserPermission(String name, String param1, String param3) {
     *     }
     *
     *     ...
     * }
     * </pre>
     *
     * The constructor parameter {@code param1} refers to the {@code admin1#param1} secured method parameter
     * and the constructor parameter {@code param3} refers to the {@code user1#getParam3} secured method parameter.
     *
     * @see #AUTODETECTED
     *
     * @return constructor parameters passed to the {@link #permission()}
     */
    String[] params() default AUTODETECTED;

    /**
     * The class that extends the {@link Permission} class to create a permission specified in {@link #value()}.
     *
     * For example:
     *
     * <pre>
     * public class UserPermission extends Permission {
     *
     *     private final String[] permissions;
     *
     *     public UserPermission(String name, String... actions) {
     *         super(name);
     *         this.actions = actions;
     *     }
     *
     *     ...
     * }
     * </pre>
     *
     * {@code actions} parameter is optional and may be omitted.
     *
     * @return permission class
     */
    Class<? extends Permission> permission() default StringPermission.class;

    /**
     * The repeatable holder for {@link PermissionsAllowed}. The annotation can only be repeatable on methods.
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        /**
         * The {@link PermissionsAllowed} instances.
         *
         * @return the instances
         */
        PermissionsAllowed[] value();
    }
}
