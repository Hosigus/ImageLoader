package com.hosigus.imageloader.Encryptors;

import com.hosigus.imageloader.interfaces.Encryptor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 某只机智 on 2018/4/5.
 * MD5加密
 */

public class MD5Encryptor implements Encryptor {
    private static final MD5Encryptor instance = new MD5Encryptor();
    private MD5Encryptor() {}
    public static MD5Encryptor getInstance() {
        return instance;
    }
    @Override
    public String encode(String code) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] bys = digest.digest(code.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : bys) {
                int number = b & 0xff;
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    builder.append("0");
                }
                builder.append(str);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
