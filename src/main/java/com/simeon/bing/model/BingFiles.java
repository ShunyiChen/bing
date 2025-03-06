package com.simeon.bing.model;

import lombok.Getter;
import lombok.Setter;


/**
 * 【病案材料】对象 bing_files
 * 
 * @author Simeon
 * @date 2025-03-04
 */
@Setter
@Getter
public class BingFiles extends BaseEntity
{
    /** 主键ID */
    private Long id;
    /** 病案ID */
    private Long recordId;
    /** 病案分类名称 */
    private String recordClassificationName;
    /** 材料分类名称 */
    private String classificationName;
    /** 文件名称 */
    private String fileName;
    /** 文件存储路径 */
    private String filePath;
    /** 文件记录状态，uploaded/waiting upload */
    private String status;
}
