package com.lafarleaf.leafguard.utils.enums;

public enum AppUserPermission {
    STUDENT_READ("student:read"), // Allow READ query on student.
    STUDENT_WRITE("stuent:write"), // Allow WRITE query on student.
    COURSE_READ("course:write"), // Allow READ query on course.
    COURSE_WRITE("course:write"); // Allow WRITE query on course.

    private final String permission;

    AppUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
