package com.simeon.bing.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecordStatus {
    private String name;
    private Integer code;

    @Override
    public String toString() {
        return name;
    }
}
