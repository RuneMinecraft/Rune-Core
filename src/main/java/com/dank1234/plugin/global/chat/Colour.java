package com.dank1234.plugin.global.chat;

import java.util.Objects;

public enum Colour {
    RED("&c"),
    DARK_RED("&4"),
    LIME("&a"),
    GREEN("&2"),
    YELLOW("&e"),
    GOLD("&6"),
    LIGHT_BLUE("&b"),
    CYAN("&3"),
    BLUE("&9"),
    DARK_BLUE("&1"),
    WHITE("&f"),
    LIGHT_GRAY("&7"),
    DARK_GRAY("&8"),
    BLACK("&0");

    private final String s;

    Colour(String s) {
        this.s = s;
    }

    public String s() {
        return this.s;
    }

    public static Colour getByCode(String s) {
        for (Colour c : Colour.values()) {
            if (Objects.equals(c.s, s)) return c;
        }
        return WHITE;
    }

    @Override
    public String toString() {
        return this.s;
    }
}
