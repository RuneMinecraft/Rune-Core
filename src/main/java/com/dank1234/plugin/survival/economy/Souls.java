package com.dank1234.plugin.survival.economy;

public class Souls extends Currency{
    @Override
    public String getName() {
        return "Souls";
    }

    @Override
    public char getColour() {
        return '3';
    }

    @Override
    public char getSymbol() {
        return '£';
    }
}
