package com.oneeuros.onelog.common;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {

    public static void validatePassword(String password) {
        if (password == null || !password.matches("^[0-9]{6}$")) {
            throw new IllegalArgumentException("비밀번호는 숫자 6자리여야 합니다.");
        }
    }
}
