package org.entropy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> clazz = Class.forName("org.entropy.User");
        Method method = clazz.getDeclaredMethod("myPrivateStaticMethod", String.class);
        method.setAccessible(true);
        method.invoke(null, "hello world");
    }
}
