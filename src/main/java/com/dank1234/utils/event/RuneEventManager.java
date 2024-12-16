package com.dank1234.utils.event;

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
            System.err.println("Triggered event is null!");
            return;
        }

        System.out.println("Event being triggered: " + event.getClass().getName());

        List<RegisteredHandler> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers != null) {
            for (RegisteredHandler handler : eventHandlers) {
                try {
                    System.out.println("Invoking method: " + handler.method().getName());
                    System.out.println("Handler expects: " + handler.method().getParameterTypes()[0].getName());
                    System.out.println("Actual event type: " + event.getClass().getName());

                    Class<?> expectedType = handler.method().getParameterTypes()[0];
                    if (expectedType.isAssignableFrom(event.getClass())) {
                        handler.method().invoke(handler.listener(), event);
                        // RuneEvent runeEvent = (RuneEvent) event;
                        ((RuneEvent) event).execute();
                    } else {
                        System.err.println("Event type mismatch! Cannot pass " + event.getClass().getName() +
                                " to method expecting " + expectedType.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.err.println("No handlers found for event type: " + event.getClass().getName());
        }
    }
}