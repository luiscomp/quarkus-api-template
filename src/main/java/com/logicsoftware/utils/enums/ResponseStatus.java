package com.logicsoftware.utils.enums;

public enum ResponseStatus {
    SUCCESS("success"),
    ERROR("error");

    private String status;

    ResponseStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
