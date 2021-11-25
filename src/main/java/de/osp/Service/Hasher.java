package de.osp.Service;

import org.springframework.http.HttpStatus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
    public static String hash(String plain) throws NoSuchAlgorithmException {
        // Apply hash.
        StringBuilder sb = new StringBuilder();
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] data = md.digest(plain.getBytes());
        for (byte datum : data) {
            sb.append(Integer.toString((datum & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
