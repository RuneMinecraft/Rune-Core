package com.dank1234.api.server;

public enum ServerType {
    GLOBAL,
    HUB,
    SURVIVAL,
    SKYBLOCK,
    BOX;

    @Override
    public String toString() {
        return this.name();
    }
}
