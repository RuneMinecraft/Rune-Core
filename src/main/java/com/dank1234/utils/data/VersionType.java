package com.dank1234.utils.data;

public enum VersionType {
    RELEASE,
    DEVELOPMENT,
    SNAPSHOT;

    @Override
    public String toString() {
        return this.name();
    }
}
