package com.simeon.bing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory {
    private Long id;
    private String name;
    private Boolean RFE;
    private Long categoryId;
    private String shortcut;
}
