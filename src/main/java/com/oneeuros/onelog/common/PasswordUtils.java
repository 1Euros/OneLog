package com.oneeuros.onelog.common;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    //패스워드 인코딩
    public static String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // 비밀번호 확인
    public static boolean checkPassword(String password, String encodedPassword) {
        return BCrypt.checkpw(password, encodedPassword);
    }

}
