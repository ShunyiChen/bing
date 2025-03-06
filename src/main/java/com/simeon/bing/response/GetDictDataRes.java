package com.simeon.bing.response;

import com.simeon.bing.model.PatientRecord;
import com.simeon.bing.model.SysDictData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetDictDataRes extends Response {
    /** 总记录数 */
    private long total;
    /** 列表数据 */
    private List<SysDictData> rows;
}
