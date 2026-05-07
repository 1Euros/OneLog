package com.oneeuros.onelog.common;

public enum PasswordAction {
    EDIT, DELETE;

    public static PasswordAction validAction(String action) {

        for (PasswordAction a : PasswordAction.values()) {
            if (a.name().equalsIgnoreCase(action)) {
                return a;
            }
        }
        throw new IllegalArgumentException("잘못된 action: "+ action);
    }
}
