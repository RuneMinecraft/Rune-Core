package com.dank1234.plugin.survival.economy;

public class Tokens extends Currency {
    @Override
    public String getName() {
        return "Tokens";
    }

    @Override
    public char getColour() {
        return 'e';
    }

    @Override
    public char getSymbol() {
        return '$';
    }
}
