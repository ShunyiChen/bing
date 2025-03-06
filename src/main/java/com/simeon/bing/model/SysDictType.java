package com.simeon.bing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典类型表 sys_dict_type
 * 
 * @author Bing
 */
@Setter
@Getter
@AllArgsConstructor
public class SysDictType extends BaseEntity {
    /** 字典主键 */
    private Long dictId;
    /** 字典名称 */
    private String dictName;
    /** 字典类型 */
    private String dictType;
    /** 状态（0正常 1停用） */
    private String status;

    @Override
    public String toString() {
        return dictName;
    }
}
