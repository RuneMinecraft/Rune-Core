package com.dank1234.utils.wrapper.message;

public enum Messages {
    PERMISSION("&cNo Permission!"),
    ARGUMENTS("&cInvalid Arguments!"),
    NULL_PLAYER("&cThat player does not exist in our databases!"),
    EXCEPTION_THROWN("&cAn exception has occurred. Please contact one of our support team!");

    private final String message;
    private String message() {
        return message;
    }

    Messages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message();
    }
}
