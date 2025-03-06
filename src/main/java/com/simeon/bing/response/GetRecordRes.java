package com.simeon.bing.response;

import com.simeon.bing.model.PatientRecord;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRecordRes extends Response {
    private PatientRecord data;
}
