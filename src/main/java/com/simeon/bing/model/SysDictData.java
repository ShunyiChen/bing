package com.simeon.bing.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典数据表 sys_dict_data
 * 
 * @author Bing
 */
@Setter
@Getter
public class SysDictData extends BaseEntity {
    /** 字典编码 */
    private Long dictCode;
    /** 字典排序 */
    private Long dictSort;
    /** 字典标签 */
    private String dictLabel;
    /** 字典键值 */
    private String dictValue;
    /** 字典类型 */
    private String dictType;
    /** 样式属性（其他样式扩展） */
    private String cssClass;
    /** 表格字典样式 */
    private String listClass;
    /** 是否默认（Y是 N否） */
    private String isDefault;
    /** 状态（0正常 1停用） */
    private String status;

    /** 是否为文件节点 */
    private boolean file;
    /** 文件相对路径 */
    private String filePath;
    /** 文件记录状态 Uploaded/Waiting Upload/Upload Failed */
    private String fileStatus;
    /** 文件记录主键ID */
    private Long fileId;

    public SysDictData() {}

    public SysDictData(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    @Override
    public String toString() {
        return dictLabel;
    }
}
