package com.dank1234.api;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RuneClassLoader extends ClassLoader {
    private final Map<String, Class<?>> classes = new HashMap<>();
    private final File classesDir;

    public RuneClassLoader(File classesDir) {
        this.classesDir = classesDir;
    }

    @Override
    protected Class<?> findClass(String name) {
        return null;
    }
}