package com.simeon.bing.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录用户表
 */
@Data
@AllArgsConstructor
public class User implements Serializable {

    private Long id;
    private String userName;
    private String password;
    private boolean enabled;

}
