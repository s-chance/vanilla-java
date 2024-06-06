package org.entropy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Class<?> clazz = Class.forName("org.entropy.User");
        Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, int.class);
        Object obj = constructor.newInstance("entropy", 55);
        Method method = clazz.getDeclaredMethod("myPrivateMethod", String.class, String.class);
        method.setAccessible(true);
        method.invoke(obj, "hello world", "!");
    }
}
