package com.flex.task_tracker.security.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class HashUtil {

    public static String hash(String pw) {

        return BCrypt.hashpw(pw, BCrypt.gensalt(12));

    }

    public static boolean checkEncrypted(String candidate, String hash) {

        return BCrypt.checkpw(candidate, hash);

    }
}
