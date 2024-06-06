package org.entropy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Container container = new Container();
        container.init();
        String className = "org.entropy.User";
        String fieldName = "message";
        Class<?> clazz = Class.forName(className);
        Object obj = container.createInstance(clazz);
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        Object fieldValue = field.get(obj);
        Method[] methods = fieldValue.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getDeclaredAnnotation(Printable.class) != null) {
                method.invoke(fieldValue);
            }
        }
    }
}
