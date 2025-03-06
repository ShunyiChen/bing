package com.simeon.bing.response;

import com.simeon.bing.model.SysUser;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetUserInfoRes {
    private SysUser user;
}
