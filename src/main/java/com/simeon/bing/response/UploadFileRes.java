package com.simeon.bing.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UploadFileRes extends Response {
    private UploadFileRes.Data data;

    @Getter
    @Setter
    public static class Data {
        private String name;
        private String url;
    }
}
