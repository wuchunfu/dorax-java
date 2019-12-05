package org.dorax.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author wcf
 * @date 2019-12-10
 */
public class GeneratePasswordUtil {

    private final static int SIZE = 1000;
    private final static int PWD_LENGTH = 8;

    public static String generatePassword() throws NoSuchAlgorithmException {
        String[] pwdStr = {"qwertyuiopasdfghjklzxcvbnm", "QWERTYUIOPASDFGHJKLZXCVBNM", "0123456789", "~!@#$%^&*()_+{}|<>?:{}"};
        SecureRandom instance = SecureRandom.getInstance("SHA1PRNG");
        char[] chs = new char[PWD_LENGTH];
        for (int i = 0; i < pwdStr.length; i++) {
            int idx = (int) (instance.nextDouble() * pwdStr[i].length());
            chs[i] = pwdStr[i].charAt(idx);
        }
        for (int i = pwdStr.length; i < PWD_LENGTH; i++) {
            int arrIdx = (int) (instance.nextDouble() * pwdStr.length);
            int strIdx = (int) (instance.nextDouble() * pwdStr[arrIdx].length());
            chs[i] = pwdStr[arrIdx].charAt(strIdx);
        }
        for (int i = 0; i < SIZE; i++) {
            int idx1 = (int) (instance.nextDouble() * chs.length);
            int idx2 = (int) (instance.nextDouble() * chs.length);
            if (idx1 == idx2) {
                continue;
            }
            char tempChar = chs[idx1];
            chs[idx1] = chs[idx2];
            chs[idx2] = tempChar;
        }
        return new String(chs);
    }

    public static void main(String[] args) {
        try {
            System.out.println(generatePassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
