package com.takeaway.gameofthree.util;

import java.security.SecureRandom;

public class Utility {
    public static int getRandomNumber(int upperLimit) {
        SecureRandom secureRandom = new SecureRandom();
        int randomNumber = secureRandom.nextInt(upperLimit);
        return randomNumber;
    }
}
