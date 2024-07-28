package com.dank1234.utils.data;

public final class Version {
    private final VersionType TYPE;
    private final String VERSION;

    private Version(VersionType type, String version) {
        this.TYPE = type;
        this.VERSION = version;
    }

    public static Version of(VersionType versionType, String version) {
        return new Version(versionType, version);
    }

    @Override
    public String toString() {
        return VERSION+"-"+TYPE;
    }
}
