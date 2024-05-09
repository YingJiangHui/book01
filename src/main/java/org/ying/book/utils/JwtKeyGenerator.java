package org.ying.book.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class JwtKeyGenerator {

    public static String generateSecretKey(int length) throws NoSuchAlgorithmException {
        byte[] keyBytes = generateRandomBytes(length);
        return java.util.Base64.getEncoder().encodeToString(keyBytes);
    }

    private static byte[] generateRandomBytes(int length) throws NoSuchAlgorithmException {
        byte[] keyBytes = new byte[length];
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        secureRandom.nextBytes(keyBytes);
        return keyBytes;
    }
}
