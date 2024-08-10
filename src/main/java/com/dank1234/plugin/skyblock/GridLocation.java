package com.dank1234.plugin.skyblock;

public class GridLocation {
    private final int x;
    private final int y;

    public GridLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getMinX() {
        return x * Island.ISLAND_SIZE();
    }
    public int getMinY() {
        return y * Island.ISLAND_SIZE();
    }
    public int getMaxX() {
        return (x + 1) * Island.ISLAND_SIZE();
    }
    public int getMaxY() {
        return (y + 1) * Island.ISLAND_SIZE();
    }

    @Override
    public String toString() {
        return "["+x+", "+y+"]";
    }
}