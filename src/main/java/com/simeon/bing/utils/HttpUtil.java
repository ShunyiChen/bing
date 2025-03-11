package com.simeon.bing.utils;

import com.simeon.bing.Settings;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpUtil {

    /**
     * 发送GET请求
     *
     * @param urlString 请求URL
     * @param accessToken accessToken
     * @return 响应内容
     * @throws Exception 如果请求失败
     */
    public static String sendGetRequest(String urlString, String accessToken) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken); // 添加Authorization头

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }
        } else {
            throw new RuntimeException("HTTP GET request failed with error code: " + responseCode);
        }
    }

    /**
     * 发送GET请求携带表单参数
     *
     * @param baseUrl
     * @param queryParams
     * @param accessToken
     * @return
     * @throws Exception
     */
    public static String sendGetRequest(String baseUrl, Map<String, String> queryParams, String accessToken) throws Exception {
        // 构建带有查询参数的URL
        StringBuilder urlWithParams = new StringBuilder(baseUrl);
        if (queryParams != null && !queryParams.isEmpty()) {
            urlWithParams.append("?");
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                String encodedKey = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
                String encodedValue = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
                urlWithParams.append(encodedKey).append("=").append(encodedValue).append("&");
            }
            // 去掉最后一个多余的"&"
            urlWithParams.setLength(urlWithParams.length() - 1);
        }

        URL url = new URL(urlWithParams.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken); // 添加Authorization头

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }
        } else {
            throw new RuntimeException("HTTP GET request failed with error code: " + responseCode);
        }
    }

    /**
     * 发送POST请求（JSON格式参数）
     *
     * @param urlString 请求URL
     * @param jsonInputString JSON格式的请求体
     * @param accessToken accessToken
     * @return 响应内容
     * @throws Exception 如果请求失败
     */
    public static String sendPostRequest(String urlString, String jsonInputString, String accessToken) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken); // 添加Authorization头
        connection.setDoOutput(true);

        // 发送请求体
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 读取响应
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }
        } else {
            throw new RuntimeException("HTTP POST request failed with error code: " + responseCode);
        }
    }

    /**
     * 批量上传多个File文件
     *
     * @param urlString     请求URL
     * @param fileParamName 文件参数名
     * @param files         要上传的文件数组
     * @param accessToken   accessToken
     * @return 响应内容
     * @throws Exception 如果请求失败
     */
    public static String uploadFiles(String urlString, String fileParamName, File[] files, String accessToken) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8), true)) {

            for (File file : files) {
                String path = file.getPath();
                // 使用 replace 方法删除指定的部分
                String result = path.replace(Settings.LOCAL_STORAGE_PATH, "");
                // 添加文件部分
                writer.append("------WebKitFormBoundary7MA4YWxkTrZu0gW\r\n");
                writer.append("Content-Disposition: form-data; name=\"").append(fileParamName).append("\"; filename=\"").append(result).append("\"\r\n");
                writer.append("Content-Type: ").append(getContentType(file)).append("\r\n\r\n");
                writer.flush();

                // 将文件内容写入请求体
                try (FileInputStream fileInputStream = new FileInputStream(file)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    os.flush();
                }

                writer.append("\r\n");
                writer.flush();
            }

            writer.append("------WebKitFormBoundary7MA4YWxkTrZu0gW--\r\n");
            writer.flush();
        }

        // 读取响应
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }
        } else {
            throw new RuntimeException("HTTP multiple files upload request failed with error code: " + responseCode);
        }
    }

    /**
     * 获取文件的Content-Type
     *
     * @param file 文件
     * @return Content-Type
     */
    private static String getContentType(File file) {
        // 这里可以根据文件扩展名返回对应的Content-Type
        String fileName = file.getName();
        if (fileName.endsWith(".txt")) {
            return "text/plain";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        } else {
            return "application/octet-stream"; // 默认类型
        }
    }

}