package com.simeon.bing.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Response {
    private Integer code;
    private String msg;
}
