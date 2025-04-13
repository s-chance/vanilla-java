package io.github.schance;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ApplicationContext ioc = new ApplicationContext("io.github.schance");
        Object cat = ioc.getBean("Cat");
        System.out.println(cat);
        Object dog = ioc.getBean("spark");
        System.out.println(dog);
    }
}