package com.dank1234.plugin.global.chat;

public enum Colour {
    WHITE("&f"),
    YELLOW(""),
    LIGHT_PURPLE(""),
    RED(""),
    AQUA(""),
    GREEN(""),
    BLUE("")
    ;

    private final String s;

    Colour(String s) {
        this.s = s;
    }

    public String s() {
        return this.s;
    }
}
