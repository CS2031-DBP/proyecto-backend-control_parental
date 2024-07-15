package org.control_parental.configuration;

import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor
public class RandomCode {

    public String generateRandomCode() {
        Random random = new Random();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6 ; i++) {
            int number = random.nextInt(10);
            sb.append(number);
        }

        return sb.toString();
    }

    public String generatePassword() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

}
