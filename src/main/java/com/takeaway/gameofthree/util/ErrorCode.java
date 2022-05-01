package com.takeaway.gameofthree.util;

public enum ErrorCode {
    MandatoryRequestParamMissing("101", "Mandatory request param missing in body."),
    SecondPlayerNotAvailable("102", "Second player is not available yet, Game ended."),
    UnknownReason("999", "Unknown reason.");

    private final String code;
    private final String message;

    ErrorCode(String reason, String message) {
        this.code = reason;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
