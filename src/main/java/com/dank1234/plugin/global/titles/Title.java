package com.dank1234.plugin.global.titles;

public final class Title {
    private final String TEXT;
    private final int WEIGHT;

    private Title(String text, int weight) {
        this.TEXT = text;
        this.WEIGHT = weight;
    }

    public String TEXT() {
        return this.TEXT;
    }
    public int WEIGHT() {
        return this.WEIGHT;
    }

    @Override
    public String toString() {
        return "Title[TEXT="+this.TEXT()+", WEIGHT="+this.WEIGHT()+"]";
    }
}
