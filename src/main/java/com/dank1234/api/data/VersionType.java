package com.dank1234.api.data;

public enum VersionType {
    RELEASE,
    DEVELOPMENT,
    SNAPSHOT;

    @Override
    public String toString() {
        return this.name();
    }
}
