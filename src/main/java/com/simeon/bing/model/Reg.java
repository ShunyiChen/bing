package com.simeon.bing.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 农村土地承包经营权确权登记公示表
 */
@Data
@Accessors(fluent = true)
@ToString
public class Reg implements Serializable {
    /** 记录ID */
    private String id;
    /** 单位 */
    private String dw;
    /** 日期 */
    private String rq;
    /** 承包方编号 */
    private String cbfbh;
    /** 承包方名称 */
    private String cbfmc;
    /** 承包方类型 */
    private String cbflx;
    /** 家庭成员 */
    private String jtcy;
    /** 性名 */
    private String xm;
    /** 证件号码 */
    private String zjhm;
    /** 家庭关系 */
    private String jtgx;
    /** 是否共有人 */
    private String sfgyr;
    /** 备注 */
    private String bz;
    /** 地块名称 */
    private String dkmc;
    /** 地块编码 */
    private String dkbm;
    /** 图幅编号 */
    private String tfbh;
    /** 二轮合同面积 */
    private String elhtmj;
    /** 二轮合同总面积 */
    private String elhtzmj;
    /** 实测面积 */
    private String scmj;
    /** 实测总面积 */
    private String sczmj;
    /** 确权面积 */
    private String qqmj;
    /** 确权总面积 */
    private String qqzmj;
    /** 东 */
    private String east;
    /** 南 */
    private String south;
    /** 西 */
    private String west;
    /** 北 */
    private String north;
    /** 土地用途 */
    private String tdyt;
    /** 等级 */
    private String dj;
    /** 土地利用类型 */
    private String tdlylx;
    /** 是否基本农田 */
    private String sfjbnt;
    /** 地块类别 */
    private String dklb;
    /** 种植类型 */
    private String zzlx;
    /** 经营方式 */
    private String jyfs;
    /** 耕保类型 */
    private String gblx;
    /** 备注 */
    private String bzz;
}
