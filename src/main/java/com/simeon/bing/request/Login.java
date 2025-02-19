package com.simeon.bing.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Login {
    private String username;
    private String password;
    private String desktop;
}
