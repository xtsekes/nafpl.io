package dev.nafplio.auth.impl;

import java.time.Instant;

class Users {
    public static String normalizeEmail(String email) {
        return email.toLowerCase();
    }

    public static String generateSecurityStamp() {
        return Instant.now().toString();
    }
}

