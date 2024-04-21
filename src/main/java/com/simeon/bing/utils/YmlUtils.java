package com.simeon.bing.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class YmlUtils {
    private static String PATH = "conf/profile.yaml";
    public static void set(String key, String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put(key, value);
        Yaml yaml = new Yaml();
        // Write data to a YAML file
        try (FileWriter writer = new FileWriter(PATH)) {
            yaml.dump(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        try (InputStream inputStream = new FileInputStream(PATH)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(inputStream);
            if(data != null) {
                return data.get(key).toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
