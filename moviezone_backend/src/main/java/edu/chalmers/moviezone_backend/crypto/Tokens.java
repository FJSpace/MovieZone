/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend.crypto;

import java.security.Key;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;

/**
 * Class that we use to read and create tokens
 * @author Space
 */
public class Tokens {
    //A key for crypting and decrypting the token
    private static final Key key = new AesKey(ByteUtil.randomBytes(16));
    
    public static String toToken(Long id) throws JoseException{
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setPayload(id.toString());
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
        jwe.setKey(key);
        return jwe.getCompactSerialization();
    }
    
    public static Long fromToken(String token) throws JoseException{
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setKey(key);
        jwe.setCompactSerialization(token);
        return Long.parseLong(jwe.getPayload());
    }
}
