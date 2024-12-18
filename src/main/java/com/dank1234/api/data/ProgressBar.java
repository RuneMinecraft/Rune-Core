package com.dank1234.api.data;

public final class ProgressBar {
    private final double percentage;

    private ProgressBar(final double percentage) {
        this.percentage = percentage;
    }
    public static ProgressBar of(final double percentage) {
        return new ProgressBar(percentage);
    }

    public String openingCharacter() {
        return "&a[&r";
    }
    public String closingCharacter() {
        return "&a]";
    }
    public String uncompletedCharacter() {
        return "&c■";
    }
    public String completedCharacter() {
        return "&a■";
    }
    public int barLength() {
        return 25;
    }

    public double percentage() {
        return this.percentage;
    }

    private String generate() {
        final int completedLength = (int) ((this.percentage() / 100) * this.barLength());
        final int uncompletedLength = this.barLength() - completedLength;

        return  this.openingCharacter() +
                this.completedCharacter().repeat(Math.max(0, completedLength)) +
                this.uncompletedCharacter().repeat(Math.max(0, uncompletedLength)) +
                this.closingCharacter();
    }

    @Override
    public String toString() {
        return this.generate();
    }
}
