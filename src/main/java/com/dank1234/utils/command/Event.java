package com.dank1234.utils.command;

import com.dank1234.utils.server.ServerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Event {
    ServerType server() default ServerType.GLOBAL;
}
