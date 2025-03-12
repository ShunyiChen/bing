package com.simeon.bing;

import java.util.HashMap;
import java.util.Map;

public class LicenseStore {
    private static Map<String, String> licenseMap = new HashMap<>();
    static {
        // 注册所有用户License
        licenseMap.put("scanmanager", "6e11114ad38b54be7d6fa8d0d9d786c3c51de26a37b177f6dfb4e23fb2250549");
    }

    public static String getEncryptedLicenseInfo(String userName) {
        return licenseMap.getOrDefault(userName, "");
    }
}
