package com.myclient.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    
    private Map<Class<? extends Event>, List<MethodData>> registry = new HashMap<>();
    
    public void register(Object obj) {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventTarget.class)) {
                method.setAccessible(true);
                
                if (method.getParameterCount() == 1) {
                    Class<?> eventClass = method.getParameterTypes()[0];
                    
                    if (Event.class.isAssignableFrom(eventClass)) {
                        Class<? extends Event> eventType = (Class<? extends Event>) eventClass;
                        registry.computeIfAbsent(eventType, k -> new ArrayList<>())
                                .add(new MethodData(obj, method));
                    }
                }
            }
        }
    }
    
    public void unregister(Object obj) {
        for (List<MethodData> dataList : registry.values()) {
            dataList.removeIf(data -> data.source.equals(obj));
        }
    }
    
    public void call(Event event) {
        List<MethodData> methods = registry.get(event.getClass());
        if (methods != null) {
            for (MethodData data : methods) {
                try {
                    data.method.invoke(data.source, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static class MethodData {
        Object source;
        Method method;
        
        MethodData(Object source, Method method) {
            this.source = source;
            this.method = method;
        }
    }
}