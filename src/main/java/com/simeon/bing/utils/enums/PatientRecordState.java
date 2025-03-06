package com.simeon.bing.utils.enums;

public enum PatientRecordState {
    NEW("New"),
    SUBMITTED("Submitted"),
    MODIFIED("Modified");

    private final String state;

    PatientRecordState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
