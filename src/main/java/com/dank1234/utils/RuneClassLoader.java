package com.dank1234.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RuneClassLoader extends ClassLoader {
    private final Map<String, Class<?>> classes = new HashMap<>();
    private final File classesDir;

    public RuneClassLoader(File classesDir) {
        this.classesDir = classesDir;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (classes.containsKey(name)) {
            return classes.get(name);
        }

        File classFile = new File(classesDir, name.replace('.', File.separatorChar) + ".class");
        if (!classFile.exists()) {
            throw new ClassNotFoundException(name);
        }

        try (FileInputStream fis = new FileInputStream(classFile)) {
            byte[] buffer = new byte[(int) classFile.length()];
            int bytesRead = fis.read(buffer);
            if (bytesRead != buffer.length) {
                throw new IOException("Could not read entire file: " + classFile.getName());
            }

            Class<?> clazz = defineClass(name, buffer, 0, buffer.length);
            classes.put(name, clazz);
            return clazz;
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
    }
}