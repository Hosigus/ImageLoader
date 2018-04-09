package com.hosigus.imageloader.Encryptors;

import com.hosigus.imageloader.interfaces.Encryptor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 某只机智 on 2018/4/5.
 * SHA加密
 */

public class SHAEncryptor implements Encryptor {
    private static final SHAEncryptor instance = new SHAEncryptor();
    private SHAEncryptor() {}
    public static SHAEncryptor getInstance() {
        return instance;
    }

    @Override
    public String encode(String code) {
        StringBuilder builder = new StringBuilder();
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA");
            sha.update(code.getBytes());
            for (byte b : sha.digest()) {
                builder.append(String.format("%02X", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
