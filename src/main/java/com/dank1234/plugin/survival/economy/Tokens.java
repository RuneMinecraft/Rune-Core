package com.dank1234.plugin.survival.economy;

public class Tokens extends Currency {
    @Override
    public String getName() {
        return "tokens";
    }

    @Override
    public char getSymbol() {
        return '$';
    }
}
