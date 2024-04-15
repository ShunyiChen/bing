package com.simeon.bing.utils;

import com.simeon.bing.dao.ParamsDAO;
import com.simeon.bing.model.Settings;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParamUtils {

    static Map<String, String> paramMap = new HashMap<>();

    public static void init() {
        try {
            List<Settings> paramList =  ParamsDAO.findAllParams();
            paramList.forEach(e -> {
                paramMap.put(e.getName(), e.getValue());
            });
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getValue(String name) {
        return paramMap.get(name);
    }
}
