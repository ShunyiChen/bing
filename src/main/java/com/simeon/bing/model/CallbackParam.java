package com.simeon.bing.model;

import com.simeon.bing.request.GetRecordsReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class CallbackParam {
    private GetRecordsReq queryParam;
    private List<PatientRecord> results;
    private Long totalCount;
}
