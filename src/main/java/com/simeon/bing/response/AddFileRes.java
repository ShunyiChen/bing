package com.simeon.bing.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddFileRes extends Response {
    private Data data;

    @Getter
    @Setter
    public static class Data {
        private Long id;
    }
}
