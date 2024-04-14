package com.simeon.bing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统设置表
 */
@Data
@Builder
public class Settings implements Serializable {
    private Long id;
    private String name;
    private String value;
    private String remark;
}
