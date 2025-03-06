package com.simeon.bing.utils;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class YamlUtils {

    private static final Yaml yaml;

    static {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 设置YAML格式为块风格
        options.setPrettyFlow(true); // 美化输出

        Representer representer = new Representer(options); // 使用 DumperOptions 初始化 Representer
        representer.getPropertyUtils().setSkipMissingProperties(true); // 忽略缺失的属性

        yaml = new Yaml(representer, options);
    }

    /**
     * 将对象保存为YAML文件
     *
     * @param obj  要保存的对象
     * @param filePath 文件路径
     * @throws IOException 如果文件写入失败
     */
    public static void saveToYaml(Object obj, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            yaml.dump(obj, writer);
        }
    }

    /**
     * 从YAML文件中读取对象
     *
     * @param filePath 文件路径
     * @param clazz    目标对象的类类型
     * @param <T>      目标对象的类型
     * @return 从YAML文件中读取的对象
     * @throws IOException 如果文件读取失败
     */
    public static <T> T loadFromYaml(String filePath, Class<T> clazz) throws IOException {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            return yaml.loadAs(inputStream, clazz);
        }
    }
}
