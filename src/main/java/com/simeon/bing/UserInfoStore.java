package com.simeon.bing;

public class UserInfoStore {
    private static String userName;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserInfoStore.userName = userName;
    }
}
