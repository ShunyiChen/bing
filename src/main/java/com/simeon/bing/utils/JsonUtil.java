package com.simeon.bing.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.util.List;
import java.util.TimeZone;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 配置 ObjectMapper 忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 配置日期格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 设置时区
    }

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

    /**
     * 将JSON字符串转换为Java List对象
     *
     * @param json JSON字符串
     * @param clazz List中元素的类型
     * @return Java List对象
     * @throws Exception 如果转换失败
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) throws Exception {
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}
