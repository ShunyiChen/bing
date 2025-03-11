package com.simeon.bing;

public class APIs {
    public static String BASE_URL = "http://localhost:8080";
    public static String LOGIN = BASE_URL + "/auth/login";
    public static String LOGOUT = BASE_URL + "/auth/logout";
    public static String GET_ROUTERS = BASE_URL + "/system/menu/getRouters";
    public static String ADD_RECORDS = BASE_URL + "/bing/record";
    public static String UPDATE_RECORDS = BASE_URL + "/bing/record/update";
    public static String GET_RECORDS = BASE_URL + "/bing/record/list";
    public static String GET_RECORDS_WITH_FILES = BASE_URL + "/bing/record/listWithFiles";
    public static String GET_ALL_WITH_FILES = BASE_URL + "/bing/record/getAllWithFiles";
    public static String GET_RECORD = BASE_URL + "/bing/record/";
    public static String GET_DICT_DATA = BASE_URL + "/system/dict/data/list";
    public static String ADD_FILE = BASE_URL + "/bing/files";
    public static String UPDATE_FILE = BASE_URL + "/bing/files/update";
    public static String GET_USER_INFO = BASE_URL + "/system/user/getInfo";
    public static String GET_FILES = BASE_URL + "/bing/files/findFiles";
    public static String UPLOAD_FILES = BASE_URL + "/file/upload";
}
