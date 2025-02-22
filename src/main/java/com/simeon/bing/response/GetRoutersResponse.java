package com.simeon.bing.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetRoutersResponse extends Response {
    private D[] data;

    @Data
    public static class D {
        private String name;
        private String path;
        private Boolean hidden;
        private String redirect;
        private String component;
        private Boolean alwaysShow;
        private M meta;
        private D[] children;
    }

    @Data
    public static class M {
        private String title;
        private String icon;
        private Boolean noCache;
        private String link;
    }
}
