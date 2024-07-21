package com.dank1234.utils.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd {
    Server server();
    String[] names();
    String[] perms();
    boolean disabled();
}
