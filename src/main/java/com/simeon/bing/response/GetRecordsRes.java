package com.simeon.bing.response;

import com.simeon.bing.model.PatientRecord;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetRecordsRes extends Response {
    /** 总记录数 */
    private long total;

    /** 列表数据 */
    private List<PatientRecord> rows;
}
