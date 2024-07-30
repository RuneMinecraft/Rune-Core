package com.dank1234.utils.server;

public enum ServerType {
    GLOBAL,
    HUB,
    SURVIVAL;

    @Override
    public String toString() {
        return this.name();
    }
}
