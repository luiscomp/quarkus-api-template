package com.logicsoftware.utils.enums;

public enum AppStatus {
    SUCCESS("success"),
    ERROR("error");

    private String status;

    AppStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
