package org.entropy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Container {
    private Map<Class<?>, Method> methods;
    private Object config;
    private Map<Class<?>, Object> services;

    public void init() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.methods = new HashMap<>();
        this.services = new HashMap<>();
        Class<?> clazz = Class.forName("org.entropy.Config");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getDeclaredAnnotation(Bean.class) != null) {
                this.methods.put(method.getReturnType(), method);
            }
        }
        this.config = clazz.getConstructor().newInstance();
    }

    public Object getServiceInstanceByClass(Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
        if (this.services.containsKey(clazz)) {
            return this.services.get(clazz);
        } else {
            if (this.methods.containsKey(clazz)) {
                Method method = this.methods.get(clazz);
                Object obj = method.invoke(this.config);
                this.services.put(clazz, obj);
                return obj;
            }
        }
        return null;
    }

    public Object createInstance(Class<?> clazz) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getDeclaredAnnotation(Autowired.class) != null) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] arguments = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    arguments[i] = getServiceInstanceByClass(parameterTypes[i]);
                }
                return constructor.newInstance(arguments);
            }
        }
        return clazz.getConstructor().newInstance();
    }
}
