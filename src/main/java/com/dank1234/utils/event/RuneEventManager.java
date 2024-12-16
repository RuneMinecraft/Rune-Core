package com.dank1234.utils.event;

import com.dank1234.utils.Logger;

import java.lang.reflect.Method;
import java.util.*;

public class RuneEventManager {
    private final Map<Class<?>, List<RegisteredHandler>> handlers = new HashMap<>();

    public void registerListener(RuneListener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Event.class)) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1) {
                    Class<?> eventClass = params[0];
                    Event annotation = method.getAnnotation(Event.class);

                    handlers.computeIfAbsent(eventClass, k -> new ArrayList<>())
                            .add(new RegisteredHandler(listener, method, annotation.priority()));
                }
            }
        }

        handlers.values().forEach(list -> list.sort(Comparator.comparing(RegisteredHandler::priority)));
    }

    public void triggerEvent(Object event) {
        if (event == null) {
            Logger.error("Triggered event is null!");
            return;
        }

        List<RegisteredHandler> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers != null) {
            for (RegisteredHandler handler : eventHandlers) {
                try {
                    Class<?> expectedType = handler.method().getParameterTypes()[0];
                    if (expectedType.isAssignableFrom(event.getClass())) {
                        handler.method().invoke(handler.listener(), event);
                        ((RuneEvent) event).execute();
                    } else {
                        System.err.println("Event type mismatch! Cannot pass " + event.getClass().getName() +
                                " to method expecting " + expectedType.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}