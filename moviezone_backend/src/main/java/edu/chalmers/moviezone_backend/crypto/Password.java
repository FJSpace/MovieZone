/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend.crypto;

import org.mindrot.jbcrypt.BCrypt;

/**
 * This class is for crypt and decrypt passwords so that we dont
 * store password in plain text in the database
 * @author Space
 * 
 */
public class Password {
    private static final int workload = 12;

    /**
     * To crypt a password before storing it in the database
     * @param password the password on plain text
     * @return The hashed string that will be stored in the database
     */
    public static String hashPassword(String password) {
        String salt = BCrypt.gensalt(workload);
        String hashed_password = BCrypt.hashpw(password, salt);
        return(hashed_password);
    }
    
    /**
     * To decrypt a password. It is used to verify that the user have 
     * given the correct password when trying to login
     * @param input The string that the user send when he try to login
     * @param crypt_password The crypted password that is stored for that user
     * @return 
     */
    public static boolean checkPassword(String input, String crypt_password) {
        boolean ok_password;

        if(null == crypt_password || !crypt_password.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
        ok_password = BCrypt.checkpw(input, crypt_password);
        return ok_password;
    }
}
