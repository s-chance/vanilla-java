package org.entropy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Address address = new Address("345 Spear Street", "94105");
        Customer customer = new Customer("entropy", "entropy@example.com");
        Order order = new Order(customer, address);
        order.getCustomer().printName();
        order.getAddress().printStreet();
    }
}
