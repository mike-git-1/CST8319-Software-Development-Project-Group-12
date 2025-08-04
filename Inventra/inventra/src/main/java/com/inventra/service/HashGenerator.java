package com.inventra.service;

public class HashGenerator {
    public static void main(String[] args) throws Exception {
        String password = "admin123"; // your test password

        String salt = AuthService.generateSalt();
        String hash = AuthService.hashPassword(password, salt);

        System.out.println("Salt: " + salt);
        System.out.println("Hash: " + hash);
    }
}