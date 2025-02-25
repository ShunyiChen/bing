package com.simeon.bing.request;

import com.simeon.bing.model.PatientRecord;
import lombok.Data;

import java.util.List;

@Data
public class BatchAddReq {
    private List<PatientRecord> data;
}
