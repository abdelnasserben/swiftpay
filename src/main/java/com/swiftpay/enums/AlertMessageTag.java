package com.swiftpay.enums;

public enum AlertMessageTag {
    ERROR,
    SUCCESS,
    WARNING;

    @Override
    public String toString() {
        return name().toLowerCase() + "Message";
    }
}
