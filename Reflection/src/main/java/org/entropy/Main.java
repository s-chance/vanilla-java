package org.entropy;

import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException {
        Class<?> clazz = Class.forName("org.entropy.User");
        Field field = clazz.getDeclaredField("name");
        System.out.println(field.getType());
        System.out.println(field.getDeclaredAnnotation(MyAnnotation.class));
    }
}
