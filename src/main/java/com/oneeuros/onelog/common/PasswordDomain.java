package com.oneeuros.onelog.common;

public enum PasswordDomain {
    POST, COMMENT;

    public static PasswordDomain validDomain(String domain) {
        for (PasswordDomain d : PasswordDomain.values()) {
            if (d.name().equalsIgnoreCase(domain)) {
                return d;
            }
        }
        return null;
    }
}
