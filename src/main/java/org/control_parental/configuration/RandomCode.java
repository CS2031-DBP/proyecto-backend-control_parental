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

}
