package com.simeon.bing.utils;

import cn.hutool.crypto.symmetric.AES;

import java.nio.charset.StandardCharsets;

public class AESUtils {
    private static final String MODE = "CBC";
    private static final String PADDING = "PKCS7Padding";
    private static String iv = "09efd2776381bd87";
    private static String key = "b6fb6933bef669fd2dd8dc59fb8c5494";

    public static String complexAESEncrypt(String txt) {
        AES aes = new AES(MODE, PADDING,
                // 密钥，可以自定义
                key.getBytes(),
                // iv加盐，按照实际需求添加
                iv.getBytes());
        return aes.encryptHex(txt);
    }

    public static String complexAESDecrypt(String txt) {
        AES aes = new AES(MODE, PADDING,
                // 密钥，可以自定义
                key.getBytes(),
                // iv加盐，按照实际需求添加
                iv.getBytes());
        return aes.decryptStr(txt, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        String userName = "scanmanager";
        String expiration = "2025-03-14";
        String licenseInfo = userName + "|" + expiration;
        System.out.println(complexAESEncrypt(licenseInfo));
    }
}
