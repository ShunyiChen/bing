package com.simeon.bing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TablePage {
    private String name;
    private Integer pageSize;

    @Override
    public String toString() {
        return name;
    }
}
