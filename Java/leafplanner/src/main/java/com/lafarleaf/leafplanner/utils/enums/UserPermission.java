package com.lafarleaf.leafplanner.utils.enums;

public enum UserPermission {
    READ("user:read"), // Allow READ query on student.
    WRITE("user:write"); // Allow WRITE query on student.

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
