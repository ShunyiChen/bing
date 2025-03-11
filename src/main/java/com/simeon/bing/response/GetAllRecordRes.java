package com.simeon.bing.response;

import com.simeon.bing.model.PatientRecord;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetAllRecordRes extends Response {
    private List<PatientRecord> data;
}
