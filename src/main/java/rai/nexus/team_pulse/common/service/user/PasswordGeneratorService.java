package rai.nexus.team_pulse.common.service.user;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class PasswordGeneratorService {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase();
    private static final String NUMBERS = "0123456789";
    private static final String SPECIALS = "!@#$%^&*()_+-=";
    private static final String ALL_CHARS = UPPER + LOWER + NUMBERS + SPECIALS;

    private static final SecureRandom RANDOM = new SecureRandom();

    public String generatePassword(int length) {
        if(length < 4) {
            throw new IllegalArgumentException("Password length should be at least 4");
        }

        StringBuilder password = new StringBuilder(length);

        password.append(UPPER.charAt(RANDOM.nextInt(UPPER.length())));
        password.append(LOWER.charAt(RANDOM.nextInt(LOWER.length())));
        password.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        password.append(SPECIALS.charAt(RANDOM.nextInt(SPECIALS.length())));


        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARS.charAt(RANDOM.nextInt(ALL_CHARS.length())));
        }

        char[] passwordChars = password.toString().toCharArray();

        for (int i = passwordChars.length - 1; i > 0; i--) {
            int indexToSwap = RANDOM.nextInt(i + 1);

            char temp = passwordChars[i];
            passwordChars[i] = passwordChars[indexToSwap];
            passwordChars[indexToSwap] = temp;
        }

        return   new String(password);

    }
}
