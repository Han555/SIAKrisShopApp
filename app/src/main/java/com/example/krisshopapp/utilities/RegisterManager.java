/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.krisshopapp.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 *
 * @author Han
 */
public class RegisterManager {
    
    public List<String> register(String username, String email, String password) {
        String salt = "";
        List<String> registrar = new ArrayList<>();
        SecurityManager secure = new SecurityManager();
        byte[] secureSalt = secure.getNextSalt();
        salt = secure.byteArrayToHexString(secureSalt);
        String toBeHashed = salt + password;
        String hashedPassword = secure.doMD5Hashing(toBeHashed);
        registrar.add(username);
        registrar.add(email);
        registrar.add(hashedPassword);
        registrar.add(salt);

        return registrar;
    }

    
    public String resetPassword(String username) {
        StringBuffer sb = new StringBuffer();
        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789/?<>}{.,$#!%&*".toCharArray();
        Random random = new Random();
        for (int i = 1; i <= 8; i++) {
            sb.append(chars[random.nextInt(chars.length)]);
        }
        String newPassword = new String(sb);

        return newPassword;
    }

    public List<String> produceNewRandomPassword() {
        List<String> newPasswordDetails = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789/?<>}{.,$#!%&*".toCharArray();
        Random random = new Random();
        for (int i = 1; i <= 8; i++) {
            sb.append(chars[random.nextInt(chars.length)]);
        }
        String newPassword = new String(sb);

        String salt = "";
        SecurityManager secure = new SecurityManager();
        byte[] secureSalt = secure.getNextSalt();
        salt = secure.byteArrayToHexString(secureSalt);
        String toBeHashed = salt + newPassword;
        String hashedPassword = secure.doMD5Hashing(toBeHashed);

        newPasswordDetails.add(hashedPassword);
        newPasswordDetails.add(salt);
        newPasswordDetails.add(newPassword);

        return newPasswordDetails;
    }

    public List<String> setUserNewPassword(String password) {
        List<String> newPasswordDetails = new ArrayList<>();

        String salt = "";
        SecurityManager secure = new SecurityManager();
        byte[] secureSalt = secure.getNextSalt();
        salt = secure.byteArrayToHexString(secureSalt);
        String toBeHashed = salt + password;
        String hashedPassword = secure.doMD5Hashing(toBeHashed);

        newPasswordDetails.add(hashedPassword);
        newPasswordDetails.add(salt);

        return newPasswordDetails;
    }
    
}
