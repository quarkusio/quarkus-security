package io.quarkus.security;

import java.security.Permission;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents permission based on simple string comparison.
 *
 * @see Permission
 */
public final class StringPermission extends Permission {

    public static final String ACTIONS_SEPARATOR = ",";
    private final Set<String> actions;

    /**
     * Constructs a permission with the specified name and actions.
     *
     * @param permissionName must not be null or empty and must not contain comma
     * @param actions optional actions; action itself must not be null or empty and must not contain comma
     */
    public StringPermission(String permissionName, String... actions) {
        super(validateAndTrim(permissionName, "Permission name"));
        if (actions != null && actions.length != 0) { // OPTIONAL
            this.actions = checkActions(actions);
        } else {
            this.actions = Collections.emptySet();
        }
    }

    private static Set<String> checkActions(String[] actions) {
        Set<String> validActions = new HashSet<>(actions.length, 1);
        for (String action : actions) {
            validActions.add(validateAndTrim(action, "Action"));
        }
        return Collections.unmodifiableSet(validActions);
    }

    private static String validateAndTrim(String str, String paramName) {
        if (str == null) {
            throw new IllegalArgumentException(String.format("%s must not be null", paramName));
        }
        str = str.trim();
        if (str.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s must not be empty", paramName));
        }
        if (str.contains(ACTIONS_SEPARATOR)) {
            // important for equals and hashCode
            throw new IllegalArgumentException(String.format("%s must not contain '%s'", paramName, ACTIONS_SEPARATOR));
        }
        return str;
    }

    /**
     * Checks if this StringPermission object "implies" the specified permission.
     * <p>
     * More precisely, this method returns true if:
     * <ul>
     * <li> {@code p} is an instance of the StringPermission
     * <li> {@code p}'s name equals this object's name
     * <li> compared permissions have no actions, or this object's actions contains at least one of the {@code p} actions
     * </ul>
     *
     * @param p the permission to check against
     *
     * @return true if the specified permission is implied by this object
     */
    @Override
    public boolean implies(Permission p) {
        if (!(p instanceof StringPermission) || !getName().equals(p.getName())) {
            return false;
        }
        StringPermission that = (StringPermission) p;

        // actions are optional, however if at least one action was specified,
        // an intersection of compared sets must not be empty
        if (that.actions.isEmpty()) {
            // no required actions
            return true;
        }
        if (actions.isEmpty()) {
            // no possessed actions
            return false;
        }
        for (String action : that.actions) {
            if (actions.contains(action)) {
                // has at least one of required actions
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object p) {
        if (this == p) {
            return true;
        }
        if (!(p instanceof StringPermission)) {
            return false;
        }
        StringPermission that = (StringPermission) p;
        return getName().equals(that.getName()) && actions.equals(that.actions);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toString().toCharArray());
    }

    /**
     * @return null if no actions were specified, or actions joined together with the {@link #ACTIONS_SEPARATOR}
     */
    @Override
    public String getActions() {
        return actions.isEmpty() ? null : String.join(ACTIONS_SEPARATOR, actions);
    }

}
