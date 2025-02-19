package com.simeon.bing.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将Java对象转换为JSON字符串
     *
     * @param obj Java对象
     * @return JSON字符串
     * @throws Exception 如果转换失败
     */
    public static String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * 将JSON字符串转换为Java对象
     *
     * @param json JSON字符串
     * @param clazz 目标Java类
     * @return Java对象
     * @throws Exception 如果转换失败
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws Exception {
        return objectMapper.readValue(json, clazz);
    }
}
