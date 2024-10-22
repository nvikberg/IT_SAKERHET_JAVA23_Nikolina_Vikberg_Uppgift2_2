package com.example.timecapsule.Service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class EncryptionService {

    // Use a constant 32-byte secret (256-bit for AES)
    private static final String SECRET = "RmV2dDJDZzJ5MkVakjsdhakdjasdhqwertysafdslkjqwe"; // Exactly 32 bytes for AES-256

    // Generate a new AES key (optional if you're using a constant secret)
    public SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        return keyGen.generateKey();
    }

    // Encrypts data using the AES key
    public String encrypt(String data) throws Exception {
        // Ensure the secret is exactly 32 bytes for AES-256
        SecretKeySpec secretKeySpec = new SecretKeySpec(getKey(SECRET), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt the encrypted text using the AES key
    public String decrypt(String encryptedText) throws Exception {
        // Ensure the secret is exactly 32 bytes for AES-256
        SecretKeySpec secretKeySpec = new SecretKeySpec(getKey(SECRET), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    // Utility to ensure the secret is 32 bytes for AES-256 (using SHA-256 hash)
    private byte[] getKey(String secret) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(secret.getBytes(StandardCharsets.UTF_8));
        return key; // Use first 32 bytes for AES-256
    }
}
