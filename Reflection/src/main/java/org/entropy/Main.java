package org.entropy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Container container = new Container();
        container.init();
        Class<?> clazz = Class.forName("org.entropy.Customer");
        Object obj = container.getServiceInstanceByClass(clazz);
        System.out.println(obj);
    }
}
