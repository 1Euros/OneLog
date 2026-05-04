package com.oneeuros.onelog.common;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordUtils {

    //숫자 6자리 검증
    public static void validatePassword(String password) {
        if (password == null || !password.matches("^[0-9]{6}$")) {
            throw new IllegalArgumentException("비밀번호는 숫자 6자리여야 합니다.");
        }
    }

    //패스워드 인코딩
    public static String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // 비밀번호 확인
    public static boolean checkPassword(String password, String encodedPassword) {
        return BCrypt.checkpw(password, encodedPassword);
    }

}
