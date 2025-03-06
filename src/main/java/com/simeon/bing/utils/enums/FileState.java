package com.simeon.bing.utils.enums;

public enum FileState {
    UPLOADED("Uploaded"),
    WAITING_UPLOAD("Waiting Upload"),
    UPLOAD_FAILED("Upload Failed");

    private final String state;

    FileState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}