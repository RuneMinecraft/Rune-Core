package com.dank1234.utils.event;

public abstract class RuneEvent {
    public abstract void execute();
    public String getName() {
        return this.getClass().getName();
    }
}