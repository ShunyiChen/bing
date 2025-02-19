package com.simeon.bing;

public class TokenStore {
    // 静态变量保存token
    private static String accessToken;

    // 保存token的静态方法
    public static void saveToken(String token) {
        accessToken = token;
    }

    // 获取token的静态方法
    public static String getToken() {
        return accessToken;
    }

    // 清空token的静态方法（可选）
    public static void clearToken() {
        accessToken = null;
    }
}
