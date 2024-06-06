package org.entropy;

import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = Class.forName("org.entropy.User");
        Field field = clazz.getDeclaredField("privateStaticField");
        field.setAccessible(true);
        field.set(null, 100);
        System.out.println(field.get(null));
    }
}
