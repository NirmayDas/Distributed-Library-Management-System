package com.nirmaydas.serverside;
//I am using an XOR encrypting method. I will XOR each ASCII value with a key (below) and store it and in order to verify use passwords I run the XORs in reverse which will decrypt them

public class PasswordEncryptor {
    private static final String KEY = "secretkey";

    public static String encrypt(String password) {
        if (password == null || password.isEmpty()) {
            return password;
        }
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            char passwordChar = password.charAt(i);
            char keyChar = KEY.charAt(i % KEY.length());
            char encryptedChar = (char) (passwordChar ^ keyChar); //xor ^
            encrypted.append(encryptedChar);
        }
        return encrypted.toString();
    }

    //decypt method is virtually the same since XOR works in reverse
    public static String decrypt(String encryptedPassword) {
        if (encryptedPassword == null || encryptedPassword.isEmpty()) {
            return encryptedPassword;
        }
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < encryptedPassword.length(); i++) {
            char encryptedChar = encryptedPassword.charAt(i);
            char keyChar = KEY.charAt(i % KEY.length());
            char decryptedChar = (char) (encryptedChar ^ keyChar);
            decrypted.append(decryptedChar);
        }
        return decrypted.toString();
    }
}