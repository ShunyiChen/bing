package com.simeon.bing.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse extends Response {
    private Res data;

    @Data
    public static class Res {
        private String access_token;
        private Integer expires_in;
    }
}
