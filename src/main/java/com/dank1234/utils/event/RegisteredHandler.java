package com.dank1234.utils.event;

import java.lang.reflect.Method;

record RegisteredHandler(RuneListener listener, Method method, Priority priority) {
}
