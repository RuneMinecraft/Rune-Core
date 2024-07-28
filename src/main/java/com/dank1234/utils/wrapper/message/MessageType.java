package com.dank1234.utils.wrapper.message;

public enum MessageType {
    NORMAL(""),
    WARNING("&e&lWarn &8» &e"),
    ERROR("&4&lError &8» &c"),
    EXCEPTION("&4&lException &8» &c");

    private final String prefix;
    MessageType(String prefix) {
        this.prefix = prefix;
    }
    @Override
    public String toString() {
        return prefix;
    }
}
