package com.dank1234.plugin.skyblock;

public class Grid {
    private static int currentX = 0;
    private static int currentY = 0;
    private static int step = 0;
    private static boolean moveX = true;

    private static final int ISLAND_SIZE = 500;

    public static GridLocation next() {
        GridLocation location = new GridLocation(currentX, currentY);

        if (moveX) {
            currentX += step % 2 == 0 ? 1 : -1;
        } else {
            currentY += step % 2 == 0 ? 1 : -1;
        }
        step++;

        if (step % 2 == 0) {
            moveX = !moveX;
        }
        return location;
    }
}