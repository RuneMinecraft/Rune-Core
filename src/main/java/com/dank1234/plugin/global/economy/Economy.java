package com.dank1234.plugin.global.economy;

public enum Economy {
    TOKENS,
    SOULS,
    GEMS;

    public static Economy getByOrdinal(int i) {
        return switch(i) {
            case 0 -> TOKENS;
            case 1 -> SOULS;
            case 2 -> GEMS;
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };
    }
    public static Economy getByName(String name) {
        return switch (name) {
            case "tokens" -> TOKENS;
            case "souls" -> SOULS;
            case "gems" -> GEMS;
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };
    }

    public String getName() {
        return this.name().toUpperCase().charAt(0)+this.name().toUpperCase().substring(1).toLowerCase();
    }

}
