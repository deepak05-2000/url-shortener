package com.urlshortener.redirect.util;

import com.urlshortener.redirect.repository.ShortenedUrlRepository;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Base62Converter {

    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final int MAX_RETRIES = 5;
    private static final int CODE_LENGTH = 8;
    private static ShortenedUrlRepository shortenedUrlRepository;

    public Base62Converter(ShortenedUrlRepository shortenedUrlRepository) {
        this.shortenedUrlRepository = shortenedUrlRepository;
    }

    public static String generateShortUrl(String longUrl) {
        String shortCode;
        int retries = 0;
        do {
            shortCode = generateShortCode(longUrl, retries);
            retries++;
        } while (shortenedUrlRepository.existsByShortCode(shortCode) && retries < MAX_RETRIES);

        if (retries >= MAX_RETRIES) {
            throw new RuntimeException("Failed to generate unique short code");
        }

        return shortCode;
    }

    private static String generateShortCode(String longUrl, int salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((longUrl + salt).getBytes(StandardCharsets.UTF_8));

            return encode(hash).substring(0, CODE_LENGTH);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found");
        }
    }

    private static String encode(byte[] input) {
        BigInteger number = new BigInteger(1, input);
        StringBuilder encoded = new StringBuilder();
        while (number.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = number.divideAndRemainder(BigInteger.valueOf(62));
            encoded.insert(0, BASE62_CHARS.charAt(divmod[1].intValue()));
            number = divmod[0];
        }
        return encoded.toString();
    }
}


